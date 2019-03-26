package org.team102.robots.robot2019.lib.commandsAndTrigger;

import org.team102.robots.robot2019.lib.VideoSelector;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CommandAdvanceVideoOutput extends InstantCommand {
	
	private VideoSelector selector;
	
	public CommandAdvanceVideoOutput(VideoSelector selector) {
		super("Advance DS Video Output");
		setRunWhenDisabled(true);
		
		this.selector = selector;
	}
	
	@Override
	public void initialize() {
		selector.changeToNextStream();
	}
}