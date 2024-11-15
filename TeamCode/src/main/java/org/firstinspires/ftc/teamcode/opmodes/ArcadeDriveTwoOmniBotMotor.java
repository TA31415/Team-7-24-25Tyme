package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.TwoMotorDriveWithHeading;

@TeleOp()
public class ArcadeDriveTwoOmniBotMotor extends OpMode {
    TwoMotorDriveWithHeading drive = new TwoMotorDriveWithHeading();

    @Override
    public void init() {

        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;

            drive.setPowers(forward, forward);
        telemetry.addData("Left stick x", gamepad1.left_stick_x);
        /*telemetry.addData("Left stick y", gamepad1.left_stick_y);
        telemetry.addData("Right = ", right);
        telemetry.addData("forward = ", forward);
        telemetry.addData("Motor Speed =", forward + right);
        telemetry.addData("Right stick x", gamepad1.right_stick_x);
        telemetry.addData("Right stick y", gamepad1.right_stick_y);
        telemetry.addData("A button", gamepad1.a);
        telemetry.addData("B button", gamepad1.b);
        telemetry.addData("X button", gamepad1.x);
        telemetry.addData("Y button", gamepad1.y);*/
        telemetry.addData("Heading", drive.getHeading(AngleUnit.DEGREES));
    }
}