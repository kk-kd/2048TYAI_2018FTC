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
@Autonomous(name = "AutoRedCornerEncoder", group = "Auto")
public class AutoRedCornerEncoder extends LinearOpMode{

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
        sleep(1500);
        rotationPower = servoController.determineRotation(ServoController.Color.RED);
        while (flag <= 50 && rotationPower == 0.0){                                         //TODO
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
        robot.moveRobot(0,0,-rotationPower);
        sleep(300);
        robot.moveRobot(0,0,0);
        sleep(400);
        telemetry.addData("status","ready to move");
        telemetry.update();

        robot.moveRobot(-0.2,0,0);
        sleep(2000);
        telemetry.addData("status", "move finished");
        robot.moveRobot(0,0,0);

        robot.moveRobot(-0.3,0,0);
        sleep(2000);

        //Move
        switch (vuMark){
            case LEFT:
                forwardDistance = 550;
                break;
            case CENTER:
                forwardDistance = 300;
                break;
            case RIGHT:
                forwardDistance = 100;
                break;
        }
        sleep(1000);
        robot.moveRobot(0.2,0,0);
        sleep(800);
        //Put block
        robot.encoderMoveTurn(1300,4,0.4);
        sleep(1000);
        robot.encoderMoveForward(forwardDistance,3,-0.3);
        sleep(1000);
        robot.encoderMoveTurn(1000,4,0.4);

        robot.moveRobot(-0.2,0,0);
        sleep(500);
        robot.moveRobot(0,0,0);

        servoController.tiltBoard();
        sleep(1500);
        servoController.horizontalBoard();
        sleep(1000);
        robot.moveRobot(-0.25,0,0);
        sleep(500);
        robot.moveRobot(-0.25,0,0);
        sleep(100);

        robot.moveRobot(0.3,0,0);
        sleep(300);
        robot.moveRobot(-0.3,0,0);
        sleep(600);
        robot.moveRobot(0,0,0);


    }
}
