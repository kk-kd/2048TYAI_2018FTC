package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.VuMarkIdentification;

/**
 * Created by candy on 08/02/2018,and.
 */
@Disabled
@Autonomous(name = "AutoBlueMid", group = "Auto")
public class AutoBlueMid extends LinearOpMode{

    OmniDrive robot;
    VuMarkIdentification vufor;

    @Override
    public void runOpMode() throws InterruptedException{

        //init
        robot                 = new OmniDrive();
        vufor                 = new VuMarkIdentification();

        robot.initDrive(this);
        vufor.initVuforia(this, robot, hardwareMap);

        waitForStart();
        vufor.activate();

        //actions


    }
}
