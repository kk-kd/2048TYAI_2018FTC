package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.BaseClasses.MotorController;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.ServoController;
import org.firstinspires.ftc.teamcode.BaseClasses.VuMarkIdentification;

/**
 * Created by candy on 08/02/2018,and.
 */
@Disabled
@Autonomous(name = "AutoBlueMid", group = "Auto")
public class AutoBlueMid extends LinearOpMode{

    OmniDrive robot;
    VuMarkIdentification vuforia;
    MotorController motorController;
    ServoController servoController;
    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;

    private double time = 0;
    private double rotationPower = 0;
    private int forwardDistance = 0;
    @Override
    public void runOpMode() throws InterruptedException{
        //init
        robot = new OmniDrive();
        vuforia = new VuMarkIdentification();
        motorController = new MotorController();
        servoController = new ServoController();

        servoController.init(this);
        motorController.init(this);
        robot.initDrive(this);
        vuforia.initVuforia(this, robot, hardwareMap);

        waitForStart();
        vuforia.activate();

        //actions
        //Jews
        rotationPower = servoController.determineRotation(ServoController.Color.BLUE);
        servoController.jewDown();
        robot.moveRobot(0,0,rotationPower);
        sleep(1000);
        servoController.jewBack();
        robot.moveRobot(0,0,-rotationPower);
        sleep(1000);

        //vuMark
        robot.moveRobot(-0.5,0,0);
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && time <= 10){
            vuMark = vuforia.startIdentifying();
            time ++;
        }
        robot.moveRobot(0,0,0);
        if(vuMark == RelicRecoveryVuMark.UNKNOWN){
            telemetry.addData("vuMark", "invisible");
            vuMark = RelicRecoveryVuMark.CENTER;
        }
        robot.moveRobot(1,0,0);
        sleep(2000);
        robot.moveRobot(-0.8,0,0);
        sleep(300);

        //Move
        switch (vuMark){
            case LEFT:
                forwardDistance = 2000;
                break;
            case CENTER:
                forwardDistance = 3000;
                break;
            case RIGHT:
                forwardDistance = 4000;
                break;
        }
        robot.encoderMove(forwardDistance, OmniDrive.Direction.FORWARD,5000,0.8);

        //Put block
    }
}
