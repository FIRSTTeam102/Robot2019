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

import org.team102.robots.robot2019.lib.RumbleSeries;

import edu.wpi.first.wpilibj.GenericHID;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * A {@link Command} to play a {@link RumbleSeries rumble} to a {@link GenericHID joystick}, and optionally wait for its completion.
 */
public class CommandPlayRumble extends TimedCommand {
	/** The {@link GenericHID joystick} to rumble */
	private GenericHID joystick;
	/** The {@link RumbleSeries rumble} to play */
	private RumbleSeries rumble;
	
	/**
	 * Creates the {@link Command}
	 * @param joystick The {@link GenericHID joystick} to rumble
	 * @param rumble The {@link RumbleSeries rumble} to play
	 * @param wait Whether or not to wait for the {@link RumbleSeries rumble} to finish playing
	 */
	public CommandPlayRumble(GenericHID joystick, RumbleSeries rumble, boolean wait) {
		super("Play rumble: " + rumble.hashCode() + "," + joystick.hashCode(), (wait ? rumble.getTotalTime() : 0));
		
		this.joystick = joystick;
		this.rumble = rumble;
	}
	
	public void initialize() { rumble.play(joystick); }
}