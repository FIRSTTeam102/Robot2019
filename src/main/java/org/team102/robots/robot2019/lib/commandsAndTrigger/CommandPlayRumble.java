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