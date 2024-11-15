package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ArmTest {
    private DcMotor armRotate;
    private DcMotor armSlider;
    private double ticksPerRotation;
    private Servo clawServo;
    private Servo wristServo;

    public void init(HardwareMap hardwareMap) {
        armRotate = hardwareMap.get(DcMotor.class, "armRotate");
        armSlider = hardwareMap.get(DcMotor.class, "armSlider");
        armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armSlider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRotate.setDirection(DcMotorSimple.Direction.REVERSE);
        ticksPerRotation = armRotate.getMotorType().getTicksPerRev();
        ticksPerRotation = armSlider.getMotorType().getTicksPerRev();
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        wristServo = hardwareMap.get(Servo.class, "rightWristServo");
        //wristServo.setDirection(Servo.Direction.REVERSE);
    }

    public void setMotorSpeed(double speed){
        armRotate.setPower(speed);
        armSlider.setPower(speed);
    }

    public void setSliderSpeed(double speed){
        armSlider.setPower(speed);
    }

    public void setRotateSpeed(double speed){
        armRotate.setPower(speed);
    }

    public double getMotorRotations() {
        return armRotate.getCurrentPosition()/ticksPerRotation;
        //return armSlider.getCurrentPosition()/ticksPerRotation;
    }

    public void setClawServoPosition(double position) {
        clawServo.setPosition(position);
    }

    public void setWristServoPosition(double position) {
        wristServo.setPosition(position);
    }

    public double getWristServoPosition() {
            return wristServo.getPosition();
    }

    public Servo.Direction getWristServoDirection() {
            return wristServo.getDirection();
    }

    /*public void toggleWristServoDirection() {
        if (wristServo.getDirection() == Servo.Direction.FORWARD) {
            wristServo.setDirection(Servo.Direction.REVERSE);
        }
        else if (wristServo.getDirection() == Servo.Direction.REVERSE) {
            wristServo.setDirection(Servo.Direction.FORWARD);
        }*/
}