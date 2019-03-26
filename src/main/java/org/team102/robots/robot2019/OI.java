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

import org.team102.robots.robot2019.commands.*;

import org.team102.robots.robot2019.lib.CommonIDs;
import org.team102.robots.robot2019.lib.CustomOperatorConsole;
import org.team102.robots.robot2019.lib.commandsAndTrigger.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class OI {
	
	private LogicGateTrigger climbTimeCheck = new LogicGateTrigger(() -> getTimeRemaining() <= RobotMap.CLIMB_MAX_TIME_REMAINING);
	
	public Joystick driverJoystick;
	public CustomOperatorConsole opConsole;
	
	public OI() {
		driverJoystick = new Joystick(RobotMap.JOYSTICK_ID_DRIVER);
		opConsole = new CustomOperatorConsole(RobotMap.JOYSTICK_ID_OPERATOR);
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
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_ABOVE_POSITION).whileHeld(new CommandUseArmSetpoint(ArmConfig.VERTICAL_VERTICAL, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_COMMIT).whileHeld(new CommandUseArmSetpoint(ArmConfig.HORIZONTAL_WRIST, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_CARGO).whileHeld(new CommandUseArmSetpoint(ArmConfig.FLOOR_CARGO, RobotMap.OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN).whileHeld(new CommandMoveArmManual(false, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP).whileHeld(new CommandMoveArmManual(false, false));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN).whileHeld(new CommandMoveArmManual(true, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP).whileHeld(new CommandMoveArmManual(true, false));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_UNUSED).whenPressed(new CommandExtendArm(CommandExtendArm.ArmExtendStatus.TOGGLE));
		
		// Driver Bumpers: When pressed, extend the hatch ejector, when released, contract it.
		LogicGateTrigger.or(getButton(CommonIDs.Gamepad.BTN_LEFT_BUMPER), getButton(CommonIDs.Gamepad.BTN_RIGHT_BUMPER)).whileActive(new CommandSetHatchManip());
		
		// Driver DPad: Climb -- This code is old, so it is currently disabled
		//getClimbPOV(CommonIDs.POVSwitch.UP_CENTER).whenActive(new CommandClimb());
		//getClimbPOV(CommonIDs.POVSwitch.DOWN_CENTER).whenActive(new CommandSetClimber(false));
		
		// Driver triggers: While held, use the cargo roller
		getAxis(CommonIDs.Gamepad.AXIS_LEFT_TRIGGER).whileActive(new CommandSetCargoManip(false));
		getAxis(CommonIDs.Gamepad.AXIS_RIGHT_TRIGGER).whileActive(new CommandSetCargoManip(true));
		
		// Climber primary stage: Extend with X, contract with Y
		getClimbButton(CommonIDs.Gamepad.BTN_X).whenActive(new CommandSetClimber(true));
		getClimbButton(CommonIDs.Gamepad.BTN_Y).whenActive(new CommandSetClimber(false));
		
		// Climber front stage: Extend with A, contract with B
		getClimbButton(CommonIDs.Gamepad.BTN_A).whenActive(new CommandSetClimber(true, true));
		getClimbButton(CommonIDs.Gamepad.BTN_B).whenActive(new CommandSetClimber(true, false));
		
		// Driver start: switch cameras
		getButton(CommonIDs.Gamepad.BTN_START).whenActive(new CommandAdvanceVideoOutput(Robot.driverNotif.streams));
	}
	
	public double getTimeRemaining() {
		double time = DriverStation.getInstance().getMatchTime();
		
		if(time == -1) {
			return 35;
		} else {
			return time;
		}
	}
	
	private LogicGateTrigger getClimbButton(int button) {
		return LogicGateTrigger.and(getButton(button), climbTimeCheck);
	}
	
	@SuppressWarnings("resource")
	private Trigger getClimbPOV(int position) {
		return new TimedTrigger(getPOV(position), RobotMap.JOYSTICK_TIMED_TRIGGER_CONFIRM_TIME).withNotification(new CommandPlayRumble(driverJoystick, RobotMap.RUMBLE_PROGRESS, false));
	}
	
	private Trigger getPOV(int position) {
		if(position == -1) {
			return getNoOp();
		}
		
		return new POVButton(driverJoystick, position);
	}
	
	private Trigger getButton(int button) {
		if(button == -1) {
			return getNoOp();
		}
		
		return new JoystickButton(driverJoystick, button);
	}
	
	private Trigger getAxis(int axis) {
		if(axis == -1) {
			return getNoOp();
		}
		
		return AxisTrigger.forGreaterThan(driverJoystick, axis, RobotMap.JOYSTICK_MIN_AXIS_PRESS_TO_ACTIVATE_TRIGGER);
	}
	
	private LogicGateTrigger getNoOp() {
		return LogicGateTrigger.or();
	}
	
	public void setOpConsoleIdlePattern() {
		opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_OFF);
	}
	
	public void setOpConsoleDisabledPattern() {
		opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_SCROLLING_ORANGE);
	}
}