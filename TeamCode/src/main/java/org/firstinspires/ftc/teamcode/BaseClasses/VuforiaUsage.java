package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by candy on 29/09/2017.
 */

public class VuforiaUsage {

    public static final String TAG = "Vuforia Usage";

    private static final double  ON_AXIS        =  10;      // Within 1.0 cm of target center-line
    private static final double  CLOSE_ENOUGH   =  20;      // Within 2.0 cm of final target standoff
    public  static final double  YAW_GAIN       =  0.018;   // Rate at which we respond to heading error
    public  static final double  LATERAL_GAIN   =  0.0027;  // Rate at which we respond to off-axis error
    public  static final double  AXIAL_GAIN     =  0.0017;  // Rate at which we respond to target distance errors


    private static final float MM_PER_INCH         = 25.4f;
    private static final float MM_BOT_WIDTH        = 18 * MM_PER_INCH;            // TODO: change the width to the actual width
    private static final float MM_FTC_FIELD_WIDTH  = (12*12 - 2) * MM_PER_INCH;
    private static final float MM_VUMARK_WIDTH     = 8.5f * MM_PER_INCH;
    private static final float MM_VUMARK_LENGTH    = 11 * MM_PER_INCH;
    private static final int   MAX_TARGETS         = 1;

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.FRONT;

    private VuforiaTrackables targets;
    private VuforiaTrackable  vuMark;
    private OpMode            opMode;
    private OmniDrive         robot;
    private HardwareMap       hardwareMap;

    private boolean             targetFound;
    private String              targetName;
    private double              robotX;
    private double              robotY;
    private double              targetRange;
    private double              targetBearing;
    private double              robotBearing;
    private double              relativeBearing;


    public VuforiaUsage(){

        targetFound = false;
        targetName = null;
        targets = null;

        robotX = 0;
        robotY = 0;
        targetRange = 0;
        targetBearing = 0;
        robotBearing = 0;
        relativeBearing = 0;
    }


    public enum TeamOrder{
        REDLEFT,
        REDRIGHT,
        BLUELEFT,
        BLUERIGHT
    }


    /***
     * Send telemetry data to indicate navigation status
     */
    public void addNavTelemetry() {
        if (targetFound)
        {
            // Display the current visible target name, robot info, target info, and required robot action.
            opMode.telemetry.addData("Visible", targetName);
            opMode.telemetry.addData("Robot", "[X]:[Y] (B) [%5.0fmm]:[%5.0fmm] (%4.0f°)",
                    robotX, robotY, robotBearing);
            opMode.telemetry.addData("Target", "[R] (B):(RB) [%5.0fmm] (%4.0f°):(%4.0f°)",
                    targetRange, targetBearing, relativeBearing);
            opMode.telemetry.addData("- Turn    ", "%s %4.0f°",  relativeBearing < 0 ? ">>> CW " : "<<< CCW", Math.abs(relativeBearing));
            opMode.telemetry.addData("- Strafe  ", "%s %5.0fmm", robotY < 0 ? "LEFT" : "RIGHT", Math.abs(robotY));
            opMode.telemetry.addData("- Distance", "%5.0fmm", Math.abs(robotX));
        }
        else
        {
            opMode.telemetry.addData("Visible", "- - - -" );
        }
    }


    public RelicRecoveryVuMark startIdentifying(){

        int time = 0;
        RelicRecoveryVuMark mark = RelicRecoveryVuMark.from(vuMark);

        while (mark != RelicRecoveryVuMark.UNKNOWN && time <10){
            time ++;
            opMode.telemetry.addData("vuMark visible", mark);
            opMode.telemetry.update();
            return mark;
        }

        return RelicRecoveryVuMark.UNKNOWN;
        //TODO: change the loop condition while the strategy is determined
    }


    public void activate(){

        if (targets != null)
            targets.activate();
    }


    /***
     * use target position to determine the best way to approach it.
     * Set the Axial, Lateral and Yaw axis motion values to get us there.
     *
     * @return true if we are close to target
     * @param standOffDistance how close do we get the center of the robot to target (in mm)
     */
    public boolean cruiseControl(double standOffDistance) {
        boolean closeEnough;

        // Priority #1 Rotate to always be pointing at the target (for best target retention).
        double Y  = (relativeBearing * YAW_GAIN);

        // Priority #2  Drive laterally based on distance from X axis (same as y value)
        double L  =(robotY * LATERAL_GAIN);

        // Priority #3 Drive forward based on the desiredHeading target standoff distance
        double A  = (-(robotX + standOffDistance) * AXIAL_GAIN);

        // Send the desired axis motions to the robot hardware.
        robot.setYaw(Y);
        robot.setAxial(A);
        robot.setLateral(L);

        // Determine if we are close enough to the target for action.
        closeEnough = ( (Math.abs(robotX + standOffDistance) < CLOSE_ENOUGH) &&
                (Math.abs(robotY) < ON_AXIS));

        return (closeEnough);
    }


