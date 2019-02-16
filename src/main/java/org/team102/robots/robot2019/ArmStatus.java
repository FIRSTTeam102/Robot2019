package org.team102.robots.robot2019;

public class ArmStatus {
	
	public final String name;
	
	public final boolean isExtended;
	public final int elbowSetpoint, wristSetpoint;
	
	public final boolean isHatchEjectorExtended;
	
	public final double cargoRollerActiveTime;
	public final boolean isCargoRollerReverse;
	
	public ArmStatus(String name, boolean isExtended, int elbowSetpoint, int wristSetpoint, boolean isHatchEjectorExtended, double cargoRollerActiveTime, boolean isCargoRollerReverse) {
		this.name = name;
		
		this.isExtended = isExtended;
		this.elbowSetpoint = elbowSetpoint;
		this.wristSetpoint = wristSetpoint;
		
		this.isHatchEjectorExtended = isHatchEjectorExtended;
		
		this.cargoRollerActiveTime = cargoRollerActiveTime;
		this.isCargoRollerReverse = isCargoRollerReverse;
	}
	
	public String toString() {
		return "{ Elbow: " + elbowSetpoint + ", Wrist: " + wristSetpoint + " }";
	}
}