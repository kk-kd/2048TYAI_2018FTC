package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


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
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // TODO: change the diameter accordingly
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                        (WHEEL_DIAMETER_INCHES * 3.1415);

    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    /* Constructor */
    public OmniDrive(){
    }

    enum Direction{
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
    public void moveRobot() {
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

        newLeftFrontTarget  = leftFront.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        newRightFrontTarget = rightFront.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        newLeftRearTarget   = leftRear.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        newRightRearTarget  = rightRear.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);

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


    public void encoderMove(double distance, Direction direction,double timeout, double speed){
        resetEncoder(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        resetEncoder(DcMotor.RunMode.RUN_USING_ENCODER);
        setPosition(distance,direction);

        runtime.reset();
        setPowerToAllMotors(Math.abs(speed));

        while (opMode.opModeIsActive() &&
                (runtime.seconds() < timeout) &&
                (leftFront.isBusy() || leftRear.isBusy() || rightFront.isBusy() || rightRear.isBusy())) {

        }

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




    public void setAxial(double axial)      {driveAxial = Range.clip(axial, -1, 1);}
    public void setLateral(double lateral)  {driveLateral = Range.clip(lateral, -1, 1); }
    public void setYaw(double yaw)          {driveYaw = Range.clip(yaw, -1, 1); }

}

