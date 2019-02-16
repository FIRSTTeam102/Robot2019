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
import edu.wpi.cscore.HttpCamera;

import edu.wpi.first.wpilibj.command.Subsystem;

public class SubsystemCameras extends Subsystem {
	
	public ArrayList<VideoSource> visibleVideoOutputs = new ArrayList<>();
	private VisionCameraHelper.Pipeline pipe;
	
	public SubsystemCameras() {
		super("Cameras");
		VisionCameraHelper.loadOpenCV();
		
		if(RobotMap.DEBUG_USE_LOCAL_PIPELINE) {
			pipe = new Pipe();
		}
		
		VideoSource visionCamera = VisionCameraHelper.openAndVerifyCamera(/*"Vision Camera"*/"Chassis Camera", RobotMap.CAMERA_ID_VISION, 480, 360, 15, 15, false);
		visibleVideoOutputs.add(visionCamera);
		
		if(pipe == null) {
			HttpCamera piVisionOutput = new HttpCamera("Vision Pi Output", RobotMap.CAMERA_URL_VISION_PI_OUTPUT, HttpCamera.HttpCameraKind.kMJPGStreamer);
			visibleVideoOutputs.add(piVisionOutput);
		} else {
			VideoSource pipelineOutput = VisionCameraHelper.startPipeline(visionCamera, 320, 240, "Vision Pipeline", false, pipe);
			pipe.unpause();
			visibleVideoOutputs.add(pipelineOutput);
		}
	}
	
	@Override protected void initDefaultCommand() {} // This is left intentionally empty
}