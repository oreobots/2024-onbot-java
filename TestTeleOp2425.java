package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "TestTeleOp2425")
public class TestTeleOp2425 extends LinearOpMode {

  private DcMotor lateralSlide;
  private DcMotor verticalSlide;
  private DcMotor frontRight;
  private DcMotor frontLeft;
  private DcMotor rearRight;
  private DcMotor rearLeft;
  private Servo bucketServo;
  private CRServo intakeServo;
  private Servo flipperServo;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    int tgtPosition;
    ElapsedTime speedChangeTime;
    double sensitivity;
    double MaxPower;
    float Y;
    float X;
    double RX;
    double DENOMINATOR;
    double FrontLeftPower;
    double RearLeftPower;
    double FrontRightPower;
    double RearRightPower;
    double ServoPosition;

    lateralSlide = hardwareMap.get(DcMotor.class, "lateralSlide");
    verticalSlide = hardwareMap.get(DcMotor.class, "verticalSlide");
    frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    rearRight = hardwareMap.get(DcMotor.class, "rearRight");
    rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
    intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
    bucketServo = hardwareMap.get(Servo.class, "bucketServo");
    flipperServo = hardwareMap.get(Servo.class, "flipperServo");
    // Put initialization blocks here.
    waitForStart();
    speedChangeTime = new ElapsedTime();
    lateralSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    verticalSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    sensitivity = 1;
    bucketServo.setPosition(1);
    MaxPower = 0.7;
    tgtPosition = 3;
    flipperServo.setPosition(0);
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        // Put loop blocks here.
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        telemetry.update();
        Y = -gamepad1.left_stick_y;
        X = gamepad1.right_stick_x;
        RX = gamepad1.left_stick_x * 1.1;
        DENOMINATOR = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(Y), Math.abs(X), Math.abs(RX))), 1));
        FrontLeftPower = (Y + X + RX) / DENOMINATOR;
        RearLeftPower = ((Y - X) + RX) / DENOMINATOR;
        FrontRightPower = ((Y - X) - RX) / DENOMINATOR;
        RearRightPower = ((Y + X) - RX) / DENOMINATOR;
        frontLeft.setPower(fabsolute(FrontLeftPower, MaxPower));
        rearLeft.setPower(fabsolute(RearLeftPower, MaxPower));
        frontRight.setPower(fabsolute(FrontRightPower, MaxPower));
        rearRight.setPower(fabsolute(RearRightPower, MaxPower));
        
        if (gamepad1.left_bumper && speedChangeTime.milliseconds() > 500) {
          if (MaxPower == 0.4) {
            MaxPower = 0.7;
          } /*else if (MaxPower == 0.6) {
           MaxPower = 1;
          }*/ else {
           MaxPower = 0.4;
          }
          speedChangeTime.reset();
          //telemetry.speak(MaxPower, null, null);
        } 
        if (gamepad1.y || gamepad2.y) {
          flipperServo.setPosition(0.1);

        } else {
          flipperServo.setPosition(1);

        }
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
          verticalSlide.setDirection(DcMotor.Direction.FORWARD);
          verticalSlide.setPower(0.5);
        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
          verticalSlide.setDirection(DcMotor.Direction.REVERSE);
          verticalSlide.setPower(0.7);
        } else {
          verticalSlide.setPower(0);
        }
        if (gamepad1.dpad_left || gamepad2.dpad_left) {
          lateralSlide.setDirection(DcMotor.Direction.REVERSE);
          lateralSlide.setPower(0.5);
        } else if (gamepad1.dpad_right || gamepad2.dpad_right){
          lateralSlide.setDirection(DcMotor.Direction.FORWARD);
          lateralSlide.setPower(0.7);
        } else {
          lateralSlide.setPower(0);
        }

        if ((gamepad1.right_trigger >= 0.1) || 
              (gamepad2.right_trigger >= 0.1)) {
          ServoPosition = 1;
          intakeServo.setPower(ServoPosition);
        } else {
          intakeServo.setPower(0);
        }
        
        if (gamepad1.a || gamepad2.a) {
          bucketServo.setPosition(1);
        } else {
          bucketServo.setPosition(0.2);
        }
        telemetry.addData("Front Left Power", frontLeft.getPower());
        telemetry.addData("intake position", intakeServo.getPower());
        telemetry.addData("bucket position", bucketServo.getPosition());
        telemetry.addData("flipper position", flipperServo.getPosition());
        telemetry.addData("Rear Left Power", rearLeft.getPower());
        telemetry.addData("Front Right Power", frontRight.getPower());
        telemetry.addData("Rear Right Power", rearRight.getPower());
        telemetry.addData("Gamepad Y", gamepad1.y);
        telemetry.addData("DpadUp", gamepad1.dpad_up);
        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("DpadUp", gamepad1.dpad_up);
        telemetry.addData("Max Power", MaxPower);
        telemetry.addData("X", X);
        telemetry.addData("Y", Y);
        telemetry.addData("RX", RX);
        telemetry.update();
      }
    }
  }

  /**
   * Describe this function...
   */
  private double fabsolute(double value, double power) {
    double fabs_value;

    if (value > 0) {
      fabs_value = JavaUtil.minOfList(JavaUtil.createListWith(value, power));
    } else if (value < 0) {
      fabs_value = JavaUtil.maxOfList(JavaUtil.createListWith(value, -power));
    } else {
      fabs_value = value;
    }
    return fabs_value;
  }
  /*public void elevatorEncoder(int ticks, double power) {
        int NewLeftLiftTarget;
        int NewRightLiftTarget;


        //creates target position for slides to go to
        NewRightLiftTarget = slideRight.getCurrentPosition() + (int) (ticks);
        NewLeftLiftTarget = slideLeft.getCurrentPosition() + (int) (ticks);

        //sets it
        slideRight.setTargetPosition(NewRightLiftTarget);
        slideLeft.setTargetPosition(NewLeftLiftTarget);

        //runs to new position
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        //with this power
        slideRight.setPower(power);
        slideLeft.setPower(power);

        //use this if you want the robot to do something as the slides are moving
        while (slideRight.isBusy() || slideLeft.isBusy()) {
          //for multithreading
        }
        
        //once not busy, set power to 0
        slideRight.setPower(0);
        slideLeft.setPower(0);
        
        
        //change mode back to run w/ encoders
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

  }
