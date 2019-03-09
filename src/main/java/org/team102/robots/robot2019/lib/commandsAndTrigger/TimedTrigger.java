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

import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A {@link Trigger} that only triggers when the other supplied trigger has been active for a certain amount of time
 */
public class TimedTrigger extends Trigger {
	
	private Command progressNotificationCommand;
	private Trigger parent;
	private double minTime;
	
	private long startTime = -1;
	
	/**
	 * Create the trigger
	 * @param parent The parent, which will be checked to see if it is active for long enough
	 * @param minTime How long the parent must remain active for this trigger to activate
	 */
	public TimedTrigger(Trigger parent, double minTime) {
		this.parent = parent;
		this.minTime = minTime;
	}
	
	@Override
	public boolean get() {
		if(parent.get()) {
			long now = System.nanoTime();
			
			if(startTime == -1) {
				startTime = now;
			}
			
			double delta = (now - startTime) / 1e9;
			boolean active = delta > minTime;
			
			if(!active && delta % .5 < .01) {
				notifyProgress();
			}
			
			return active;
		} else {
			startTime = -1;
			return false;
		}
	}
	
	private void notifyProgress() {
		if(progressNotificationCommand != null) {
			progressNotificationCommand.start();
		}
	}
	
	/**
	 * Adds a {@link Command} to be executed approximately every half-second while the timed trigger is in the process of being activated, or {@code null} for none.
	 * @param progressNotificationCommand The command
	 * @return Itself, for ease of construction
	 */
	public TimedTrigger withNotification(Command progressNotificationCommand) {
		this.progressNotificationCommand = progressNotificationCommand;
		return this;
	}
}