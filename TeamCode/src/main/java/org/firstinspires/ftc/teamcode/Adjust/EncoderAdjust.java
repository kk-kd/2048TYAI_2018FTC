package org.firstinspires.ftc.teamcode.Adjust;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

/**
 * Created by candy on 23/03/2018.
 */
@TeleOp(name = "EncoderAdjust", group = "Test")

public class EncoderAdjust extends LinearOpMode{

    DcMotor motor1;
    DcMotor motor2;
    double power1 = 0;
    double power2 = 0;
    @Override
    public void runOpMode() throws InterruptedException{

        motor1 = hardwareMap.get(DcMotor.class,"motor1");
        motor2 = hardwareMap.get(DcMotor.class,"motor2");

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Init","Complete");
        telemetry.update();

        waitForStart();
        while(opModeIsActive()) {

            power1 = gamepad1.right_stick_y;
            power2 = gamepad1.left_stick_y;

            motor1.setPower(power1);
            motor2.setPower(power2);

            if (gamepad1.x){
                motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            telemetry.addData("power1", power1);
            telemetry.addData("encoder1", motor1.getCurrentPosition());
            telemetry.addData("power2", power2);
            telemetry.addData("encoder2", motor2.getCurrentPosition());
            telemetry.update();

        }

    }
}
