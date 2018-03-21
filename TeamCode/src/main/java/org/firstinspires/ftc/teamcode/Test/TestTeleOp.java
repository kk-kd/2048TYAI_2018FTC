package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BaseClasses.MotorController;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.ServoController;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TestOp", group="main")
public class TestTeleOp extends LinearOpMode{

    private int degree = 0;
    private double yawPower = 1;
    private long   sleepTime = 2000;

    OmniDrive robot;
    MotorController motorController;
    ServoController servoController;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new OmniDrive();
        motorController = new MotorController();
        servoController = new ServoController();

        servoController.init(this);
        motorController.init(this);
        robot.initDrive(this);

        waitForStart();

        while(opModeIsActive()){
            //auto and manualDrive
            robot.manualDrive(degree);

            if (gamepad1.right_bumper) motorController.takeBlock();
            else if (gamepad1.left_bumper) motorController.throwBlock();
            else motorController.stopBlock();

            if (gamepad1.y) motorController.slideUp();
            else if (gamepad1.a) motorController.slideDown();
            else motorController.stopSlide();

            if(gamepad1.dpad_down) servoController.horizontalBoard();
            else if(gamepad1.dpad_up) servoController.tiltBoard();
            else if(gamepad2.a) servoController.horizontalBoard();
            else if(gamepad2.y) servoController.tiltBoard();


            if (gamepad1.x){
                robot.moveRobot(0,0,yawPower);
                sleep(sleepTime);
                robot.moveRobot(0,0,0);
                degree += 90;
            }else if (gamepad1.b){
                robot.moveRobot(0,0,-yawPower);
                sleep(sleepTime);
                degree -= 90;
            }

            if(degree > 270) degree -=360;
            else if (degree < 0) degree += 360;


            motorController.takeBlock(gamepad2.left_stick_y, gamepad2.right_stick_y);
            if(gamepad2.left_bumper) motorController.slideUp(0.5,0.5);

            telemetry.addData("degree", degree);
            telemetry.update();

        }

    }
}
