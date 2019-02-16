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


package org.team102.robots.robot2019.lib.commandsAndTrigger;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * A trigger that implements a logical operator ({@code not}, {@code and}, {@code nand}, {@code or}, {@code nor}, {@code xor}, {@code xnor}) on one or more other triggers
 */
public class LogicGateTrigger extends Trigger {
	
	private Supplier<Boolean> getImpl;
	
	private LogicGateTrigger(Supplier<Boolean> getImpl) {
		this.getImpl = getImpl;
	}
	
	/**
	 * Returns the logical {@code not} of the given trigger
	 * @param trig The trigger
	 * @return The {@code not} of that trigger
	 */
	public static LogicGateTrigger not(Trigger trig) {
		return new LogicGateTrigger(() -> !trig.get());
	}
	
	/**
	 * Returns the logical {@code and} of the given triggers
	 * @param triggers The triggers
	 * @return The {@code and} of those triggers
	 */
	public static LogicGateTrigger and(Trigger... triggers) {
		return new LogicGateTrigger(() -> {
			for(Trigger trig : triggers) {
				if(!trig.get()) {
					return false;
				}
			}
			
			return true;
		});
	}
	
	/**
	 * Returns the logical {@code or} of the given triggers
	 * @param triggers The triggers
	 * @return The {@code or} of those triggers
	 */
	public static LogicGateTrigger or(Trigger... triggers) {
		return new LogicGateTrigger(() -> {
			for(Trigger trig : triggers) {
				if(trig.get()) {
					return true;
				}
			}
			
			return false;
		});
	}
	
	/**
	 * Returns the logical {@code xor} of the given triggers
	 * @param trig1 The first trigger
	 * @param trig2 The second trigger
	 * @return The {@code xor} of those triggers
	 */
	public static LogicGateTrigger xor(Trigger trig1, Trigger trig2) {
		return new LogicGateTrigger(() -> trig1.get() != trig2.get());
	}
	
	/**
	 * Returns the logical {@code nand} of the given triggers
	 * @param triggers The triggers
	 * @return The {@code nand} of those triggers
	 */
	public static LogicGateTrigger nand(Trigger... triggers) {
		return not(and(triggers));
	}
	
	/**
	 * Returns the logical {@code nor} of the given triggers
	 * @param triggers The triggers
	 * @return The {@code nor} of those triggers
	 */
	public static LogicGateTrigger nor(Trigger... triggers) {
		return not(or(triggers));
	}

	/**
	 * Returns the logical {@code xnor} of the given triggers
	 * @param trig1 The first trigger
	 * @param trig2 The second trigger
	 * @return The {@code xnor} of those triggers
	 */
	public static LogicGateTrigger xnor(Trigger trig1, Trigger trig2) {
		return not(xor(trig1, trig2));
	}
	
	@Override
	public boolean get() {
		return getImpl.get();
	}
}