package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class PortsTesting extends LinearOpMode {
    // Control Hub Motors
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontArm;
    DcMotor testMotor; // For testing only!
    
    // Control Hub Servos
    Servo clawServoRight;
    Servo clawServoLeft;
    Servo leftArm;
    
    // Expansion Hub Motors
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor linSlideUpper;
    DcMotor linSlideLower;
    
    // Expansion Hub Servos
    Servo linearServo;
    Servo rightArm;
    
    // Control limits
    private double clawLowerBound = 0.0;
    private double clawUpperBound = 0.21;
    private double armLowerBound = 0.15;
    private double armUpperBound = 0.5;
    private int frontArmLowerBound = 0;
    private int frontArmUpperBound = 1000;
    private int linSlideLowerBound = -20;
    private int linSlideUpperBound = 20;
    private int testMotorLowerBound = 0;
    private int testMotorUpperBound = 1000;
    
    private void initControlHub() {
        telemetry.addData("Status", "Initializing Control Hub");
        telemetry.update();
        
        // Get motors
        frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        backLeft  = hardwareMap.dcMotor.get("BackLeft");
        frontArm  = hardwareMap.dcMotor.get("FrontArm");
        testMotor = hardwareMap.dcMotor.get("TestMotor");
        
        // Get servos
        clawServoRight = hardwareMap.servo.get("ClawServoRight");
        clawServoLeft  = hardwareMap.servo.get("ClawServoLeft");
        leftArm        = hardwareMap.servo.get("LeftArm");
        
        telemetry.addData("Status", "Initialized Control Hub");
        telemetry.update();
    }
    
    public void initExpansionHub() {
        telemetry.addData("Status", "Initializing Expansion Hub");
        telemetry.update();
        
        // Get motors
        frontRight    = hardwareMap.dcMotor.get("FrontRight");
        backRight     = hardwareMap.dcMotor.get("BackRight");
        linSlideLower = hardwareMap.dcMotor.get("LinSlideLower");
        linSlideUpper = hardwareMap.dcMotor.get("LinSlideUpper");
        
        // Get servos
        linearServo = hardwareMap.servo.get("LinearServo");
        rightArm    = hardwareMap.servo.get("RightArm");
        
        telemetry.addData("Status", "Initialized Expansion Hub");
        telemetry.update();
    }
    
    public void initAll() {
        initControlHub();
        initExpansionHub();
        
        telemetry.addData("Status", "Initialized all");
        telemetry.update();
        
        waitForStart();
    }
    
    private void setClawPosition(double pos) {
        // Scale normalized data
        double scaledPos = Helper.mix(clawLowerBound, clawUpperBound, pos);
        
        telemetry.addData("Setting claw position", scaledPos);
        
        clawServoRight.setPosition(scaledPos);
        clawServoLeft.setPosition(1.0 - scaledPos);
    }
    
    private void setArmPosition(double pos) {
        // Scale normalized data
        double scaledPos = Helper.mix(armLowerBound, armUpperBound, pos);
        
        telemetry.addData("Setting arm position", scaledPos);
        
        clawServoRight.setPosition(scaledPos);
        clawServoLeft.setPosition(1.0 - scaledPos);
    }
    
    private void setFrontArmPosition(double pos) {
        // Scale normalized data
        int scaledPos = (int) Helper.mix((double) frontArmLowerBound, (double) frontArmUpperBound, pos);
        
        telemetry.addData("Setting front arm positon", scaledPos);
        
        frontArm.setTargetPosition(scaledPos);
        frontArm.setPower(0.5);
        frontArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void setLinSlidePosition(double pos) {
        // Scale normalized data
        int scaledPos = (int) Helper.mix((double) linSlideLowerBound, (double) linSlideUpperBound, pos);
        
        telemetry.addData("Setting linear slide positon", scaledPos);
        
        linSlideLower.setTargetPosition(scaledPos);
        linSlideUpper.setTargetPosition(-scaledPos);
        linSlideLower.setPower(0.01);
        linSlideUpper.setPower(0.01);
        linSlideLower.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linSlideUpper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    
    private void setTestMotorPosition(double pos) {
        // Scale normalized data
        int scaledPos = (int) Helper.mix((double) testMotorLowerBound, (double) testMotorUpperBound, pos);
        
        telemetry.addData("Setting test motor positon", scaledPos);
        
        testMotor.setTargetPosition(scaledPos);
        testMotor.setPower(0.5);
        testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    
    private void testFrontArm() {
        if (gamepad1.x) {
            setFrontArmPosition(0.0);
        }
        
        if (gamepad1.y) {
            setFrontArmPosition(1.0);
        }
    }
    
    private void testLinSlide() {
        if (gamepad1.x) {
            setLinSlidePosition(0.0);
        }
        
        if (gamepad1.y) {
            setLinSlidePosition(1.0);
        }
    }
    
    private void testTestMotor() {
        if (gamepad1.x) {
            setTestMotorPosition(0.0);
        }
        
        if (gamepad1.y) {
            setTestMotorPosition(1.0);
        }
    }
    
    private void displayStats() {
        telemetry.addData("Current front arm position", frontArm.getCurrentPosition());
        telemetry.addData("Current lower linear slide position", linSlideLower.getCurrentPosition());
        telemetry.addData("Current upper linear slide position", linSlideUpper.getCurrentPosition());
    }
    
    @Override
    public void runOpMode() throws InterruptedException {
        initAll();
        
        frontArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linSlideLower.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linSlideUpper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linSlideLower.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linSlideUpper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (isStopRequested()) return;
        
        while (opModeIsActive()) {
            // testFrontArm();
            testLinSlide();
            // testTestMotor();
            
            displayStats();
            telemetry.update();
        }
    }
}