    /**
     * This method should be considered when multiple targets exist and should not be used in 2018 FTC season
     * Comment out @Deprecated to use in 2019
     * @Deprecated Use {@link #targetIsVisible()} instead for this season
     * @return whether a target is found
     */
    @Deprecated
    public boolean targetsAreVisible(){

        int targetTestID = 0;

        // Check each target in turn, but stop looking when the first target is found.
        while ((targetTestID < MAX_TARGETS) && !targetIsVisible(targetTestID)) {
            targetTestID++ ;
        }

        return (targetFound);
    }

    public boolean targetIsVisible(){
        return targetIsVisible(0);
    }


    public boolean targetIsVisible(int targetId){

        VuforiaTrackable target = targets.get(targetId);
        VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener)target.getListener();
        OpenGLMatrix location  = null;

        // if we have a target, look for an updated robot position
        if ((target != null) && (listener != null) && listener.isVisible()) {
            targetFound = true;
            targetName = target.getName();

            // If we have an updated robot location, update all the relevant tracking information
            location  = listener.getUpdatedRobotLocation();
            if (location != null) {

                // Create a translation and rotation vector for the robot.
                VectorF trans = location.getTranslation();
                Orientation rot = Orientation.getOrientation(location, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                // Robot position is defined by the standard Matrix translation (x and y)
                robotX = trans.get(0);
                robotY = trans.get(1);

                // Robot bearing (in +vc CCW cartesian system) is defined by the standard Matrix z rotation
                robotBearing = rot.thirdAngle;

                // target range is based on distance from robot position to origin.
                targetRange = Math.hypot(robotX, robotY);

                // target bearing is based on angle formed between the X axis to the target range line
                targetBearing = Math.toDegrees(-Math.asin(robotY / targetRange));

                // Target relative bearing is the target Heading relative to the direction the robot is pointing.
                relativeBearing = targetBearing - robotBearing;
            }
            targetFound = true;
        }
        else  {
            // Indicate that there is no target visible
            targetFound = false;
            targetName = "None";
        }

        return targetFound;
    }


        /**
         * call the method right after the initiation of robot
         * @param opMode
         * @param robot
         * @param teamOrder redLeft, redRight, blueLeft, or blueRight looking from the driving station of the red/blue alliance

         */
    public void initVuforia(OpMode opMode, OmniDrive robot, HardwareMap hardwareMap,TeamOrder teamOrder) {
        this.opMode = opMode;
        this.robot = robot;
        this.hardwareMap = hardwareMap;

        /**
         * Vuforia setup
         */
        int cameraMonitorViewId = this.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ac3EJmb/////AAAAGTxjSZzhKEoetuVyiWDXtrhFJd4Ds08qhs72SLU23Q71WY3VbzkivcdTtX5L/Csl8bXqXRgDqB/Bdq267iev5J+tC4ifkc1Tk/bh2pUoSZrf9lxnHd/uU6S/acBewAJ6B7y+Uut+qHI53ge8Y6123Z6RmhJvNSHafzYTTGJfjjtUFmGshscvSXmXUSKdo/hLCaAfIy6WNq4Z59BQH+KEZA3ycyXzS+7+7qdNvNOTiVqPN7PapXlPY43wi4ZaVCu3F59lEvVgHzmA/1X8SeBI7N9Ny+771nFTfNZfVmu3BMho14gY9L6xDG/7XrvBFHqouLr4fRJa/5QOJR1g5ZNecWIcB9PveWlHfphV1196BLlw";
        parameters.cameraDirection = CAMERA_CHOICE;
        parameters.useExtendedTracking = false;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        this.opMode.telemetry.addData("Init Vufoira", "VuforiaLocalizer Created");
        this.opMode.telemetry.update();


        /**
         *targets loading
         */
        targets = vuforia.loadTrackablesFromAsset("RelicVuMark");
        vuMark = targets.get(0);
        vuMark.setName("vuMark");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);

        this.opMode.telemetry.addData("Init Vufoira", "Targets Loaded");
        this.opMode.telemetry.update();

