package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;


public class NormalDrive{
    // Private Members
    private LinearOpMode opMode;

    private DcMotor  leftFront      = null;
    private DcMotor  rightFront     = null;
    private DcMotor  leftRear       = null;
    private DcMotor  rightRear      = null;

    private double  driveAxial      = 0 ;   // Positive is forward
    private double  driveLateral    = 0 ;   // Positive is right

    /* Constructor */
    public NormalDrive(){
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

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

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
        double vLeftRear   = driveAxial + driveLateral;
        double vLeftFront  = driveAxial + driveLateral;
        double vRightRear  = driveAxial - driveLateral;
        double vRightFront = driveAxial - driveLateral;

        // normalize all motor speeds so no alues exceeds 100%.
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
        opMode.telemetry.addData("Axes  ", "A[%+5.2f], L[%+5.2f]", driveAxial, driveLateral);
        opMode.telemetry.addData("Wheels", "LeftFront[%+5.2f], LeftRear[%+5.2f], RightFront[%+5.2f], RightRear[%+5.2f]", vLeftFront, vLeftRear, vRightFront, vRightRear);
        opMode.telemetry.update();
    }


    public void setAxial(double axial)      {driveAxial = Range.clip(axial, -1, 1);}
    public void setLateral(double lateral)  {driveLateral = Range.clip(lateral, -1, 1); }
}


