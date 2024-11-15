package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DriveAndArm2 {
    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;
    private IMU imu;
    public DcMotorEx armRotate;
    public DcMotor armSlider;
    public DcMotor liftMotor;
    private Servo clawServo;
    private Servo rightWristServo;
    private Servo leftWristServo;
    private double ticksPerRotation1;
    private double ticksPerRotation2;
    private double ticksPerRotation3;
    public double clawPositionOpen = 0.83;
    public double clawPositionClosed = 0.45;
    public double wristPositionMid = 0.5;
    public double wristPositionDown = 1;
    public int armPosition0 = 0;
    public int armPosition1 = -625;
    public int armPosition2 = -1250;
    public int armPosition3 = -1875;
    public int armPosition4 = -2500;
    public int sliderPosition0 = 0;
    public int sliderPosition1 = 500;
    public int sliderPosition2 = 1000;
    public int sliderPosition3 = 1500;
    public int sliderPosition4 = 2000;
    public int armVel = -100;
    public int slideVel = -100;

    public void init(HardwareMap hardwareMap) {
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");
        imu = hardwareMap.get(IMU.class, "imu");
        armRotate = hardwareMap.get(DcMotorEx.class, "armRotate");
        armSlider = hardwareMap.get(DcMotor.class, "armSlider");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        rightWristServo = hardwareMap.get(Servo.class, "rightWristServo");
        leftWristServo = hardwareMap.get(Servo.class, "leftWristServo");


        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armSlider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armSlider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRotate.setDirection(DcMotorSimple.Direction.REVERSE);

        ticksPerRotation1 = armRotate.getMotorType().getTicksPerRev();
        ticksPerRotation2 = armSlider.getMotorType().getTicksPerRev();
        ticksPerRotation3 = liftMotor.getMotorType().getTicksPerRev();

        RevHubOrientationOnRobot RevOrientation =
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        imu.initialize(new IMU.Parameters(RevOrientation));
    }

    public void setArmPosition(int armPosition){
        armRotate.setTargetPosition(armPosition);
        armRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRotate.setVelocity(armVel);
    }

    public double getHeading(AngleUnit angleUnit) {
        return imu.getRobotYawPitchRollAngles().getYaw(angleUnit);

    }

    public void setPowers(double leftFrontPower, double leftBackPower, double rightFrontPower, double rightBackPower) {
        double largest = 1;
        largest = Math.max(largest, Math.abs(leftFrontPower));
        largest = Math.max(largest, Math.abs(leftBackPower));
        largest = Math.max(largest, Math.abs(rightFrontPower));
        largest = Math.max(largest, Math.abs(rightBackPower));

        leftFrontDrive.setPower(leftFrontPower / largest);
        leftBackDrive.setPower(leftBackPower / largest);
        rightFrontDrive.setPower(rightFrontPower / largest);
        rightBackDrive.setPower(rightBackPower / largest);
    }

    public void setDrive(double forward, double right, double rotate) {
        double frontLeftPower = forward - rotate - right; //forward + right + rotate;
        double backLeftPower = forward - rotate + right; //forward - right + rotate;
        double frontRightPower = forward + rotate + right; //forward - right - rotate;
        double backRightPower = forward + rotate - right; //forward + right - rotate;

        setPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

    public void setMotorSpeed(double speed) {
        armRotate.setPower(speed);
        armSlider.setPower(speed);
    }

    public void setSliderSpeed(double speed) {
        double armSliderSpeedMax = 0.5;
        armSlider.setPower(speed * armSliderSpeedMax);
    }

    public void setRotateSpeed(double speed) {
        double armRotateSpeedMax = 0.5;
        armRotate.setPower(speed * armRotateSpeedMax);
    }

    public double getMotorRotations() {
        return armRotate.getCurrentPosition() / ticksPerRotation1;
    }
    public void setClawServoPosition(double position) {
        clawServo.setPosition(position);
    }

    public void setRightWristServoPosition(double position) {
        rightWristServo.setPosition(position);
    }

    public void setLeftWristServoPosition(double position) {
        leftWristServo.setPosition(position);
    }

    public double getRightWristServoPosition() {
        return rightWristServo.getPosition();
    }

    public double getLeftWristServoPosition() { return leftWristServo.getPosition(); }

    public Servo.Direction getRightWristServoDirection() {
        return rightWristServo.getDirection();
    }

    public Servo.Direction getLeftWristServoDirection() {
        return leftWristServo.getDirection();
    }
}
