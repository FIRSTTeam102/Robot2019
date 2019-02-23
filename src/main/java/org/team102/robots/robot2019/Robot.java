/*
 * The code for FRC Team #102's robot for the 2019 game, Destination: Deep Space
 * Copyright (C) 2019  Robotics Fund Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * Contact us at: firstteam102@gmail.com
 */

package org.team102.robots.robot2019;

import org.team102.robots.robot2019.lib.arduino.ArduinoConnection;
import org.team102.robots.robot2019.subsystems.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
	public static SubsystemDriveTrain driveTrain;
	public static SubsystemArm arm;
	public static SubsystemCargoManipulator cargoManip;
	public static SubsystemHatchManipulator hatchManip;
	public static SubsystemClimber climber;
	public static SubsystemCameras cameras;
	public static SubsystemCentering centering;
	public static SubsystemAesthetics robotAesthetics;
	public static SubsystemDriverNotification driverNotif;
	
	public static OI oi;
	
	// ALWAYS include this!!
	public static void main(String[] args) {
		TimedRobot.startRobot(Robot::new);
	}
	
	@Override
	public void robotInit() {
		ArduinoConnection.findArduinos();
		
		driveTrain = new SubsystemDriveTrain();
		arm = new SubsystemArm();
		cargoManip = new SubsystemCargoManipulator();
		hatchManip = new SubsystemHatchManipulator();
		climber = new SubsystemClimber();
		cameras = new SubsystemCameras();
		centering = new SubsystemCentering();
		robotAesthetics = new SubsystemAesthetics();
		driverNotif = new SubsystemDriverNotification();
		
		oi = new OI();
		driverNotif.initOIPortions();
		oi.init();
		
		oi.setOpConsoleDisabledPattern();
		setLongLightStripPattern(false);
	}
	
	@Override
	public void robotPeriodic() {
		Scheduler.getInstance().run();
	}
	
	@Override
	public void teleopInit() {
		oi.setOpConsoleIdlePattern();
		setLongLightStripPattern(true);
	}
	
	@Override
	public void teleopPeriodic() {}
	
	@Override
	public void autonomousInit() {
		oi.setOpConsoleIdlePattern();
		setLongLightStripPattern(true);
	}
	
	@Override
	public void autonomousPeriodic() {}
	
	@Override
	public void disabledInit() {
		oi.setOpConsoleDisabledPattern();
		setLongLightStripPattern(false);
	}
	
	@Override
	public void disabledPeriodic() {}
	
	@Override
	public void testInit() {
		oi.setOpConsoleIdlePattern();
		setLongLightStripPattern(true);
	}
	
	@Override
	public void testPeriodic() {}
	
	private void setLongLightStripPattern(boolean enabled) {
		int[] patterns = RobotMap.LONG_LIGHT_STRIP_PATTERN_SET_DISABLED;
		
		if(enabled) {
			switch(DriverStation.getInstance().getAlliance()) {
				case Red: {
					patterns = RobotMap.LONG_LIGHT_STRIP_PATTERN_SET_RED_ALLIANCE;
					break;
				}
				
				case Blue: {
					patterns = RobotMap.LONG_LIGHT_STRIP_PATTERN_SET_BLUE_ALLIANCE;
					break;
				}
				
				default:
			}
		}
		
		robotAesthetics.setLongLightStripPatternSet(patterns);
	}
}