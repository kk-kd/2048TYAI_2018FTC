package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by candy on 13/03/2018.
 */

public class ServoController {

    Servo leftLift = null;
    Servo rightLift = null;
    Servo leftJew = null;
    public Servo front = null;

    ColorSensor csLeft = null;

    final double LEFT_LIFT_HOME = 0.4;
    final double RIGHT_LIFT_HOME = 0.5;
    final double FRONT_HOME = 0.142;
    final double FRONT_TILT = 0.8;

    final double LEFT_LIFT_TILT = 0.8;

    final double LEFT_LIFT_PO1 = 0.5;
    final double LEFT_LIFT_PO2 = 0.9;
    final double LEFT_LIFT_PO3 = 0.6;

    //TODO: adjust para
    final double LEFT_JEW_HOME = 0.2;
    final double LEFT_JEW_TILT1 = 0.63;

    final double BLUE_THRESHOLD = 100;
    final double RED_THRESHOLD  = 100;

    OpMode opMode;
    private double LEFT_JEW_TILT3 = 0.75;
    private double LEFT_JEW_TILT2 = 0.8;


    public enum Color{
        RED,
        BLUE,
        NULL
    }

    public void init(LinearOpMode opMode) {

        // Save reference to Hardware map
        this.opMode = opMode;

        // Define and Initialize Motors
        leftLift  = this.opMode.hardwareMap.get(Servo.class, "leftLift");
        rightLift = this.opMode.hardwareMap.get(Servo.class, "rightLift");
        leftJew = this.opMode.hardwareMap.get(Servo.class, "leftJew");
        front = this.opMode.hardwareMap.get(Servo.class, "front");

        csLeft  = this.opMode.hardwareMap.get(ColorSensor.class, "csLeft");

        leftJew.setPosition(LEFT_JEW_HOME);
        leftLift.setPosition(LEFT_LIFT_HOME);
        rightLift.setPosition(RIGHT_LIFT_HOME);
        front.setPosition(FRONT_TILT);
    }

    public void jewDown1(){
        leftJew.setPosition(LEFT_JEW_TILT1);
    }

    public void jewDown2(){
        leftJew.setPosition(LEFT_JEW_TILT2);
    }
    public void jewDown3(){
        leftJew.setPosition(LEFT_JEW_TILT3);
    }


    public void jewBack(){
        leftJew.setPosition(LEFT_JEW_HOME);
    }

    public Color readColor(){
        if(csLeft.red() - csLeft.blue() >= 5) return Color.RED;
        else if(csLeft.blue() - csLeft.red() >=5) return Color.BLUE;
        return Color.NULL;
    }

    public void boardPosition1(){
        leftLift.setPosition(LEFT_LIFT_PO1);
    }

    public void boardPosition2(){
        leftLift.setPosition(LEFT_LIFT_PO2);
    }
    public void tiltBoard(){
        leftLift.setPosition(LEFT_LIFT_TILT);
    }

    public void horizontalBoard(){
        leftLift.setPosition(LEFT_LIFT_HOME);
    }

    public void boardPosition3(){
        leftLift.setPosition(LEFT_LIFT_PO3);
    }

    public void frontHome(){
        front.setPosition(FRONT_HOME);

    }

    public void frontTilt(){
        front.setPosition(FRONT_TILT);
    }


    //In this case, the color sensor should read the jew color at the front
    //If not, switch two case conditions
    public double determineRotation(Color side){
        double rotationPower = 0;
        Color jewColor = readColor();

        switch (side){
            case RED:
                if (jewColor == Color.BLUE) rotationPower = -0.3;
                else if(jewColor == Color.RED) rotationPower = 0.3;
                else rotationPower = 0;
                break;

            case BLUE:
                if (jewColor == Color.BLUE){
                    opMode.telemetry.addData("color","blue");
                    opMode.telemetry.update();
                    rotationPower = 0.3;
                }
                else if(jewColor == Color.RED){
                    opMode.telemetry.addData("color","red");
                    opMode.telemetry.update();
                    rotationPower = -0.3;
                }
                else rotationPower = 0;
                break;
        }
        return rotationPower;

    }

}
