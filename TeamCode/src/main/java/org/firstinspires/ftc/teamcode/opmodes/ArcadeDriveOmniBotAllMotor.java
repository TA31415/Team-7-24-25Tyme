package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.OmniBotAllMotorDrive;

@TeleOp()
public class ArcadeDriveOmniBotAllMotor extends OpMode {
    OmniBotAllMotorDrive drive = new OmniBotAllMotorDrive();

    @Override
    public void init() {

        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = -gamepad1.left_stick_x;
        double rotate = -gamepad1.right_stick_x;

        drive.setPowers(forward - rotate - right, forward - rotate + right, forward + rotate + right, forward + rotate - right);
        telemetry.addData("Heading", drive.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Left stick x", gamepad1.left_stick_x);
    }
}