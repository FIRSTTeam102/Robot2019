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

package org.team102.robots.robot2019.lib.arduino;

import edu.wpi.first.wpilibj.SerialPort;

public class RioSerialImpl implements ArduinoConnection.SerialImpl {
	
	private SerialPort port;
	private boolean open;
	private SerialPort.Port portType;
	
	private static SerialPort.Port portIDToPortType(String id) {
		if(id.endsWith("0")) {
			return SerialPort.Port.kUSB;
		} else if(id.endsWith("1")) {
			return SerialPort.Port.kUSB1;
		} else if(id.endsWith("2")) {
			return SerialPort.Port.kUSB2;
		} else {
			throw new IllegalArgumentException("Unknown or unsupported port " + id);
		}
	}
	
	public RioSerialImpl(String portID) {
		this(portIDToPortType(portID));
	}
	
	public RioSerialImpl(SerialPort.Port portType) {
		port = new SerialPort(9600, portType, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
		port.setFlowControl(SerialPort.FlowControl.kNone);
		
		this.portType = portType;
	}
	
	@Override
	public boolean isOpen() {
		return open;
	}
	
	@Override
	public String getName() {
		return portType.name();
	}
	
	@Override
	public int bytesAvailable() {
		return port.getBytesReceived();
	}
	
	@Override
	public void write(byte[] data) {
		port.write(data, data.length);
	}
	
	@Override
	public byte[] read(int len) {
		return port.read(len);
	}
	
	@Override
	public void close() {
		port.close();
		open = false;
	}
}