package org.team102.robots.robot2019.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemCentering extends Subsystem {
	
	public SubsystemCentering() {
		super("Centering");
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public double getFrontDSValue() {
		return 0;
	}
	
	public double getRearDSValue() {
		return 0;
	}
	
	public String getStatus() {
		return "";
	}
}