package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

import org.firstinspires.ftc.teamcode.mechanisms.DriveAndArm2;

@TeleOp()
public class DriveAndArmOpMode2  extends OpMode {
    DriveAndArm2 drive = new DriveAndArm2();
    boolean gp1aAlreadyPressed = false;
    boolean gp2xAlreadyPressed = false;
    boolean gp2yAlreadyPressed = false;
    boolean gp1dPadUpAlreadyPressed = false;
    boolean gp1dPadDownAlreadyPressed = false;
    boolean gp2dPadUpAlreadyPressed = false;
    boolean gp2dPadDownAlreadyPressed = false;
    boolean clawOpen = false;
    boolean wristUp = false;
    int armPosition = 0;
    int sliderPosition = 0;
    boolean robotCentric = false;
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
        drive.armRotate.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        drive.armSlider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.armRotate.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        drive.armSlider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void checkArmPosition() {
        telemetry.addData("Arm position", armPosition);
        if(gamepad1.dpad_up && !gp1dPadUpAlreadyPressed) {
            armPosition += 1;
            //drive.armRotate.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        }
        gp1dPadUpAlreadyPressed = gamepad1.dpad_up;

        if(gamepad1.dpad_down && !gp1dPadDownAlreadyPressed) {
            armPosition -= 1;
            //drive.armRotate.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        }
        gp1dPadDownAlreadyPressed = gamepad1.dpad_down;

        if (armPosition > 4) armPosition = 4;
        if (armPosition < 0) armPosition = 0;
        if (armPosition == 0) drive.setArmPosition(drive.armPosition0);
        if (armPosition == 1) drive.setArmPosition(drive.armPosition1);
        if (armPosition == 2) drive.setArmPosition(drive.armPosition2);
        if (armPosition == 3) drive.setArmPosition(drive.armPosition3);
        if (armPosition == 4) drive.setArmPosition(drive.armPosition4);
    }

    private void checkSliderPosition() {
        telemetry.addData("Slider position", sliderPosition);
        if(gamepad2.dpad_up && !gp2dPadUpAlreadyPressed) {
            sliderPosition += 1;
            //drive.armSlider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        gp2dPadUpAlreadyPressed = gamepad2.dpad_up;

        if(gamepad2.dpad_down && !gp2dPadDownAlreadyPressed) {
            sliderPosition -= 1;
            //drive.armSlider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        gp2dPadDownAlreadyPressed = gamepad2.dpad_down;

        if (sliderPosition > 4) sliderPosition = 4;
        if (sliderPosition < 0) sliderPosition = 0;
        if (sliderPosition == 0) drive.setArmPosition(drive.sliderPosition0);
        if (sliderPosition == 1) drive.setArmPosition(drive.sliderPosition1);
        if (sliderPosition == 2) drive.setArmPosition(drive.sliderPosition2);
        if (sliderPosition == 3) drive.setArmPosition(drive.sliderPosition3);
        if (sliderPosition == 4) drive.setArmPosition(drive.sliderPosition4);
    }

    private void checkDriveMode() {
        telemetry.addData("Robot centric", robotCentric);
        if(gamepad1.a && !gp1aAlreadyPressed) robotCentric = !robotCentric;
        gp1aAlreadyPressed = gamepad1.a;
    }

    private void checkClawOpen() {
        telemetry.addData("Claw open", clawOpen);
        if(gamepad2.y && !gp2yAlreadyPressed) clawOpen = !clawOpen;
        gp2yAlreadyPressed = gamepad2.y;

        if (clawOpen) drive.setClawServoPosition(drive.clawPositionOpen);
        else drive.setClawServoPosition(drive.clawPositionClosed);
    }

    private void checkWristUp() {
        telemetry.addData("Wrist Position", wristUp);
        if(gamepad2.x && !gp2xAlreadyPressed) wristUp = !wristUp;
        gp2xAlreadyPressed = gamepad2.x;

        if (wristUp) drive.setRightWristServoPosition(drive.wristPositionMid);
        else drive.setRightWristServoPosition(drive.wristPositionDown);
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
        double liftMotor = gamepad2.left_trigger;

        /*if (gamepad2.b) {
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
        }*/

        if (slider != 0.0 && !(gp2dPadDownAlreadyPressed || gp2dPadUpAlreadyPressed)) {
            drive.armSlider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            drive.setSliderSpeed(slider);
        }

        if (armRotate != 0.0 && !(gp1dPadDownAlreadyPressed || gp1dPadUpAlreadyPressed)) {
            drive.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            drive.setRotateSpeed(armRotate);
        }

        checkDriveMode();
        checkWristUp();
        checkClawOpen();
        checkArmPosition();
        checkSliderPosition();
        telemetry.addData("Heading", drive.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Left stick x gp 1", gamepad1.left_stick_x);
        telemetry.addData("Left stick x gp2", gamepad2.left_stick_x);
        telemetry.addData("Left stick y gp1", gamepad1.left_stick_y);
        telemetry.addData("Left stick y gp2", gamepad2.left_stick_y);
        telemetry.addData("Left trigger gp2", gamepad2.left_trigger);
        telemetry.addData("Robot centric", robotCentric);
        telemetry.addData("Wrist up", wristUp);
        telemetry.addData("Claw open", clawOpen);
        telemetry.addData("Arm position", armPosition);
        telemetry.addData("Arm rotate", drive.armRotate.getCurrentPosition());
        telemetry.addData("Slider", drive.armSlider.getCurrentPosition());
        telemetry.addData("Slider position", sliderPosition);
        telemetry.addData("Right wrist servo Position", drive.getRightWristServoPosition());
        telemetry.addData("Left wrist servo Position", drive.getLeftWristServoPosition());
        telemetry.addData("Right wrist servo Direction", drive.getRightWristServoDirection());
        telemetry.addData("Left wrist servo Direction", drive.getLeftWristServoDirection());

        if (robotCentric) {
            robotCentricDrive(forward, right, rotate);
        }
        else {
            fieldCentricDrive(forward, right, rotate);
        }
    }
}