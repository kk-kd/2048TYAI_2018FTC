package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BaseClasses.AccessoriesController;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.VuMarkIdentification;
import org.firstinspires.ftc.teamcode.BaseClasses.VuforiaUsage;

/**
 * Created by candy on 08/02/2018,and.
 */
@Disabled
@Autonomous(name = "AutoBlueMid", group = "Auto")
public class AutoBlueMid extends LinearOpMode{

    OmniDrive robot;
    VuMarkIdentification vufor;
    AccessoriesController accessoriesController;

    @Override
    public void runOpMode() throws InterruptedException{

        //init
        robot                 = new OmniDrive();
        vufor                 = new VuMarkIdentification();
        accessoriesController = new AccessoriesController();

        robot.initDrive(this);
        vufor.initVuforia(this, robot, hardwareMap);
        accessoriesController.init(this);

        waitForStart();
        vufor.activate();

        //actions


    }
}
