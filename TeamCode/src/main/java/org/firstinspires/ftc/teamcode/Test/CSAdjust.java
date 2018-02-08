package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

/**
 * Created by candy on 08/02/2018.
 */
@TeleOp(name = "CS Adjust", group = "Test")
public class CSAdjust extends LinearOpMode{

    OmniDrive robot ;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException{
        robot = new OmniDrive();
        hardwareMap.get(ColorSensor.class, "CS");

        waitForStart();
        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Blue", colorSensor.blue());
        telemetry.addData("Green", colorSensor.green());

        telemetry.addData("Alpha", colorSensor.alpha());
        telemetry.update();

    }
}
