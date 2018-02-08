package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

/**
 * Created by candy on 27/09/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Aut", group="main")
public class TestAuto extends LinearOpMode{

    OmniDrive robot = new OmniDrive();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initDrive(this);
        waitForStart();

        robot.encoderMove(500, OmniDrive.Direction.FORWARD, 5, 0.8);
        robot.encoderMove(500, OmniDrive.Direction.BACKWARD, 5, 0.5);

            /**
            robot.moveRobot(1,0,0);
            sleep(3000);
            robot.moveRobot(-1,0,0);
            sleep(3000);
            robot.moveRobot(0,1,0);
            sleep(3000);
            robot.moveRobot(0,-1,0);
            sleep(3000);
            robot.moveRobot(0,0,1);
            sleep(3000);
            robot.moveRobot(0,0,-1);
            sleep(3000);
            **/
    }
}
