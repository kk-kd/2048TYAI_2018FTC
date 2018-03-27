package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.BaseClasses.MotorController;
import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
import org.firstinspires.ftc.teamcode.BaseClasses.ServoController;
import org.firstinspires.ftc.teamcode.BaseClasses.VuMarkIdentification;

/**
 * Created by candy on 08/02/2018,and.
 */
@Autonomous(name = "AutoRedMidEncoder_NG", group = "Auto")
public class AutoRedMidEncoder_NG extends LinearOpMode{

    OmniDrive robot;
    VuMarkIdentification vuforia;
    MotorController motorController;
    ServoController servoController;
    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;

    private double time = 0;
    private double rotationPower = 0;
    private int forwardDistance = 0;
    private int flag = 0;

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


        //vuMark
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && time <= 20){
            vuMark = vuforia.startIdentifying();
            time ++;
            sleep(200);
        }

        if(vuMark == RelicRecoveryVuMark.UNKNOWN){
            telemetry.addData("vuMark", "invisible");
            telemetry.update();
            vuMark = RelicRecoveryVuMark.CENTER;
        }

        //actions
        //Jews
        servoController.jewDown1();
        sleep(500);
        rotationPower = servoController.determineRotation(ServoController.Color.RED);
        while (flag <= 50 && rotationPower == 0.0){
            rotationPower = servoController.determineRotation(ServoController.Color.RED);
            flag++;
        }

        telemetry.addData("color", rotationPower);
        telemetry.update();

        robot.moveRobot(0,0,rotationPower);
        sleep(400);
        robot.moveRobot(0,0,0);
        servoController.jewBack();
        sleep(400);
        robot.moveRobot(0,0,0);
        sleep(400);
        robot.moveRobot(0,0,-rotationPower);
        sleep(300);
        robot.moveRobot(0,0,0);
        sleep(400);
        telemetry.addData("status","ready to move");
        telemetry.update();

        robot.moveRobot(-0.2,0,0);
        sleep(2500);
        telemetry.addData("status", "move finished");
        robot.moveRobot(0,0,0);

        //Move
        switch (vuMark){
            case LEFT:
                forwardDistance = 450;
                break;
            case CENTER:
                forwardDistance = 630;
                break;
            case RIGHT:
                forwardDistance = 880;
                break;
        }
        sleep(1000);
        robot.moveRobot(0,0.3,0);
        sleep(1000);
        robot.moveRobot(0,0,0);
        robot.encoderMoveTurn(2000,3,-0.4);
        robot.moveRobot(0,0,0);

        //navigate
        robot.moveRobot(-0.2,0,0);
        sleep(2000);



        robot.encoderMoveForward(forwardDistance,3,-0.3);//TODO
        robot.moveRobot(0,0,0);

        //Put block
//        robot.encoderMoveTurn(-1000,3,-0.4);//TODO
//        robot.moveRobot(0,0,0);
//        sleep(1000);
//        robot.moveRobot(-0.25,0,0);
//        sleep(500);
//
//        servoController.tiltBoard();
//        robot.moveRobot(0,0,0);
//        sleep(2000);
//        robot.moveRobot(0.25,0,0);
//        sleep(600);
//        robot.moveRobot(0,0,0);
//
//        servoController.horizontalBoard();
//        sleep(1000);
//
//        robot.moveRobot(-0.3,0,0);
//        sleep(800);
//        robot.moveRobot(0.25,0,0);
//        sleep(600);
//        robot.moveRobot(0,0,0);


    }
}
