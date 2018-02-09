package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

/**
 * Created by candy on 09/02/2018.
 */
@TeleOp(name = "IMU Test", group = "Test")
public class TestIMU extends LinearOpMode {

    OmniDrive robot;
    String angle;

    @Override
    public void runOpMode() throws InterruptedException{
        robot = new OmniDrive();

        robot.initDrive(this);
        robot.initIMU();

        waitForStart();

        while (opModeIsActive()) {
            robot.manualDrive();
            angle = robot.getAngleInDegree();
            telemetry.addData("Angle", angle);
            telemetry.update();
            sleep(3000);
        }
    }
}
