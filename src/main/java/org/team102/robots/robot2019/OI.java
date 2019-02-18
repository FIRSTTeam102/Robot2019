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

public class OI {
	
	public Joystick driverJoystick;
	public CustomOperatorConsole opConsole;
	
	public OI() {
		driverJoystick = new Joystick(RobotMap.JOYSTICK_ID_DRIVER);
		opConsole = new CustomOperatorConsole(RobotMap.JOYSTICK_ID_OPERATOR);
	}
	
	@SuppressWarnings("resource") // Because otherwise it would complain about all the joystick buttons not being closed, even though there is no good reason why they should be closed.
	public void init() {
		// All button assignments on the operator console should be fairly self-explanatory
		// While progressing, they will light up the LEDs on the Operator Console the color of their buttons
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_TOP).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_ROCKET_HATCH_TOP, RobotMap.OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_MIDDLE).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_ROCKET_HATCH_MIDDLE, RobotMap.OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_HATCH_BOTTOM).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_ROCKET_HATCH_BOTTOM, RobotMap.OP_CONTROLLER_PATTERN_SET_YELLOW_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_TOP).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_ROCKET_CARGO_TOP, RobotMap.OP_CONTROLLER_PATTERN_SET_RED_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_MIDDLE).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_ROCKET_CARGO_MIDDLE, RobotMap.OP_CONTROLLER_PATTERN_SET_RED_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_ROCKET_CARGO_BOTTOM).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_ROCKET_CARGO_BOTTOM, RobotMap.OP_CONTROLLER_PATTERN_SET_RED_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_CARGO).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_CARGO_SHIP_CARGO, RobotMap.OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_CARGO_SHIP_OR_LOADING_STATION_HATCH).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_CARGO_SHIP_OR_LOADING_STATION_HATCH, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_ABOVE_POSITION).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_FLOOR_HATCH_ABOVE_POSITION, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_HATCH_COMMIT).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_FLOOR_HATCH_COMMIT, RobotMap.OP_CONTROLLER_PATTERN_SET_BLUE_BUTTON));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_FLOOR_CARGO).whileHeld(new CommandUseArmSetpoint(RobotMap.ARM_SETPOINT_FLOOR_CARGO, RobotMap.OP_CONTROLLER_PATTERN_SET_GREEN_BUTTON));
		
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_DOWN).whileHeld(new CommandMoveArmManual(false, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_ELBOW_UP).whileHeld(new CommandMoveArmManual(false, false));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_DOWN).whileHeld(new CommandMoveArmManual(true, true));
		opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_MANUAL_WRIST_UP).whileHeld(new CommandMoveArmManual(true, false));
		
		// Driver A: When pressed, extend the hatch ejector, when released, contract it.
		new JoystickButton(driverJoystick, CommonIDs.Gamepad.BTN_A).whileHeld(new CommandSetHatchManip());
		
		// Driver DPad Up for a certain amount of time: Climb
		new TimedTrigger(new POVButton(driverJoystick, CommonIDs.POVSwitch.UP_CENTER), RobotMap.JOYSTICK_TIMED_TRIGGER_CONFIRM_TIME)
				.withNotification(new CommandPlayRumble(driverJoystick, RobotMap.RUMBLE_PROGRESS, false))
				.whenActive(new CommandClimb());
		
		// Driver DPad Down for a certain (longer) amount of time: Retract climber
		new TimedTrigger(new POVButton(driverJoystick, CommonIDs.POVSwitch.DOWN_CENTER), RobotMap.JOYSTICK_TIMED_TRIGGER_LONG_CONFIRM_TIME)
				.withNotification(new CommandPlayRumble(driverJoystick, RobotMap.RUMBLE_PROGRESS, false))
				.whenActive(new CommandSetClimber(false));
		
		// Driver left bumper or left trigger: When pressed, start up the cargo roller going out, when released, stop it.
		LogicGateTrigger.or(
				new JoystickButton(driverJoystick, CommonIDs.Gamepad.BTN_LEFT_BUMPER),
				getTriggerForAxis(driverJoystick, CommonIDs.Gamepad.AXIS_LEFT_TRIGGER)
		).whileActive(new CommandSetCargoManip(false));
		
		// Driver right bumper or right trigger: When pressed, start up the cargo roller going in, when released, stop it.
		LogicGateTrigger.or(
				new JoystickButton(driverJoystick, CommonIDs.Gamepad.BTN_RIGHT_BUMPER),
				getTriggerForAxis(driverJoystick, CommonIDs.Gamepad.AXIS_RIGHT_TRIGGER)
		).whileActive(new CommandSetCargoManip(true));
		
		JoystickButton jb = opConsole.getButton(RobotMap.OP_CONTROLLER_BUTTON_ID_UNUSED);
		jb.whenPressed(new CommandExtendArm(true));
		jb.whenReleased(new CommandExtendArm(false));
		
		setOpConsoleDisabledPattern();
	}
	
	public double getTimeRemaining() {
		double time = DriverStation.getInstance().getMatchTime();
		
		if(time == -1) {
			return 120;
		} else {
			return time;
		}
	}
	
	public static AxisTrigger getTriggerForAxis(Joystick js, int axis) {
		return AxisTrigger.forGreaterThan(js, axis, RobotMap.JOYSTICK_MIN_AXIS_PRESS_TO_ACTIVATE_TRIGGER);
	}
	
	public void setOpConsoleIdlePattern() {
		opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_OFF);
	}
	
	public void setOpConsoleDisabledPattern() {
		opConsole.setLightPattern(RobotMap.OP_CONTROLLER_PATTERN_SCROLLING_ORANGE);
	}
}