package org.team102.robots.robot2019.lib;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoConnection implements Closeable {
	public static final String EOL = "\n";
	
	private SerialPort port;
	private String dataBuff = "";
	
	private ArrayList<String> lines = new ArrayList<>();
	private Consumer<String> lineListener = null;
	
	public ArduinoConnection(String portID) {
		try {
			port = SerialPort.getCommPort(portID);
			port.setBaudRate(9600);
			port.setParity(SerialPort.NO_PARITY);
			port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
			port.setNumStopBits(1);
			port.setNumDataBits(8);
			port.openPort();
		} catch(Exception e) {
			if(port.isOpen()) {
				System.out.println("Something wrong may have happened whilst opening serial port " + portID + ", but if everything still works fine, it shouldn't matter");
			} else {
				throw new IllegalStateException("Failed to open port", e);
			}
		}
	}
	
	@Override
	public void close() {
		port.closePort();
	}
	
	public void setLineListener(Consumer<String> lineListener) {
		this.lineListener = lineListener;
	}
	
	public void write(String line) {
		byte[] buff = (line + EOL).getBytes();
		port.writeBytes(buff, buff.length);
	}
	
	public void update() {
		int bytesAvailable = port.bytesAvailable();
		
		if(bytesAvailable > 0) {
			byte[] buff = new byte[bytesAvailable];
			port.readBytes(buff, bytesAvailable);
			
			dataBuff += new String(buff);
		}
		
		if(dataBuff.contains(EOL)) {
			boolean useLastToken = dataBuff.endsWith(EOL);
			String[] tokens = dataBuff.split(EOL);
			int max = tokens.length - 1;
			
			if(useLastToken) {
				dataBuff = "";
			} else {
				dataBuff = tokens[max];
				max--;
			}
			
			for(int i = 0; i <= max; i++) {
				String tok = tokens[i].trim();
				
				if(tok.length() > 0) {
					if(lineListener == null) {
						lines.add(tok);
					} else {
						lineListener.accept(tok);
					}
				}
			}
		}
	}
}