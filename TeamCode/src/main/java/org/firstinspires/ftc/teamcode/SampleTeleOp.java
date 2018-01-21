package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BaseClasses.NormalDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.VuforiaUsage;

/**
 * Created by candy on 27/09/2017.
 */
@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOp", group="main")
public class SampleTeleOp extends LinearOpMode{

    OmniDrive robot = new OmniDrive();
    VuforiaUsage nav = new VuforiaUsage();

    @Override
    public void runOpMode() throws InterruptedException {
        //base
        robot.initDrive(this);

        //rev identification adn recognition
        nav.initVuforia(this, robot, hardwareMap, VuforiaUsage.TeamOrder.BLUELEFT);

        waitForStart();

        nav.activate();

        while(opModeIsActive()){
            //vuMark identification
            nav.startIdentifying();

            //auto and manualDrive
            robot.moveRobot(1,2,3);
            robot.manualDrive();

            //encoder drive
            robot.encoderMove(1, OmniDrive.Direction.FORWARD,5,0.5);
        }
    }
}
