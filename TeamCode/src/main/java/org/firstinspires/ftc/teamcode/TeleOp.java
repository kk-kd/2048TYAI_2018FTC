package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BaseClasses.NormalDrive;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOp", group="main")
public class TeleOp extends LinearOpMode{

    NormalDrive robot = new NormalDrive();
    public Servo servoLeft;
    public Servo servoRight;
    private final double leftServoHome = 0.4;
    private final double leftServoGrab = 0.26;
    private final double rightServoHome = 0.5;
    private final double rightServoGrab = 0.75;
    
    @Override
    public void runOpMode() throws InterruptedException {
        robot.initDrive(this);

        servoLeft = hardwareMap.get(Servo.class, "servoLeft");
        servoRight = hardwareMap.get(Servo.class, "servoRight");

        servoLeft.setPosition(leftServoHome);
        servoRight.setPosition(rightServoHome);

        waitForStart();
        telemetry.update();

        while(opModeIsActive()){
            robot.manualDrive();

            if (gamepad1.x) {
                servoLeft.setPosition(leftServoHome);
                servoRight.setPosition(rightServoHome);
            }
            else if (gamepad1.y){
                servoLeft.setPosition(leftServoGrab);
                servoRight.setPosition(rightServoGrab);
            }

            telemetry.addData("LeftServo", servoLeft.getPosition());
            telemetry.addData("RightServo", servoRight.getPosition());
            telemetry.update();

        }
    }
}
