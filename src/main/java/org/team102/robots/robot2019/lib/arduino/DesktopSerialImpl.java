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

import com.fazecast.jSerialComm.SerialPort;

import edu.wpi.first.wpilibj.Timer;

public class DesktopSerialImpl implements ArduinoConnection.SerialImpl {
	
	private SerialPort port;
	
	public DesktopSerialImpl(String portID) {
		port = SerialPort.getCommPort(portID);
		port.setBaudRate(9600);
		port.setParity(SerialPort.NO_PARITY);
		port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
		port.setNumStopBits(1);
		port.setNumDataBits(8);
		port.openPort();
	}
	
	@Override
	public boolean isOpen() {
		return port.isOpen();
	}
	
	@Override
	public String getName() {
		return port.getSystemPortName();
	}
	
	@Override
	public int bytesAvailable() {
		return port.bytesAvailable();
	}
	
	@Override
	public void write(byte[] data) {
		port.writeBytes(data, data.length);
		
		while(port.bytesAwaitingWrite() > 0) {
			Timer.delay(.01);
		}
	}
	
	@Override
	public byte[] read(int len) {
		byte[] buff = new byte[len];
		port.readBytes(buff, len);
		
		return buff;
	}
	
	@Override
	public void close() {
		port.closePort();
	}
}