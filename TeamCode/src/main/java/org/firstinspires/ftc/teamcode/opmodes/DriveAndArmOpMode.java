package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

import org.firstinspires.ftc.teamcode.mechanisms.DriveAndArm;

@TeleOp()
public class DriveAndArmOpMode extends OpMode {
    DriveAndArm drive = new DriveAndArm();
    boolean aAlreadyPressed = false;
    boolean robotCentric = false;
    int armPosition = 0;
    int sliderPosition = 0;
    IMU imu;

    public void init() {

        drive.init(hardwareMap);
        robotCentric = false;

        imu = hardwareMap.get(IMU.class, "imu");
        //RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,RevHubOrientationOnRobot.UsbFacingDirection.LEFT);

        //imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));

        //used for omni bot because control hub (imu) is at -45 to Y axis and + 45 to X axis
        IMU.Parameters myIMUparameters;

        myIMUparameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        new Orientation(
                                AxesReference.INTRINSIC,
                                AxesOrder.ZYX,
                                AngleUnit.DEGREES,
                                0,
                                +90,
                                0,
                                0  // acquisitionTime, not used
                        )
                )
        );

        imu.initialize(myIMUparameters);
        //drive.armRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void checkMode() {
        telemetry.addData("Robot centric", robotCentric);
        if(gamepad1.a && !aAlreadyPressed) robotCentric = !robotCentric;
        aAlreadyPressed = gamepad1.a;
    }

    private void robotCentricDrive(double forward, double right, double rotate) {
        drive.setDrive(forward, right, rotate);
    }

    private void fieldCentricDrive(double forward, double right, double rotate) {
        double robotAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        //convert to polar
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(forward, right);
        //rotate angle
        theta = AngleUnit.normalizeRadians(theta - robotAngle);

        //convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        drive.setDrive(newForward, newRight, rotate);
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = -gamepad1.left_stick_x;
        double rotate = -gamepad1.right_stick_x;
        double slider = gamepad2.left_stick_y;
        double armRotate = -gamepad2.right_stick_y;

        if (gamepad2.b) {
            drive.setClawServoPosition(0.45);
        }
        if (gamepad2.x) {
            drive.setClawServoPosition(0.83);
        }
        if (gamepad2.dpad_down) {
            drive.setRightWristServoPosition(0.83);
        }
        if (gamepad2.y) {
            drive.setRightWristServoPosition(0.3);
        }
        if (gamepad2.left_bumper) {
            drive.setRightWristServoPosition(0.43);
        }
        if (gamepad2.right_bumper) {
            drive.setRightWristServoPosition(0.7);
        }

        drive.setSliderSpeed(slider);
        drive.setRotateSpeed(armRotate);

        checkMode();
        telemetry.addData("Heading", drive.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Left stick x gp 1", gamepad1.left_stick_x);
        telemetry.addData("Left stick x gp2", gamepad2.left_stick_x);
        telemetry.addData("Left stick y gp1", gamepad1.left_stick_y);
        telemetry.addData("Left stick y gp2", gamepad2.left_stick_y);
        telemetry.addData("Robot centric", robotCentric);
        telemetry.addData("Right wrist servo Position", drive.getRightWristServoPosition());
        telemetry.addData("Left wrist servo Position", drive.getLeftWristServoPosition());
        telemetry.addData("Right wrist servo Direction", drive.getRightWristServoDirection());
        telemetry.addData("Left wrist servo Direction", drive.getLeftWristServoDirection());
        telemetry.addData("Arm position", armPosition);
        telemetry.addData("Arm rotate", drive.armRotate.getCurrentPosition());
        telemetry.addData("Slider position", sliderPosition);
        telemetry.addData("Slider", drive.armSlider.getCurrentPosition());

        if (robotCentric) {
            robotCentricDrive(forward, right, rotate);
        }
        else {
            fieldCentricDrive(forward, right, rotate);
        }
    }
}