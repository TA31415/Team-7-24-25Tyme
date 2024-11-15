package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.mechanisms.FieldCentricOmniBot;

@TeleOp()
public class ArcadeDriveFieldCentricOmniBot extends OpMode {
    FieldCentricOmniBot drive = new FieldCentricOmniBot();
    boolean aAlreadyPressed = false;
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
        double rotate = gamepad1.right_stick_x;
        checkMode();
        //drive.setPowers(forward - rotate - right, forward - rotate + right, forward + rotate + right, forward + rotate - right);
        //drive.setDrive(forward, right, rotate);
        telemetry.addData("Heading", drive.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Left stick x", gamepad1.left_stick_x);
        telemetry.addData("Robot centric", robotCentric);

        if (robotCentric) {
            robotCentricDrive(forward, right, rotate);
        }
        else {
            fieldCentricDrive(forward, right, rotate);
        }
    }
}