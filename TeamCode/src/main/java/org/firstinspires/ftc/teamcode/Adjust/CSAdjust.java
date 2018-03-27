package org.firstinspires.ftc.teamcode.Adjust;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.BaseClasses.OmniDrive;

/**
 * Created by candy on 08/02/2018.
 */
@TeleOp(name = "CSAdjust", group = "Test")
public class CSAdjust extends LinearOpMode{

    OmniDrive robot ;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException{
        robot = new OmniDrive();
        colorSensor = hardwareMap.get(ColorSensor.class, "csLeft");

        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final double SCALE_FACTOR = 255;

        waitForStart();
        while(opModeIsActive()) {
            Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
                    (int) (colorSensor.green() * SCALE_FACTOR),
                    (int) (colorSensor.blue() * SCALE_FACTOR),
                    hsvValues);


            telemetry.addData("Red", colorSensor.red());
            telemetry.addData("Blue", colorSensor.blue());
            telemetry.addData("Green", colorSensor.green());

            telemetry.addData("Alpha", colorSensor.alpha());
            telemetry.addData("Hue", hsvValues[0]);

            telemetry.update();
        }

    }
}
