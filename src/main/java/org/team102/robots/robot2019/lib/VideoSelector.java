package org.team102.robots.robot2019.lib;

import java.util.List;
import java.util.Collections;
import java.util.Map;

import org.team102.robots.robot2019.lib.commandsAndTrigger.CommandSetVideoOutput;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class VideoSelector {
	public static final Map<String, Object> PROP_HIDE_LABELS = Collections.<String, Object>singletonMap("Label Position", "HIDDEN");
	
	private List<VideoSource> sources;
	private MjpegServer server;
	
	private ComplexWidget streamHolder;
	private ShuffleboardLayout selectorHolder;
	private NetworkTableEntry selectedStreamHolder;
	
	private int currSource = -1;
	
	public VideoSelector(List<VideoSource> sources, String name) {
		this(sources, CameraServer.getInstance().addServer(name));
	}
	
	public VideoSelector(List<VideoSource> sources, String name, int port) {
		this(sources, CameraServer.getInstance().addServer(name, port));
	}
	
	private VideoSelector(List<VideoSource> sources, MjpegServer server) {
		this.sources = sources;
		this.server = server;
		
		if(sources.size() == 0) {
			System.out.println("Warning: No video sources available for video selector " + server.getName());
		}
		
		changeToNextStream();
	}
	
	private String nameOf(VideoSource src) {
		if(src == null) {
			return "(none)";
		} else {
			return src.getName();
		}
	}
	
	public ComplexWidget getStreamHolder(ShuffleboardContainer holder) {
		if(streamHolder == null) {
			streamHolder = VisionCameraHelper.advertiseServerToShuffleboard(server, holder);
		}
		
		return streamHolder;
	}
	
	public ShuffleboardLayout getSelectorHolder(ShuffleboardContainer holder) {
		if(selectorHolder == null) {
			selectorHolder = holder.getLayout(server.getName() + " Streams", BuiltInLayouts.kList).withProperties(PROP_HIDE_LABELS);
			selectedStreamHolder = selectorHolder.add("Selected Stream", "").getEntry();
			updateSelectedStreamName();
			
			for(VideoSource src : sources) {
				selectorHolder.add(new CommandSetVideoOutput(this, nameOf(src))).withWidget(BuiltInWidgets.kCommand);
			}
		}
		
		return selectorHolder;
	}
	
	private void updateSelectedStreamName() {
		if(selectedStreamHolder != null) {
			selectedStreamHolder.setString("Selected stream: " + nameOf(sources.get(currSource)));
		}
	}
	
	public int findStream(String name) {
		for(int i = 0; i < sources.size(); i++) {
			if(name.equals(nameOf(sources.get(i)))) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void setStream(int id) {
		if(id < 0 || id >= sources.size()) {
			System.out.println("Warning: Tried to switch to a non-existant source!");
			return;
		}
		
		currSource = id;
		server.setSource(sources.get(id));
		updateSelectedStreamName();
	}
	
	public void setStreamByName(String name) {
		setStream(findStream(name));
	}
	
	public void changeToNextStream() {
		setStream((currSource + 1) % sources.size());
	}
}