package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by candy on 16/10/2017.
 */
@Disabled
@TeleOp(name = "SingleMotor", group = "Test")
public class TeleOpSingleMotor extends LinearOpMode {

    private DcMotor motor;
    HardwareMap hardwareMap = null;

    @Override
    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.get(DcMotor.class,"motor");

        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status","Ready to start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){

            double power = -gamepad1.left_stick_y;
            motor.setPower(power);

            telemetry.addData("Power",power);
            telemetry.update();

            sleep(40);
        }
    }
}
