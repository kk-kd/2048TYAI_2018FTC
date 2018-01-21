package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by candy on 20/11/2017.
 */

public class ServoController {

    private LinearOpMode opMode;

    private Servo  servoA      = null;
    private Servo  servoB      = null;
    private Servo  servoC      = null;

    /* Constructor */
    public ServoController(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(LinearOpMode opMode) {

        this.opMode = opMode;

        servoA = this.opMode.hardwareMap.get(Servo.class, "ServoA");
        servoB = this.opMode.hardwareMap.get(Servo.class, "ServoB");
        servoC = this.opMode.hardwareMap.get(Servo.class, "ServoC");

        setPosition(0);

        this.opMode.telemetry.addData(">","Press start");
        this.opMode.telemetry.update();
    }

    public void setPosition(double position){
        servoA.setPosition(position);
        servoB.setPosition(position);
        servoC.setPosition(position);
    }

    public void setServoA(double postionA) {
        servoA.setPosition(postionA);
    }
    public void setServoB(double postionB) {
        servoA.setPosition(postionB);
    }
    public void setServoC(double postionC) {
        servoA.setPosition(postionC);
    }
}
