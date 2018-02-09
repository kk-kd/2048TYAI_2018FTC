package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseClasses.VuMarkIdentification;

/**
 * Created by candy on 08/02/2018.
 */
@TeleOp(name = "Vision Test", group = "Test")
public class TestVision extends LinearOpMode {

    VuMarkIdentification vu;

    @Override
    public void runOpMode() throws InterruptedException{

        vu = new VuMarkIdentification();
        vu.initVuforia(this, null, hardwareMap);

        waitForStart();
        vu.activate();

        while (opModeIsActive()){
            vu.startIdentifying();
        }
    }
}
