// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

  private final PWMSparkMax m_leftDrive = new PWMSparkMax(1);
  private final PWMSparkMax m_rightDrive = new PWMSparkMax(2);
  
  private final Spark m_intake = new Spark(3);

  private final Spark m_belt = new Spark(4);

  private final Spark m_leftLaunch = new Spark(5);
  private final Spark m_rightLaunch = new Spark(6);


  private final DifferentialDrive m_robotDrive =
      new DifferentialDrive(m_leftDrive::set, m_rightDrive::set);

  private final DigitalInput topLeftLimitSwitch = new DigitalInput(1);
  private final DigitalInput bottomLeftLimitSwitch = new DigitalInput(2);
  private final DigitalInput topRightLimitSwitch = new DigitalInput(3);
  private final DigitalInput bottomRightLimitSwitch = new DigitalInput(4);
  private final Spark m_leftLift = new Spark(7);
  private final Spark m_rightLift = new Spark(8);



  private final PS4Controller m_controller = new PS4Controller(0);

  private final Timer m_timer = new Timer();

  public Robot() {
    SendableRegistry.addChild(m_robotDrive, m_leftDrive);
    SendableRegistry.addChild(m_robotDrive, m_rightDrive);
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightDrive.setInverted(false);
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // Drive for 2 seconds
    if (m_timer.get() < 2.0) {
      // Drive forwards half speed, make sure to turn input squaring off
      m_robotDrive.arcadeDrive(0.5, 0.0, false);
    } else {
      m_robotDrive.stopMotor(); // stop robot
    }
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}


  public void moveBelt() {
    m_belt.set(1);
  }

  public void stopBelt() {
    m_belt.set(0);
  }

//Edit the values in the parenthesis to slow it down or speed it up. A negative number will invert the motor. 
//Have seperate so that they can be controlled seperatley. 
  public void leftLift() {
    m_leftLift.set(-.125);
    if(bottomLeftLimitSwitch.get()){
      m_leftLift.set(0);
    }
  }
  public void rightLift() {
    m_rightLift.set(.125);
    if(bottomRightLimitSwitch.get()){
      m_rightLift.set(0);
    }
  }
  public void stopLeftLift() {
  m_leftLift.set(0);
}
  public void stopRightLift(){
  m_rightLift.set(0);
}

//Make sure to check these values and edit accordingly. 


//These will be automated to drop the robot off the chain. (Please check to see if we can do this)
  public void dropRobot(){
  m_leftLift.set(-.125);
  if(topLeftLimitSwitch.get()){
    m_leftLift.set(0);
  }
  m_rightLift.set(0.125);
  if(topRightLimitSwitch.get()){
    m_rightLift.set(0);
  }
}





  
//These values were random. Change accordingly.
  public void ejectAmp() {
    m_leftLaunch.set(0.5);
    m_rightLaunch.set(-0.5);
    moveBelt();
  }
  public void stopEjectAmp() {
    stopBelt();
    m_leftLaunch.set(0);
    m_rightLaunch.set(0);
  }

//Runs the eject motors first before the belt. Should allow for the wheels to get up to speed. 
  public void ejectSpeaker() {
    m_leftLaunch.set(1);
    m_rightLaunch.set(-1);
    moveBelt();
  }
  public void stopEjectSpeaker() {
    stopBelt();
    m_leftLaunch.set(0);
    m_rightLaunch.set(0);
  }

//Intake roller motors. 
  public void intake(){
    m_intake.set(-0.25);
  }
  public void stopIntake(){
    m_intake.set(0);
  }




  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    

    //Shoot to amp with Square
    if(m_controller.getSquareButtonPressed())
      ejectAmp();
    if(m_controller.getSquareButtonReleased())
      stopEjectAmp();

    //Intake with triangle
    if(m_controller.getTriangleButtonPressed())
      intake();
    if(m_controller.getTriangleButtonReleased())
      stopIntake();

    //Shoot to speaker with circle
    if(m_controller.getCircleButtonPressed())
      ejectSpeaker();
    if(m_controller.getCircleButtonReleased())
      stopEjectSpeaker();
    
    //Lift Robot with triggers(L1 and R1). Hold to go up for each. 
    if(m_controller.getL1ButtonPressed()){
      leftLift();
    }
    if(m_controller.getL1ButtonReleased()){
      stopLeftLift();
    }
    if(m_controller.getR1ButtonPressed()){
      rightLift();
    }
    if(m_controller.getR1ButtonReleased()){
      stopRightLift();
    }

    //Drive with left joystick
    m_robotDrive.arcadeDrive(-m_controller.getLeftY(), -m_controller.getRightX());



  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
