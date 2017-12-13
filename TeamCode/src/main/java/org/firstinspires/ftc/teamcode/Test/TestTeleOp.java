package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BaseClasses.NormalDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.VuforiaUsage;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOp", group="main")
public class TestTeleOp extends LinearOpMode{

    OmniDrive robot = new OmniDrive();
    VuforiaUsage nav = new VuforiaUsage();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initDrive(this);
        nav.initVuforia(this, robot, hardwareMap, VuforiaUsage.TeamOrder.BLUELEFT);
        waitForStart();

        nav.activate();

        while(opModeIsActive()){
            nav.startIdentifying();

        }
    }
}
