package org.firstinspires.ftc.teamcode.Adjust;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by candy on 08/02/2018.
 */
@TeleOp(name = "Servo Adjust",group = "Test")
public class ServoAdjust extends LinearOpMode{

    Servo servoTest;
    double position = 0;

    @Override
    public void runOpMode() throws InterruptedException{
        servoTest = hardwareMap.get(Servo.class, "servoTest");
        servoTest.setPosition(0);

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.x) position += 0.05;
            else if (gamepad1.b) position -= 0.05;

            servoTest.setPosition(position);

            telemetry.addData("servoTest", position);
            telemetry.update();
        }
    }
}
