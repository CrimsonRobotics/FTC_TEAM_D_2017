package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeleopMode", group = "Teleop")
//@Disabled

public class TeleopMode extends OpMode {

    DcMotor leftMotor;// Initialize variables to represent motors
    DcMotor rightMotor;
    DcMotor liftMotor;//

    Servo rightServo;// Initialize variables to represent servos
    Servo leftServo;

    // Initialize variables

    float leftPower = 0;
    float rightPower = 0;
    float liftPower = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("left_drive");// Get references to the motors from the hardware map
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");

        rightServo = hardwareMap.servo.get("rightServo"); // Get references to the servos from the hardware map
        leftServo = hardwareMap.servo.get("leftServo");

        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);// Reset the encoder for the winch

        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Reverse the right motor

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        // Tank drive
        if (gamepad1.right_stick_y != 0){ // Allow triggers to determine the power of the drive motors with gamepad1 overriding gamepad2
            leftPower = -gamepad1.right_stick_y;
        }else{
            leftPower = -gamepad2.right_stick_y;
        }
        if (gamepad1.left_stick_y != 0){
            rightPower = -gamepad1.left_stick_y;
        }else{
            rightPower = -gamepad2.left_stick_y;
        }

        /*if (gamepad1.right_stick_y != 0){// Arcade drive for gamepad 1
            if (gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_x < -0.1){
                leftPower = -gamepad1.right_stick_y;
                rightPower = -gamepad1.right_stick_y;
            }else if (gamepad1.right_stick_x < -0.1){
                leftPower = -gamepad1.right_stick_y * (1-Math.abs(gamepad1.right_stick_x));
                rightPower = -gamepad1.right_stick_y;
            }else if (gamepad1.right_stick_x > 0.1){
                leftPower = -gamepad1.right_stick_y;
                rightPower = -gamepad1.right_stick_y * (1-Math.abs(gamepad1.right_stick_x));
            }
        }else if (gamepad2.right_stick_x < -0.1){
            leftPower = -gamepad2.right_stick_y * (1-Math.abs(gamepad2.right_stick_x));
            rightPower = -gamepad2.right_stick_y * (Math.abs(gamepad2.right_stick_x));
        }else if (gamepad1.right_stick_x > 0.1){
            leftPower = -gamepad2.right_stick_y * (Math.abs(gamepad2.right_stick_x));
            rightPower = -gamepad2.right_stick_y * (1-Math.abs(gamepad2.right_stick_x));
        }if (gamepad2.right_stick_y != 0){// Arcade drive for gamepad 2
            if (gamepad2.right_stick_x > 0.1 || gamepad2.right_stick_x < -0.1){
                leftPower = -gamepad2.right_stick_y;
                rightPower = -gamepad2.right_stick_y;
            }else if (gamepad2.right_stick_x < -0.1){
                leftPower = -gamepad2.right_stick_y * (1-Math.abs(gamepad2.right_stick_x));
                rightPower = -gamepad2.right_stick_y;
            }else if (gamepad2.right_stick_x > 0.1){
                leftPower = -gamepad2.right_stick_y;
                rightPower = -gamepad2.right_stick_y * (1-Math.abs(gamepad2.right_stick_x));
            }
        }else if (gamepad2.right_stick_x < -0.1){
            leftPower = -gamepad2.right_stick_y * (1-Math.abs(gamepad2.right_stick_x));
            rightPower = -gamepad2.right_stick_y * (Math.abs(gamepad2.right_stick_x));
        }else if (gamepad1.right_stick_x > 0.1){
            leftPower = -gamepad2.right_stick_y * (Math.abs(gamepad2.right_stick_x));
            rightPower = -gamepad2.right_stick_y * (1-Math.abs(gamepad2.right_stick_x));
        }*/

        if (gamepad1.left_stick_y > .1 || gamepad2.left_stick_y < -.1){
            telemetry.addData("Lift", "Up");
            telemetry.update();

            liftMotor.setTargetPosition(3000); // Prepare armMotor to rotate 3 times, because this encoder will send 1440 pulses per rotation

            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //armMotor.setPower(1);
            liftPower = 1;
        }else if(gamepad1.left_stick_y < -.1 || gamepad2.left_stick_y < -.1){
            telemetry.addData("Lift", "Down");
            telemetry.update();

            liftMotor.setTargetPosition(1000); // Prepare armMotor to return to its initial position

            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftPower = 1;

        }else if ((gamepad1.left_stick_y > -.1 && gamepad1.left_stick_y < .1) || (gamepad2.left_stick_y > -.1 && gamepad2.left_stick_y < .1)){
            liftPower = 0;// Pause the movement of the lift
        }

        telemetry.addData("Lift Position", liftMotor.getCurrentPosition());
        telemetry.update();

        if(gamepad1.x || gamepad2.x) { // Open hand
            rightServo.setPosition(1);
            leftServo.setPosition(0);
        }else if(gamepad1.b || gamepad2.b) { // Close hand
            rightServo.setPosition(0);
            leftServo.setPosition(1);
        }

        liftMotor.setPower(liftPower);
        leftMotor.setPower(leftPower); // Apply power variables to motors
        rightMotor.setPower(rightPower);
    }
}

