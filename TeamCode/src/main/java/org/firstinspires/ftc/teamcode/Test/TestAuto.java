package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.VuforiaUsage;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Aut", group="main")
public class TestAuto extends LinearOpMode{

    OmniDrive robot = new OmniDrive();
    VuforiaUsage nav = new VuforiaUsage();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initDrive(this);
        nav.initVuforia(this, robot, hardwareMap, VuforiaUsage.TeamOrder.BLUELEFT);
        waitForStart();

        nav.activate();

        while(opModeIsActive()){
            telemetry.addData("1","1");
            telemetry.update();
            robot.moveRobot(0,1,0);
            sleep(3000);
            telemetry.addData("2",2);
            telemetry.update();
            robot.moveRobot(0,0,0);
            sleep(3000);

        }
    }
}
