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

import org.team102.robots.robot2019.commands.CommandClimb;
import org.team102.robots.robot2019.commands.CommandExtendArm;
import org.team102.robots.robot2019.commands.CommandMoveArmManual;
import org.team102.robots.robot2019.commands.CommandSetCargoManip;
import org.team102.robots.robot2019.commands.CommandSetClimber;
import org.team102.robots.robot2019.commands.CommandSetHatchManip;
import org.team102.robots.robot2019.commands.CommandUseArmSetpoint;
import org.team102.robots.robot2019.lib.CommonIDs;
import org.team102.robots.robot2019.lib.CustomOperatorConsole;
import org.team102.robots.robot2019.lib.commandsAndTrigger.AxisTrigger;
import org.team102.robots.robot2019.lib.commandsAndTrigger.CommandPlayRumble;
import org.team102.robots.robot2019.lib.commandsAndTrigger.LogicGateTrigger;
import org.team102.robots.robot2019.lib.commandsAndTrigger.TimedTrigger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class OI {
	
	public Joystick driverJoystick;
	public CustomOperatorConsole opConsole;
	public Joystick testJoystick;
	
	public OI() {
		driverJoystick = new Joystick(RobotMap.JOYSTICK_ID_DRIVER);
		opConsole = new CustomOperatorConsole(RobotMap.JOYSTICK_ID_OPERATOR);
		
		if(RobotMap.IS_TEST_JOYSTICK_ENABLED) {
			testJoystick = new Joystick(RobotMap.JOYSTICK_ID_TEST);
		}
	}
	
	public void init() {
		// All button assignments on the operator console should be fairly self-explanatory
		// While progressing, they will light up the LEDs on the Operator Console the color of their buttons
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_TOP).whileHeld(new CommandUseArmSetpoint(ArmConfig.ROCKET_HATCH_TOP, RobotMap.OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_MIDDLE).whileHeld(new CommandUseArmSetpoint(ArmConfig.ROCKET_HATCH_MIDDLE, RobotMap.OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_BOTTOM).whileHeld(new CommandUseArmSetpoint(ArmConfig.ROCKET_HATCH_BOTTOM, RobotMap.OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_TOP).whileHeld(new CommandUseArmSetpoint(ArmConfig.ROCKET_CARGO_TOP, RobotMap.OP_CONTROLLER_PATTERN_SET_RED_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_MIDDLE).whileHeld(new CommandUseArmSetpoint(ArmConfig.ROCKET_CARGO_MIDDLE, RobotMap.OP_CONTROLLER_PATTERN_SET_RED_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_BOTTOM).whileHeld(new CommandUseArmSetpoint(ArmConfig.ROCKET_CARGO_BOTTOM, RobotMap.OP_CONTROLLER_PATTERN_SET_RED_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_CARGO).whileHeld(new CommandUseArmSetpoint(ArmConfig.CARGO_SHIP_CARGO, RobotMap.OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_OR_LOADING_STATION_HATCH).whileHeld(new CommandUseArmSetpoint(ArmConfig.CARGO_SHIP_OR_LOADING_STATION_HATCH, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_ABOVE_POSITION).whileHeld(new CommandUseArmSetpoint(ArmConfig.FLOOR_HATCH_ABOVE_POSITION, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_COMMIT).whileHeld(new CommandUseArmSetpoint(ArmConfig.FLOOR_HATCH_COMMIT, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_CARGO).whileHeld(new CommandUseArmSetpoint(ArmConfig.FLOOR_CARGO, RobotMap.OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN).whileHeld(new CommandMoveArmManual(false, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP).whileHeld(new CommandMoveArmManual(false, false));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN).whileHeld(new CommandMoveArmManual(true, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP).whileHeld(new CommandMoveArmManual(true, false));
		
		// Driver A: When pressed, extend the hatch ejector, when released, contract it.
		getButton(CommonIDs.Gamepad.BTN_A, false).whileHeld(new CommandSetHatchManip());
		
		// Driver DPad Up for a certain amount of time: Climb
		getTimedPOV(CommonIDs.POVSwitch.UP_CENTER, false, false).whenActive(new CommandClimb());
		
		// Driver DPad Down for a certain (longer) amount of time: Retract climber
		getTimedPOV(CommonIDs.POVSwitch.DOWN_CENTER, true, false).whenActive(new CommandSetClimber(false));
		
		// Driver left bumper or left trigger: When pressed, start up the cargo roller going out, when released, stop it.
		getAxisOrButton(CommonIDs.Gamepad.AXIS_LEFT_TRIGGER, CommonIDs.Gamepad.BTN_LEFT_BUMPER, false).whileActive(new CommandSetCargoManip(false));
		
		// Driver right bumper or right trigger: When pressed, start up the cargo roller going in, when released, stop it.
		getAxisOrButton(CommonIDs.Gamepad.AXIS_RIGHT_TRIGGER, CommonIDs.Gamepad.BTN_RIGHT_BUMPER, false).whileActive(new CommandSetCargoManip(true));
		
		// If the test joystick is enabled...
		if(testJoystick != null) {
			// Set the arm extender's state from the state of the Back button
			JoystickButton extendArmTest = getButton(CommonIDs.Gamepad.BTN_BACK, true);
			extendArmTest.whenPressed(new CommandExtendArm(true));
			extendArmTest.whenReleased(new CommandExtendArm(false));
			
			// Climb unconditionally when Start is pressed
			getButton(CommonIDs.Gamepad.BTN_START, true).whenPressed(new CommandClimb.CommandClimbUnconditionally());
			
			// Extend the climber when X is pressed
			getButton(CommonIDs.Gamepad.BTN_X, true).whenPressed(new CommandSetClimber(true));
			
			// Contract the climber when Y is pressed
			getButton(CommonIDs.Gamepad.BTN_Y, true).whenPressed(new CommandSetClimber(false));
			
			// Extend the front stage of the climber when A is pressed
			getButton(CommonIDs.Gamepad.BTN_A, true).whenPressed(new CommandSetClimber(true, true));
			
			// Contract the front stage of the climber when B is pressed
			getButton(CommonIDs.Gamepad.BTN_B, true).whenPressed(new CommandSetClimber(true, false));
		}
	}
	
	public double getTimeRemaining() {
		double time = DriverStation.getInstance().getMatchTime();
		
		if(time == -1) {
			return 120;
		} else {
			return time;
		}
	}
	
	private TimedTrigger getTimedPOV(int position, boolean longerTime, boolean testJS) {
		return addTimeConfirm(getPOV(position, testJS), longerTime);
	}
	
	private LogicGateTrigger getAxisOrButton(int axis, int button, boolean testJS) {
		return LogicGateTrigger.or(getAxis(axis, testJS), getButton(button, testJS));
	}
	
	private POVButton getPOV(int position, boolean testJS) {
		return new POVButton(getJS(testJS), position);
	}
	
	private JoystickButton getButton(int button, boolean testJS) {
		return new JoystickButton(getJS(testJS), button);
	}
	
	private AxisTrigger getAxis(int axis, boolean testJS) {
		return AxisTrigger.forGreaterThan(getJS(testJS), axis, RobotMap.JOYSTICK_MIN_AXIS_PRESS_TO_ACTIVATE_TRIGGER);
	}
	
	@SuppressWarnings("resource")
	private TimedTrigger addTimeConfirm(Trigger trig, boolean longerTime) {
		double time = RobotMap.JOYSTICK_TIMED_TRIGGER_CONFIRM_TIME;
		if(longerTime) {
			time = RobotMap.JOYSTICK_TIMED_TRIGGER_LONG_CONFIRM_TIME;
		}
		
		return new TimedTrigger(trig, time).withNotification(new CommandPlayRumble(driverJoystick, RobotMap.RUMBLE_PROGRESS, false));
	}
	
	private Joystick getJS(boolean testJS) {
		if(testJS) {
			return testJoystick;
		} else {
			return driverJoystick;
		}
	}
	
	public void setOpConsoleIdlePattern() {
		opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_OFF);
	}
	
	public void setOpConsoleDisabledPattern() {
		opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_SCROLLING_ORANGE);
	}
}