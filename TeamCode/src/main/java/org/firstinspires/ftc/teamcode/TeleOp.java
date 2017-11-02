package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOp", group="main")
public class TeleOp extends LinearOpMode{

    NormalDrive robot = new NormalDrive();
    public Servo servoLeft;
    public Servo servoRight;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initDrive(this);

        servoLeft = hardwareMap.get(Servo.class, "servoLeft");
        servoRight = hardwareMap.get(Servo.class, "servoRight");

        servoLeft.setPosition(0);
        servoRight.setPosition(0);

        waitForStart();
        telemetry.update();

        while(opModeIsActive()){
            robot.manualDrive();

            if(gamepad1.x) servoLeft.setPosition(0.5);
            if(gamepad1.y) servoRight.setPosition(0.5);
        }
    }
}
