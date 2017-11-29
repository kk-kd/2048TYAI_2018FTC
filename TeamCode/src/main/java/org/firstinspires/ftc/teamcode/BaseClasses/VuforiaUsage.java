package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
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

    private static final float MM_PER_INCH         = 25.4f;
    private static final float MM_BOT_WIDTH        = 18 * MM_PER_INCH;            // TODO: change the width to the actual width
    private static final float MM_FTC_FIELD_WIDTH  = (12*12 - 2) * MM_PER_INCH;
    private static final float MM_VUMARK_WIDTH     = 8.5f * MM_PER_INCH;
    private static final float MM_VUMARK_LENGTH    = 11 * MM_PER_INCH;

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.FRONT;


    private VuforiaTrackables targets;
    private VuforiaTrackable  vuMark;
    private OpMode            opMode;
    private OmniDrive robot;
    private HardwareMap       hardwareMap;

    public enum TeamOrder{
        REDLEFT,
        REDRIGHT,
        BLUELEFT,
        BLUERIGHT
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

    public void startTracking(){
        if (targets != null) targets.activate();
    }

    public boolean targetIsVisible(){

    }


    /**
     * call the method right after the initiation of robot
     * @param opMode
     * @param robot
     */
    public void initVuforia(OpMode opMode, OmniDrive robot, HardwareMap hardwareMap) {
        this.opMode = opMode;
        this.robot = robot;
        this.hardwareMap = hardwareMap;


        /**
         * Vuforia setup
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ac3EJmb/////AAAAGTxjSZzhKEoetuVyiWDXtrhFJd4Ds08qhs72SLU23Q71WY3VbzkivcdTtX5L/Csl8bXqXRgDqB/Bdq267iev5J+tC4ifkc1Tk/bh2pUoSZrf9lxnHd/uU6S/acBewAJ6B7y+Uut+qHI53ge8Y6123Z6RmhJvNSHafzYTTGJfjjtUFmGshscvSXmXUSKdo/hLCaAfIy6WNq4Z59BQH+KEZA3ycyXzS+7+7qdNvNOTiVqPN7PapXlPY43wi4ZaVCu3F59lEvVgHzmA/1X8SeBI7N9Ny+771nFTfNZfVmu3BMho14gY9L6xDG/7XrvBFHqouLr4fRJa/5QOJR1g5ZNecWIcB9PveWlHfphV1196BLlw";
        parameters.cameraDirection = CAMERA_CHOICE;
        parameters.useExtendedTracking = false;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        opMode.telemetry.addData("Init Vufoira", "VuforiaLocalizer Created");
        opMode.telemetry.update();


        /**
         *targets loading
         */
        targets = vuforia.loadTrackablesFromAsset("RelicVuMark");
        vuMark = targets.get(0);
        vuMark.setName("vuMark");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);

        opMode.telemetry.addData("Init Vufoira", "Targets Loaded");
        opMode.telemetry.update();
    }


    /**
     * call this method before starting to navigate
     * @param teamOrder redLeft, redRight, blueLeft, or blueRight looking from the driving station of the red/blue alliance
     */

    public void setLocation(TeamOrder teamOrder){
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
                opMode.telemetry.addData("Warning:","Cannot locate the vuMark. Please enter the team order");
                opMode.telemetry.update();


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
        opMode.telemetry.addData("Init Vufoira", "Finished");
        opMode.telemetry.update();
    }

    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
