package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ArmTest;

@TeleOp()
public class ArmTestOpMode extends OpMode {
    ArmTest drive = new ArmTest();
    //boolean dPadDownAlreadyPressed = false;
    //boolean toggleWristServoDirection = false;
    //boolean getWristServoDirection = false;

    @Override
    public void init() {
        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        double slider = gamepad2.left_stick_y;
        double armRotate = gamepad2.right_stick_y;
        //boolean dPadDownAlreadyPressed = false;

        drive.setSliderSpeed(slider);
        drive.setRotateSpeed(armRotate);

        if (gamepad2.b) {
            drive.setClawServoPosition(0.45);
        }
        if (gamepad2.x) {
            drive.setClawServoPosition(0.75);
        }
        if (gamepad2.a) {
            drive.setWristServoPosition(0.7);
        }
        if (gamepad2.y) {
            drive.setWristServoPosition(0.3);
        }

        /*if (gamepad2.dpad_down && !dPadDownAlreadyPressed) getWristServoDirection = !getWristServoDirection; {
            dPadDownAlreadyPressed = gamepad2.dpad_down;
            drive.toggleWristServoDirection();
        }*/

        telemetry.addData("Left stick x", gamepad2.left_stick_x);
        telemetry.addData("Left stick y", gamepad2.left_stick_y);
        telemetry.addData("Right stick x", gamepad2.right_stick_x);
        telemetry.addData("Right stick y", gamepad2.right_stick_y);
        telemetry.addData("A button", gamepad2.a);
        telemetry.addData("B button", gamepad2.b);
        telemetry.addData("X button", gamepad2.x);
        telemetry.addData("Y button", gamepad2.y);
        telemetry.addData("Dpad down", gamepad2.dpad_down);
        telemetry.addData("Left bumper", gamepad2.left_bumper);
        telemetry.addData("Right bumper", gamepad2.right_bumper);
        telemetry.addData("Wrist servo Position", drive.getWristServoPosition());
        telemetry.addData("Wrist servo Direction", drive.getWristServoDirection());
    }
}