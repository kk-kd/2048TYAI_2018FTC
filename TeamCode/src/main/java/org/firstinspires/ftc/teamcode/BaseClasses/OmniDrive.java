package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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

        // Stop all robot motion by setting each axis value to zero
        moveRobot(0,0,0) ;

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        this.opMode.telemetry.addData(">","Press start");
        this.opMode.telemetry.update();
    }

    //PHONE SHOULD BE SET ON THE LEFT SIDE
    public void manualDrive(int degree)  {
        // In this mode the Left stick moves the robot fwd & back, and Right & Left.
        // The Right stick rotates CCW and CW.

        float axialOne = opMode.gamepad1.left_stick_y;
        float axialTwo = opMode.gamepad1.left_stick_x;
        float axialThree = opMode.gamepad1.left_trigger - opMode.gamepad1.right_trigger;

        switch (degree){
            case 0:
                setAxial(-axialOne);
                setLateral(axialTwo);
                setYaw(axialThree);
                break;
            case 90:
                setAxial(axialTwo);
                setLateral(-axialOne);
                setYaw(axialThree);
                break;
            case 180:
                setAxial(axialOne);
                setLateral(-axialTwo);
                setYaw(-axialThree);
                break;
            case 270:
                setAxial(-axialTwo);
                setLateral(axialOne);
                setYaw(axialThree);
                break;

        }

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


    public void setAxial(double axial)      {driveAxial = Range.clip(axial, -1, 1);}
    public void setLateral(double lateral)  {driveLateral = Range.clip(lateral, -1, 1); }
    public void setYaw(double yaw)          {driveYaw = Range.clip(yaw, -1, 1); }

}

