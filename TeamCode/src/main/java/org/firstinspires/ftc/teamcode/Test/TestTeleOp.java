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
    private long   sleepTime = 600;

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

            if(gamepad1.dpad_down) servoController.horizontalBoard();
            else if(gamepad1.dpad_up) servoController.tiltBoard();

            else if (gamepad2.a) servoController.boardPosition1();
            else if (gamepad2.y) servoController.boardPosition2();
            else if (gamepad2.b) servoController.boardPosition3();

            if (gamepad1.a) {
                servoController.frontHome();
            }
            else if (gamepad1.y) servoController.frontTilt();

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

            motorController.slideUp(gamepad2.left_trigger*0.8,gamepad2.left_trigger*0.8);
            motorController.slideUp(-gamepad2.right_trigger*0.8, -gamepad2.right_trigger*0.8);

            motorController.takeBlock(gamepad2.left_stick_y, gamepad2.right_stick_y*0.9);
            if(gamepad2.left_bumper) motorController.slideUp(0.5,0.5);
            else if(gamepad2.right_bumper) motorController.slideUp(-0.5,-0.5);
            else motorController.stopSlide();

            if (gamepad2.dpad_up) motorController.slideUp(0.5,0);
            else if (gamepad2.dpad_left) motorController.slideUp(-0.5,0);
            else motorController.stopSlide();

            if (gamepad2.dpad_down) motorController.slideUp(0,-0.5);
            else if (gamepad2.dpad_right) motorController.slideUp(0,0.5);
            else motorController.stopSlide();



            telemetry.addData("degree", degree);
            telemetry.update();

        }

    }
}