        /**
         * set location of the target intended
         * field coordination definition can be found in the ftc github official release page
         */
        switch (teamOrder){
            case REDLEFT:
                OpenGLMatrix vuMarkRedLeftLocation = OpenGLMatrix
                        .translation(MM_FTC_FIELD_WIDTH / 2 - 26.25f - MM_VUMARK_LENGTH / 2, -MM_FTC_FIELD_WIDTH / 2, 1.5f + MM_VUMARK_WIDTH / 2)
                        .multiplied(Orientation.getRotationMatrix(
                                AxesReference.EXTRINSIC, AxesOrder.XYZ,
                                AngleUnit.DEGREES, 90, 0, 180));
                vuMark.setLocation(vuMarkRedLeftLocation);
                RobotLog.ii(TAG, "RedLeft Target=%s", format(vuMarkRedLeftLocation));
                break;

            case REDRIGHT:
                OpenGLMatrix vuMarkRedRightLocation = OpenGLMatrix
                        .translation(-(MM_FTC_FIELD_WIDTH / 2 - 43.75f + MM_VUMARK_LENGTH), -MM_FTC_FIELD_WIDTH / 2, 1.5f + MM_VUMARK_WIDTH / 2)
                        .multiplied(Orientation.getRotationMatrix(
                                AxesReference.EXTRINSIC, AxesOrder.XYZ,
                                AngleUnit.DEGREES, 90, 0, 180));
                vuMark.setLocation(vuMarkRedRightLocation);
                RobotLog.ii(TAG, "RedRight Target=%s", format(vuMarkRedRightLocation));
                break;

            case BLUELEFT:
                OpenGLMatrix vuMarkBlueLeftLocation = OpenGLMatrix
                        .translation(-(MM_FTC_FIELD_WIDTH / 2 - 49.75f - MM_VUMARK_LENGTH), -MM_FTC_FIELD_WIDTH / 2, 1.5f + MM_VUMARK_WIDTH / 2)
                        .multiplied(Orientation.getRotationMatrix(
                                AxesReference.EXTRINSIC, AxesOrder.XYZ,
                                AngleUnit.DEGREES, -90, 0, 180));
                vuMark.setLocation(vuMarkBlueLeftLocation);
                RobotLog.ii(TAG, "BlueLeft Target=%s", format(vuMarkBlueLeftLocation));
                break;

            case BLUERIGHT:
                OpenGLMatrix vuMarkBlueRightLocation = OpenGLMatrix
                        .translation(MM_FTC_FIELD_WIDTH / 2 - 20f + MM_VUMARK_LENGTH / 2, -MM_FTC_FIELD_WIDTH / 2, 1.5f + MM_VUMARK_WIDTH / 2)
                        .multiplied(Orientation.getRotationMatrix(
                                AxesReference.EXTRINSIC, AxesOrder.XYZ,
                                AngleUnit.DEGREES, -90, 0, 180));
                vuMark.setLocation(vuMarkBlueRightLocation);
                RobotLog.ii(TAG, "BlueRight Target=%s", format(vuMarkBlueRightLocation));
                break;

            default:
                this.opMode.telemetry.addData("Warning:","Cannot locate the vuMark. Please enter the team order");
                this.opMode.telemetry.update();


        }

        /**
         * Create a transformation matrix describing where the phone is on the robot.
         *
         * The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
         * pointing to the LEFT side of the Robot.  If we consider that the camera and screen will be
         * in "Landscape Mode" the upper portion of the screen is closest to the front of the robot.
         *
         * If using the rear (High Res) camera:
         * We need to rotate the camera around it's long axis to bring the rear camera forward.
         * This requires a negative 90 degree rotation on the Y axis
         *
         * If using the Front (Low Res) camera
         * We need to rotate the camera around it's long axis to bring the FRONT camera forward.
         * This requires a Positive 90 degree rotation on the Y axis
         *
         */
        final int CAMERA_FORWARD_DISPLACEMENT  = 110;
        final int CAMERA_VERTICAL_DISPLACEMENT = 200;
        final int CAMERA_LEFT_DISPLACEMENT     = 0;

        OpenGLMatrix phoneLocation = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT,CAMERA_LEFT_DISPLACEMENT,CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC,AxesOrder.XYZ,
                        AngleUnit.DEGREES,0,0,0)); //TODO: change the orientation of the phone
        RobotLog.ii(TAG, "BlueRight Target=%s", format(phoneLocation));

        ((VuforiaTrackableDefaultListener) vuMark.getListener()).setPhoneInformation(phoneLocation,parameters.cameraDirection);
        this.opMode.telemetry.addData("Init Vufoira", "Finished");
        this.opMode.telemetry.update();
    }

    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
