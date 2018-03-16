package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by candy on 13/03/2018.
 */

public class ServoController {

    Servo leftLift = null;
    Servo rightLift = null;

    final double leftLiftHome = 0;
    final double rightLiftHome = 0.5;
    final double leftLiftTilt = 0.45;
    final double rightLiftTilt =0.05;

    OpMode opMode;


    public void init(LinearOpMode opMode) {

        // Save reference to Hardware map
        this.opMode = opMode;

        // Define and Initialize Motors
        leftLift  = this.opMode.hardwareMap.get(Servo.class, "leftLift");
        rightLift = this.opMode.hardwareMap.get(Servo.class, "rightLift");

        leftLift.setPosition(leftLiftHome);
        rightLift.setPosition(rightLiftHome);
    }

    public void tiltBoard(){
        leftLift.setPosition(leftLiftTilt);
        rightLift.setPosition(rightLiftTilt);
    }

    public void horizontalBoard(){
        leftLift.setPosition(leftLiftHome);
        rightLift.setPosition(rightLiftHome);
    }
}
