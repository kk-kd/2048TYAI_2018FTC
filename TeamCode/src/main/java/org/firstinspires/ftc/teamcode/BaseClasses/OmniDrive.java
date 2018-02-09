package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;


public class OmniDrive{
    // Private Members
    private LinearOpMode opMode;
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor  leftFront      = null;
    private DcMotor  rightFront     = null;
    private DcMotor  leftRear       = null;
    private DcMotor  rightRear      = null;

    private double  driveAxial      = 0 ;   // Positive is forward
    private double  driveLateral    = 0 ;   // Positive is right
    private double  driveYaw        = 0 ;   // Positive is CCW

    static final double     COUNTS_PER_MOTOR_REV    = 757 ;    // MATRIX Motor Encoder; 1440 for TETRIX
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_MM       = 98 ;     // TODO: change the diameter accordingly
    static final double     COUNTS_PER_MM           = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                        (WHEEL_DIAMETER_MM * 3.1415);

    BNO055IMU imu;
    Orientation angles;

    /* Constructor */
    public OmniDrive(){
    }

    public enum Direction{
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    /* Initialize standard Hardware interfaces */
    public void initDrive(LinearOpMode opMode) {

        // Save reference to Hardware map
        this.opMode = opMode;

        // Define and Initialize Motors
        leftFront  = this.opMode.hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = this.opMode.hardwareMap.get(DcMotor.class, "rightFront");
        leftRear   = this.opMode.hardwareMap.get(DcMotor.class, "leftRear");
        rightRear  = this.opMode.hardwareMap.get(DcMotor.class, "rightRear");

        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        // Stop all robot motion by setting each axis value to zero
        moveRobot(0,0,0) ;

        this.opMode.telemetry.addData(">","Press start");
        this.opMode.telemetry.update();
    }

    public void manualDrive()  {
        // In this mode the Left stick moves the robot fwd & back, and Right & Left.
        // The Right stick rotates CCW and CW.

        setAxial(-opMode.gamepad1.left_stick_y);
        setLateral(opMode.gamepad1.left_stick_x);
        setYaw(-opMode.gamepad1.right_stick_x);
        moveRobot();
    }


    /***
     * void moveRobot(double axial, double lateral, double yaw)
     * Set speed levels to motors based on axes requests
     * @param axial     Speed in Fwd Direction
     * @param lateral   Speed in lateral direction (+ve to right)
     * @param yaw       Speed of Yaw rotation.  (+ve is CCW)
     */
    public void moveRobot(double axial, double lateral, double yaw) {
        setAxial(axial);
        setLateral(lateral);
        setYaw(yaw);
        moveRobot();
    }

    /***
     * void moveRobot()
     * This method will calculate the motor speeds required to move the robot according to the
     * speeds that are stored in the three Axis variables: driveAxial, driveLateral, driveYaw.
     * This code is setup for a three wheeled OMNI-drive but it could be modified for any sort of omni drive.
     *
     * The code assumes the following conventions.
     * 1) Positive speed on the Axial axis means move FORWARD.
     * 2) Positive speed on the Lateral axis means move RIGHT.
     * 3) Positive speed on the Yaw axis means rotate COUNTER CLOCKWISE.
     *
     * This convention should NOT be changed.  Any new drive system should be configured to react accordingly.
     */
    private void moveRobot() {
        // calculate required motor speeds to achieve axis motions
        double vLeftRear   = driveAxial - driveLateral - driveYaw;
        double vLeftFront  = driveAxial + driveLateral - driveYaw;
        double vRightRear  = driveAxial + driveLateral + driveYaw;
        double vRightFront = driveAxial - driveLateral + driveYaw;

        // normalize all motor speeds so no values exceeds 100%.
        double max = Math.max(Math.max(Math.abs(vLeftFront), Math.abs(vLeftRear)), Math.max(Math.abs(vRightFront), Math.abs(vRightRear)));
        if (max > 1.0)
        {
            vLeftFront  /= max;
            vLeftRear   /= max;
            vRightFront /= max;
            vRightRear  /= max;
        }

        // Set drive motor power levels.
        leftRear.setPower(vLeftRear);
        leftFront.setPower(vLeftFront);
        rightFront.setPower(vRightFront);
        rightRear.setPower(vRightRear);

        // Display Telemetry
        opMode.telemetry.addData("Axes  ", "A[%+5.2f], L[%+5.2f], Y[%+5.2f]", driveAxial, driveLateral, driveYaw);
        opMode.telemetry.addData("Wheels", "LeftFront[%+5.2f], LeftRear[%+5.2f], RightFront[%+5.2f], RightRear[%+5.2f]", vLeftFront, vLeftRear, vRightFront, vRightRear);
        opMode.telemetry.update();
    }


