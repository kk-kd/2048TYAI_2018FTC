//package org.firstinspires.ftc.teamcode.Auto;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.BaseClasses.MotorController;
//import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;
//import org.firstinspires.ftc.teamcode.BaseClasses.ServoController;
//import org.firstinspires.ftc.teamcode.BaseClasses.VuMarkIdentification;
//
///**
// * Created by candy on 08/02/2018,and.
// */
//@Disabled
//@Autonomous(name = "AutoRedMid", group = "Auto")
//public class AutoRedMid extends LinearOpMode{
//
//    OmniDrive robot;
//    VuMarkIdentification vuforia;
//    MotorController motorController;
//    ServoController servoController;
//
//    OmniDrive.Direction
//
//    @Override
//    public void runOpMode() throws InterruptedException{
//
//        //init
//        robot = new OmniDrive();
//        vuforia = new VuMarkIdentification();
//        motorController = new MotorController();
//        servoController = new ServoController();
//
//        servoController.init(this);
//        motorController.init(this);
//        robot.initDrive(this);
//        vuforia.initVuforia(this, robot, hardwareMap);
//
//        waitForStart();
//        vuforia.activate();
//
//        //actions
//
//
//    }
//}
