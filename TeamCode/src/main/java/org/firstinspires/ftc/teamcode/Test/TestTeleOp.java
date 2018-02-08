package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TestOp", group="main")
public class TestTeleOp extends LinearOpMode{

    OmniDrive robot = new OmniDrive();

    //rightServo Home position is 0.6

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initDrive(this);

        waitForStart();

        while(opModeIsActive()){
            //auto and manualDrive
            robot.manualDrive();

        }

    }
}
