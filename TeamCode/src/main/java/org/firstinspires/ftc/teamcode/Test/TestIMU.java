package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseClasses.IMUImp;

/**
 * Created by candy on 09/02/2018.
 */
@TeleOp(name = "IMU Test", group = "Test")
public class TestIMU extends LinearOpMode {

    IMUImp imu;
    String heading, roll, pitch;

    @Override
    public void runOpMode() throws InterruptedException{
        imu = new IMUImp();

        imu.initIMU(this);

        waitForStart();

        while (opModeIsActive()) {
            heading = imu.getHeadingInDegree();
            roll    = imu.getRollInDegree();
            pitch   = imu.getPitchInDegree();

            telemetry.addData("Heading", heading);
            telemetry.addData("Roll", roll);
            telemetry.addData("Pitch", pitch);
            telemetry.update();
            sleep(3000);
        }
    }
}
