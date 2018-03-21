package org.firstinspires.ftc.teamcode.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by candy on 08/02/2018.
 */

public class VuMarkIdentification {

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.FRONT;

    private VuforiaTrackables targets;
    private VuforiaTrackable  vuMark;
    private OpMode            opMode;
    private OmniDrive         robot;
    private HardwareMap       hardwareMap;

    public void initVuforia(OpMode opMode, OmniDrive robot, HardwareMap hardwareMap) {
        this.opMode = opMode;
        this.robot = robot;
        this.hardwareMap = hardwareMap;

        /**
         * Vuforia setup
         */
        int cameraMonitorViewId = this.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ac3EJmb/////AAAAGTxjSZzhKEoetuVyiWDXtrhFJd4Ds08qhs72SLU23Q71WY3VbzkivcdTtX5L/Csl8bXqXRgDqB/Bdq267iev5J+tC4ifkc1Tk/bh2pUoSZrf9lxnHd/uU6S/acBewAJ6B7y+Uut+qHI53ge8Y6123Z6RmhJvNSHafzYTTGJfjjtUFmGshscvSXmXUSKdo/hLCaAfIy6WNq4Z59BQH+KEZA3ycyXzS+7+7qdNvNOTiVqPN7PapXlPY43wi4ZaVCu3F59lEvVgHzmA/1X8SeBI7N9Ny+771nFTfNZfVmu3BMho14gY9L6xDG/7XrvBFHqouLr4fRJa/5QOJR1g5ZNecWIcB9PveWlHfphV1196BLlw";
        parameters.cameraDirection = CAMERA_CHOICE;
        parameters.useExtendedTracking = false;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        this.opMode.telemetry.addData("Init Vufoira", "VuforiaLocalizer Created");
        this.opMode.telemetry.update();


        /**
         *targets loading
         */
        targets = vuforia.loadTrackablesFromAsset("RelicVuMark");
        vuMark = targets.get(0);
        vuMark.setName("vuMark");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);

        this.opMode.telemetry.addData("Init Vufoira", "Targets Loaded");
        this.opMode.telemetry.update();
    }

    public RelicRecoveryVuMark startIdentifying(){

        RelicRecoveryVuMark mark = RelicRecoveryVuMark.from(vuMark);

        if (mark != RelicRecoveryVuMark.UNKNOWN ){
            opMode.telemetry.addData("vuMark visible", mark);
            opMode.telemetry.update();
            return mark;
        }

        return RelicRecoveryVuMark.UNKNOWN;
    }

    /**
     * Called after waitForStart()
     */
    public void activate(){

        if (targets != null)
            targets.activate();
    }
}