    /***
     * Encoder
     *
     * The following methods are designed to provide encoder run in all directions
     */

    private void resetEncoder(DcMotor.RunMode runMode){
        leftFront.setMode(runMode);
        leftRear.setMode(runMode);
        rightFront.setMode(runMode);
        rightRear.setMode(runMode);
    }

    private void setPowerToAllMotors(double power){
        leftFront. setPower(power);
        leftRear.  setPower(power);
        rightFront.setPower(power);
        rightRear. setPower(power);
    }

    private void setPosition(double distance, Direction direction){

        int newLeftFrontTarget, newRightFrontTarget,newLeftRearTarget,newRightRearTarget;

        newLeftFrontTarget  = leftFront.getCurrentPosition() + (int)(distance * COUNTS_PER_MM);
        newRightFrontTarget = rightFront.getCurrentPosition() + (int)(distance * COUNTS_PER_MM);
        newLeftRearTarget   = leftRear.getCurrentPosition() + (int)(distance * COUNTS_PER_MM);
        newRightRearTarget  = rightRear.getCurrentPosition() + (int)(distance * COUNTS_PER_MM);

        switch (direction){
            case BACKWARD:
                newLeftFrontTarget  = -newLeftFrontTarget;
                newRightFrontTarget = -newRightFrontTarget;
                newLeftRearTarget   = -newLeftRearTarget;
                newRightRearTarget  = -newRightRearTarget;
                break;
            case LEFT:
                newLeftFrontTarget  = -newLeftFrontTarget;
                newRightRearTarget  = -newRightRearTarget;
                break;
            case RIGHT:
                newRightFrontTarget = -newRightFrontTarget;
                newLeftRearTarget   = -newLeftRearTarget;
                break;
            default:
                break;
        }

        leftFront.setTargetPosition(newLeftFrontTarget);
        leftRear.setTargetPosition(newLeftRearTarget);
        rightFront.setTargetPosition(newRightFrontTarget);
        rightRear.setTargetPosition(newRightRearTarget);

        resetEncoder(DcMotor.RunMode.RUN_TO_POSITION);

    }


    public void encoderMove(double distance, Direction direction,double timeoutInSec, double speed){
        int flag = 0;

        resetEncoder(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        resetEncoder(DcMotor.RunMode.RUN_USING_ENCODER);

        opMode.telemetry.addData("Init","Complete");
        opMode.telemetry.update();

        setPosition(distance,direction);

        runtime.reset();
        setPowerToAllMotors(Math.abs(speed));
        opMode.telemetry.addData("Power", "Set");
        opMode.telemetry.update();

        while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeoutInSec) &&
                (leftFront.isBusy() && leftRear.isBusy() && rightFront.isBusy() && rightRear.isBusy())) {
            flag ++;
        }
        opMode.telemetry.addData("flag", flag);
        opMode.telemetry.update();

        setPowerToAllMotors(0);
        resetEncoder(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    /***
     * Sensor BNO055IMU implementation
     *
     * BNO055 is a 9-axis Gyro in REV Expansion Cub
     * BNO055 usage should have a higher precision than pure encoder drive, and will not be affected by "condition" in Beijing
     * Encoder should be merely a backup for this implementation
     */

    public void initIMU(){

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        //imu in REV is on I2C 0 port
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public String getAngleInDegree() {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        new Runnable() {
            @Override
            public void run() {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            }
        };
        return formatAngle(angles.angleUnit, angles.firstAngle);
    }

    private String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    private String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    public void setAxial(double axial)      {driveAxial = Range.clip(axial, -1, 1);}
    public void setLateral(double lateral)  {driveLateral = Range.clip(lateral, -1, 1); }
    public void setYaw(double yaw)          {driveYaw = Range.clip(yaw, -1, 1); }

}

