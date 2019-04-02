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

package org.team102.robots.robot2019.subsystems;

import org.team102.robots.robot2019.Robot;
import org.team102.robots.robot2019.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemClimber  extends Subsystem {
	
	private Solenoid climber;
	private Solenoid frontStage;
	
	private AnalogInput safetySensor;
	
	public SubsystemClimber() {
		super("Climber");
		
		climber = new Solenoid(RobotMap.SOLENOID_CLIMBER);
		frontStage = new Solenoid(RobotMap.SOLENOID_CLIMBER_FRONT_STAGE);
		addChild("Climbing Cylinder", climber);
		addChild("Climber Front Stage", frontStage);
		
		safetySensor = addInput(RobotMap.SAFETY_SENSOR, "Safety Sensor");
	}
	
	private AnalogInput addInput(int id, String name) {
		if(id == -1) {
			return null;
		} else {
			AnalogInput analog = new AnalogInput(id);
			addChild(name, analog);
			return analog;
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void setFrontStageExtended(boolean extended) {
		frontStage.set(extended);
	}
	
	public void setClimberExtended(boolean extended) {
		climber.set(extended);
	}
	
	public boolean hasEnoughTimeToClimb() {
		return Robot.oi.getTimeRemaining() >= RobotMap.CLIMBER_EXTENSION_TIME;
	}
	
	public boolean isClimbing() {
		return climber.get();
	}
	
	public boolean isFrontStageClimbing() {
		return frontStage.get();
	}
	
	public String getStatus() {
		if(isClimbing()) {
			return "CLIMBING!";
		} else if(hasEnoughTimeToClimb()) {
			return "Ready (Safety range: " + getSafetySensorValue() + ")";
		} else {
			return "Not enough time!!";
		}
	}
	
	public int getSafetySensorValue() {
		if(safetySensor != null) {
			return safetySensor.getValue();
		} else {
			return Integer.MAX_VALUE;
		}
	}
	
	public boolean isSafetySensorTriggered() {
		return (getSafetySensorValue() < RobotMap.SAFETY_ENABLED_DISTANCE) && isFrontStageClimbing() && !Robot.limitSwitchesDisabled;
	}
}