package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by candy on 13/03/2018.
 */

public class MotorController {

    DcMotor leftWheel = null;
    DcMotor rightWheel = null;
    DcMotor leftSlide = null;
    DcMotor rightSlide = null;

    OpMode opMode;

    public void init(LinearOpMode opMode) {

        // Save reference to Hardware map
        this.opMode = opMode;

        // Define and Initialize Motors
        leftWheel  = this.opMode.hardwareMap.get(DcMotor.class, "leftWheel");
        rightWheel = this.opMode.hardwareMap.get(DcMotor.class, "rightWheel");
        leftSlide   = this.opMode.hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide  = this.opMode.hardwareMap.get(DcMotor.class, "rightSlide");

        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        rightWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);

        leftWheel.setPower(0);
        rightWheel.setPower(0);
        leftSlide.setPower(0);
        rightSlide.setPower(0);
    }

    public void takeBlock(){
        takeBlock(1,1);
    }
    public void stopBlock(){
        takeBlock(0,0);
    }
    public void throwBlock(){
        takeBlock(-1,-1);
    }

    public void takeBlock(double powerLeft, double powerRight){
        rightWheel.setPower(powerRight);
        leftWheel.setPower(powerLeft);
    }

    public void slideDown(){
        slideUp(-1,-1);
    }
    public void stopSlide(){
        slideUp(0,0);
    }
    public void slideUp(){
        slideUp(1,1);
    }
    public void slideUp(double powerLeft, double powerRight){
        leftSlide.setPower(powerLeft);
        rightSlide.setPower(powerRight);
    }
}
