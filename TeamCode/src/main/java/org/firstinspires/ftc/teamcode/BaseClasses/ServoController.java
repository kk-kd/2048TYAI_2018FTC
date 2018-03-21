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

    ColorSensor csLeft = null;

    final double LEFT_LIFT_HOME = 0.4;
    final double RIGHT_LIFT_HOME = 0.5;
    final double LEFT_LIFT_TILT = 0.85;
    final double RIGHT_LIFT_TILT =0.05;

    //TODO: adjust para
    final double LEFT_JEW_HOME = 0.5;
    final double LEFT_JEW_TILT = 0;

    final double BLUE_THRESHOLD = 100;
    final double RED_THRESHOLD  = 100;

    OpMode opMode;


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

//        csLeft  = this.opMode.hardwareMap.get(ColorSensor.class, "csLeft");

        leftJew.setPosition(LEFT_JEW_HOME);
        leftLift.setPosition(LEFT_LIFT_HOME);
        rightLift.setPosition(RIGHT_LIFT_HOME);
    }

    public void jewDown(){
        leftJew.setPosition(LEFT_JEW_TILT);
    }

    public void jewBack(){
        leftJew.setPosition(LEFT_JEW_HOME);
    }

    public Color readColor(){
        if(csLeft.red() > RED_THRESHOLD) return Color.RED;
        else if(csLeft.blue() > BLUE_THRESHOLD) return Color.BLUE;
        return Color.NULL;
    }


    public void tiltBoard(){
        leftLift.setPosition(LEFT_LIFT_TILT);
        rightLift.setPosition(RIGHT_LIFT_TILT);
    }

    public void horizontalBoard(){
        leftLift.setPosition(LEFT_LIFT_HOME);
        rightLift.setPosition(RIGHT_LIFT_HOME);
    }

    //In this case, the color sensor should read the jew color at the front
    //If not, switch two case conditions
    public double determineRotation(Color side){
        double rotationPower = 0;
        Color jewColor = readColor();

        switch (side){
            case RED:
                if (jewColor == Color.BLUE) rotationPower = -0.5;
                else if(jewColor == Color.RED) rotationPower = 0.5;
                else rotationPower = 0;
                break;

            case BLUE:
                if (jewColor == Color.BLUE) rotationPower = 0.5;
                else if(jewColor == Color.RED) rotationPower = -0.5;
                else rotationPower = 0;
                break;
        }
        return rotationPower;

    }

}
