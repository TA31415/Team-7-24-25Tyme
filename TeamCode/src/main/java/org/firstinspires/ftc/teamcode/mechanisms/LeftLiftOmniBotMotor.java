package org.firstinspires.ftc.teamcode.mechanisms;

// import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LeftLiftOmniBotMotor {
    private DcMotor motor;
    private double ticksPerRotation;

    public void init(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "towerMotor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        ticksPerRotation = motor.getMotorType().getTicksPerRev();
    }

    public void setMotorSpeed(double speed){
        motor.setPower(speed);
       // telemetry.addData("Motor Speed =", speed);
    }

    public double getMotorRotations(){
        return motor.getCurrentPosition()/ticksPerRotation;
    }
//    public void setPowers(double v, double v1) {
//
//    }
}
