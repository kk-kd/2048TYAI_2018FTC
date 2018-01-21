package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BaseClasses.NormalDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.ServoController;
import org.firstinspires.ftc.teamcode.BaseClasses.VuforiaUsage;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOp", group="main")
public class TestTeleOp extends LinearOpMode{

    ServoController servoController = new ServoController();
    double positionA = 0;
    double positionB = 0;
    double positionC = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        servoController.init(this);
        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.a){
                positionA += 0.1;
                servoController.setServoA(positionA);
            }else if(gamepad1.b){
                positionA -= 0.1;
                servoController.setServoA(positionA);
            }else if(gamepad1.x){
                positionB += 0.1;
                servoController.setServoB(positionB);
            }else if(gamepad1.y){
                positionB -= 0.1;
                servoController.setServoB(positionB);
            }else if(gamepad1.left_bumper){
                positionC += 0.1;
                servoController.setServoC(positionC);
            }else if(gamepad2.right_bumper){
                positionC -= 0.1;
                servoController.setServoC(positionC);
            }


            telemetry.addData("servoA Position: ", positionA);
            telemetry.addData("servoB Position: ", positionB);
            telemetry.addData("servoC Position: ", positionC);

            telemetry.update();

        }
    }
}
