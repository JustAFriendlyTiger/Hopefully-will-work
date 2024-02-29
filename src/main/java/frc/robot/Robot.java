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

  private final DigitalInput topLimitSwitch = new DigitalInput(0);
  private final DigitalInput bottomLimitSwitch = new DigitalInput(0);


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

  public void lift() {

  }

  public void ejectAmp() {
    m_leftDrive.set(0.5);
    m_rightDrive.set(-0.5);
  }

  public void stopEjectAmp() {
    m_leftDrive.set(0);
    m_rightDrive.set(0);
  }


  public void ejectSpeaker() {
    m_leftDrive.set(1);
    m_rightDrive.set(-1);
  }

  public void stopEjectSpeaker() {
    m_leftDrive.set(0);
    m_rightDrive.set(0);
  }


  public void intake(){
    m_leftDrive.set(-0.25);
    m_rightDrive.set(0.25);
  }

  public void stopIntake(){
    m_leftDrive.set(0);
    m_rightDrive.set(0);
  }




  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    
    
    if(m_controller.getSquareButtonPressed());
      ejectAmp();
    if(m_controller.getSquareButtonReleased());
      stopEjectAmp();

    
    if(m_controller.getTriangleButtonPressed());
      intake();
    if(m_controller.getTriangleButtonReleased());
      stopIntake();


    if(m_controller.getCircleButtonPressed());
      ejectSpeaker();
    if(m_controller.getCircleButtonReleased());
      stopEjectSpeaker();
    //m_robotDrive.arcadeDrive(-m_controller.getLeftY(), -m_controller.getRightX());



  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
