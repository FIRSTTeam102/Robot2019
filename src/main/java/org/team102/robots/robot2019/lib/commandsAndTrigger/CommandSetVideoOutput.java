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

import org.team102.robots.robot2019.lib.VideoSelector;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandSetVideoOutput extends InstantCommand {
	
	private VideoSelector selector;
	private String streamName;
	
	public CommandSetVideoOutput(VideoSelector selector, String streamName) {
		super(streamName);
		setRunWhenDisabled(true);
		
		this.selector = selector;
		this.streamName = streamName;
	}
	
	protected void initialize() {
		selector.setStreamByName(streamName);
	}
}