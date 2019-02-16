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

package org.team102.robots.robot2019.subsystems;

import java.util.ArrayList;

import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemCameras extends Subsystem/*WithArduino*/ {
	
	public ArrayList<VideoSource> visibleVideoOutputs = new ArrayList<>();
	//private VisionCameraHelper.Pipeline pipe;
	
	public SubsystemCameras() {
		super("Cameras");//, RobotMap.LIGHTS_ARDUINO_WHOIS_RESPONSE, "Vision Light Control");
		VisionCameraHelper.loadOpenCV();
		
		//pipe = new Pipe();
		
		VideoSource visionCamera = VisionCameraHelper.openAndVerifyCamera(/*"Vision Camera"*/"Chassis Camera", RobotMap.CAMERA_ID_VISION, 480, 360, 15, 15, false);
		visibleVideoOutputs.add(visionCamera);
		
		/*VideoSource pipelineOutput = VisionCameraHelper.startPipeline(visionCamera, 320, 240, "Vision Pipeline", false, pipe);
		setPipelineActive(true);
		visibleVideoOutputs.add(pipelineOutput);*/
		
		VideoSource armCamera = VisionCameraHelper.openAndVerifyCamera("Arm Camera", RobotMap.CAMERA_ID_ARM, 480, 360, 15, 15, false);
		visibleVideoOutputs.add(armCamera);
	}
	
	@Override protected void initDefaultCommand() {} // This is left intentionally empty
	/*@Override protected void onArduinoLineReceived(String line) {} // This is left intentionally empty
	
	public void setLightBrightness(double brightness) {
		int rawBrightness = Math.min(Math.max((int)Math.round(brightness * 255), 0), 255);
		sendLineToArduino("LED:" + rawBrightness);
	}
	
	public void setLights(boolean on) {
		double brightness = 0;
		
		if(on) {
			brightness = RobotMap.CAMERA_LIGHT_BRIGHTNESS;
		}
		
		setLightBrightness(brightness);
	}
	
	public void setPipelineActive(boolean on) {
		if(pipe.isPaused() && on) {
			pipe.unpause();
		} else if(!pipe.isPaused() && !on) {
			pipe.pause();
		}
		
		setLights(on);
	}*/
}