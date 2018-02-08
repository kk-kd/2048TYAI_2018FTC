package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by candy on 20/11/2017.
 */

public class AccessoriesController {

    private LinearOpMode opMode;

    private Servo  servoRightBall   = null;

    private double servoRightHome = 0.6;

    /* Constructor */
    public AccessoriesController(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(LinearOpMode opMode) {
        this.opMode = opMode;

        servoRightBall = this.opMode.hardwareMap.get(Servo.class, "ServoA");

        servoRightBall.setPosition(servoRightHome);
    }

    /* Autonomous Actions */
    public void HitBall(){
        servoRightBall.setPosition(0);
    }
}
