/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;

import org.opencv.imgproc.Imgproc;

import org.opencv.core.*;

   /*JSON format:
   {
       "team": 102,
       "ntmode": <"client" or "server", "client" if unspecified>
       "cameras": [
           {
               "name": "rPi Camera 0"
               "path": "/dev/video0"
               "pixel format": <"MJPEG", "YUYV", etc>   // optional
               "width": 320		                // optional
               "height": 240		                // optional
               "fps": 30	                        // optional
               "brightness": 3    			// optional
               //"white balance": <"auto", "hold", value> // optional
               //"exposure": <"auto", "hold", value>      // optional
               //"properties": [{"name":"connect_verbose","value":1},{"name":"contrast","value":100},{"name":"saturation","value":50},{"name":"red_balance","value":1000},{"name":"blue_balance","value":1000},{"name":"horizontal_flip","value":false},{"name":"vertical_flip","value":false},{"name":"power_line_frequency","value":1},{"name":"sharpness","value":50},{"name":"color_effects","value":0},{"name":"rotate","value":0},{"name":"color_effects_cbcr","value":32896},{"name":"video_bitrate_mode","value":0},{"name":"video_bitrate","value":10000000},{"name":"repeat_sequence_header","value":false},{"name":"h264_i_frame_period","value":60},{"name":"h264_level","value":11},{"name":"h264_profile","value":4},{"name":"auto_exposure","value":0},{"name":"exposure_time_absolute","value":1000},{"name":"exposure_dynamic_framerate","value":false},{"name":"auto_exposure_bias","value":12},{"name":"white_balance_auto_preset","value":1},{"name":"image_stabilization","value":false},{"name":"iso_sensitivity","value":0},{"name":"iso_sensitivity_auto","value":1},{"name":"exposure_metering_mode","value":0},{"name":"scene_mode","value":0},{"name":"compression_quality","value":30}],
               "stream": {                              // optional
               //    "properties": [
               //        {
               //            "name": <stream property name>
               //            "value": <stream property value>
               //        }
               //    ]
               }
           }
       ]
   }*/




public final class piTesting {
  private static String configFile = "/boot/frc.json";

  @SuppressWarnings("MemberName")
  public static class CameraConfig {
    public String name;
    public String path;
    public JsonObject config;
    public JsonElement streamConfig;
  }

  public static int team;
  public static boolean server;
  public static List<CameraConfig> cameraConfigs = new ArrayList<>();

  //private Main() {
  //}

  /**
   * Report parse error.
   */
  public static void parseError(String str) {
    System.err.println("config error in '" + configFile + "': " + str);
  }

  /**
   * Read single camera configuration.
   */
  public static boolean readCameraConfig(JsonObject config) {
    CameraConfig cam = new CameraConfig();

    // name
    JsonElement nameElement = config.get("name");
    if (nameElement == null) {
      parseError("could not read camera name");
      return false;
    }
    cam.name = nameElement.getAsString();

    // path
    JsonElement pathElement = config.get("path");
    if (pathElement == null) {
      parseError("camera '" + cam.name + "': could not read path");
      return false;
    }
    cam.path = pathElement.getAsString();

    // stream properties
    cam.streamConfig = config.get("stream");

    cam.config = config;

    cameraConfigs.add(cam);
    return true;
  }

  /**
   * Read configuration file.
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public static boolean readConfig() {
    // parse file
    JsonElement top;
    try {
      top = new JsonParser().parse(Files.newBufferedReader(Paths.get(configFile)));
    } catch (IOException ex) {
      System.err.println("could not open '" + configFile + "': " + ex);
      return false;
    }

    // top level must be an object
    if (!top.isJsonObject()) {
      parseError("must be JSON object");
      return false;
    }
    JsonObject obj = top.getAsJsonObject();

    // team number
    JsonElement teamElement = obj.get("team");
    if (teamElement == null) {
      parseError("could not read team number");
      return false;
    }
    team = teamElement.getAsInt();

    // ntmode (optional)
    if (obj.has("ntmode")) {
      String str = obj.get("ntmode").getAsString();
      if ("client".equalsIgnoreCase(str)) {
        server = false;
      } else if ("server".equalsIgnoreCase(str)) {
        server = true;
      } else {
        parseError("could not understand ntmode value '" + str + "'");
      }
    }

    // cameras
    JsonElement camerasElement = obj.get("cameras");
    if (camerasElement == null) {
      parseError("could not read cameras");
      return false;
    }
    JsonArray cameras = camerasElement.getAsJsonArray();
    for (JsonElement camera : cameras) {
      if (!readCameraConfig(camera.getAsJsonObject())) {
        return false;
      }
    }

    return true;
  }

  /**
   * Start running the camera.
   */
  public static VideoSource startCamera(CameraConfig config, CameraServer inst) {
    System.out.println("Starting camera '" + config.name + "' on " + config.path);
    inst = CameraServer.getInstance();
    UsbCamera camera = new UsbCamera(config.name, config.path);
    MjpegServer server = inst.startAutomaticCapture(camera);
    
    
    Gson gson = new GsonBuilder().create();

    camera.setConfigJson(gson.toJson(config.config));
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);

    if (config.streamConfig != null) {
      server.setConfigJson(gson.toJson(config.streamConfig));
    }

    return camera;
  }
  

  /**
   * Example pipeline.
   */
  public static class MyPipeline implements VisionPipeline {

		private double[] rgbThresholdRed = {0.0, 250.0};
		private double[] rgbThresholdGreen = {75.0, 255.0};
		private double[] rgbThresholdBlue = {0.0, 250.0};
		
		private double[] rgbThresholdHue = {100.0, 140.0};
		private double[] rgbThresholdSaturation = {100.0, 255.0};
		private double[] rgbThresholdValue = {100.0, 255.0};

		private double blurRadius = 6.0;
		
		private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
		
		private ArrayList<MatOfPoint2f> convexHullsOutput = new ArrayList<MatOfPoint2f>();
		
		private Rect[] findCornersAndrewRects = new Rect[40];
		private RotatedRect[] findCornersRects = new RotatedRect[40];
		
		private RotatedRect[] sizeFilterRRects = new RotatedRect[20];
		private Rect[] sizeFilterRects = new Rect[20];
		
		private RotatedRect[][] rRectPairs = new RotatedRect[5][2];
		private Rect[][] rectPairs = new Rect[5][2];
		
		public void process(Mat mat) {
			
			//Mat mat1 = new Mat();
			System.out.println("Running pipeline");
			
			//Clear stuff
			findContoursOutput.clear();
			convexHullsOutput.clear();
			//Arrays.fill(findCornersAndrewRects, null);
			//Arrays.fill(findCornersRects, null);
			Arrays.fill(sizeFilterRRects, null);
			Arrays.fill(sizeFilterRects, null);
			for (int i=0; i<4; i++) {
				for (int j=0; j<2; j++) {
					rRectPairs[i][j] = null;
					rectPairs[i][j] = null;
				}
			}
			
			//Step rgbThresholdInput:
			rgbThreshold(mat, rgbThresholdHue, rgbThresholdSaturation, rgbThresholdValue, mat);

			// Step Blur:
			blur(mat, blurRadius, mat);

			// Step Find_Contours:
			findContours(mat, false, findContoursOutput);

			// Step Convex_Hulls:
			convexHulls(findContoursOutput, convexHullsOutput);
			
			//Find andrew and amanda corners
			//Imgproc.cvtColor(mat2, mat1, 8); //8 = GRAY2BGR
			Arrays.fill(findCornersRects, null);
			findCorners(mat, convexHullsOutput, findCornersRects, findCornersAndrewRects, mat);
			
			//Filter corners that are too small
			rRectSizeFilter(mat, findCornersRects, findCornersAndrewRects, 200, mat, sizeFilterRRects, sizeFilterRects);
			
			//return sizeFilterOutput;
			findPairs(mat, sizeFilterRRects, sizeFilterRects, mat, rRectPairs, rectPairs/*, coolPairs*/);
			
			if (rectPairs[0][0] == null) {
				System.out.println("No rectPairs!");
				//return mat3;
				//mat = mat3;
				//mat1 = mat;
			}
			if (rectPairs[1][0] == null) {
				drawFieldMarkers(mat, rRectPairs[0], mat);
				//return mat1;
				//mat = mat1;
				//mat1 = mat;
			}
			
			/*
			Mat closestOutput = new Mat();
			RotatedRect[] closestRPair = new RotatedRect[2];
			Rect[] closestPair = new Rect[2];
			closestPair(pairsOutput, rRectPairs, rectPairs, closestOutput, closestRPair, closestPair);
			drawFieldMarkers(closestOutput, closestRPair, fieldMarkerOutput);
			*/
			//System.out.println(rectPairs[1][0]);
			
			//return mat1;
			//mat = mat1;
			//mat1 = mat;
		}
		
		//IMG PROCESSING AND FILTERS START HERE
		/**
		 * Segment an image based on color ranges.
		 * @param input The image on which to perform the RGB threshold.
		 * @param red The min and max red.
		 * @param green The min and max green.
		 * @param blue The min and max blue.
		 * @param output The image in which to store the output.
		 */
		private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue, Mat out) {
			Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
			Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
				new Scalar(red[1], green[1], blue[1]), out);
			Imgproc.cvtColor(out, out, Imgproc.COLOR_HSV2RGB);
		}

		/**
		 * Softens an image using median filter blur.
		 * @param input The image on which to perform the blur.
		 * @param type The blurType to perform.
		 * @param doubleRadius The radius for the blur.
		 * @param output The image in which to store the output.
		 */
		private void blur(Mat input, double doubleRadius, Mat output) {
			int radius = (int)(doubleRadius + 0.5);
			int kernelSize = 2 * radius + 1;
			Imgproc.medianBlur(input, output, kernelSize);
		}

		/**
		 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
		 * @param input The image on which to perform the Distance Transform.
		 * @param type The Transform.
		 * @param maskSize the size of the mask.
		 * @param output The image in which to store the output.
		 */
		private void findContours(Mat input, boolean externalOnly,
			List<MatOfPoint> contours) {
			Mat hierarchy = new Mat();
			contours.clear();
			int mode;
			if (externalOnly) {
				mode = Imgproc.RETR_EXTERNAL;
			}
			else {
				mode = Imgproc.RETR_LIST;
			}
			int method = Imgproc.CHAIN_APPROX_SIMPLE;
			Imgproc.findContours(input, contours, hierarchy, mode, method);
		}

		/**
		 * Compute the convex hulls of contours.
		 * @param inputContours The contours on which to perform the operation.
		 * @param outputContours The contours where the output will be stored.
		 */
		private void convexHulls(List<MatOfPoint> inputContours, ArrayList<MatOfPoint2f> outputContours) {
			final MatOfInt hull = new MatOfInt();
			outputContours.clear();
			for (int i = 0; i < inputContours.size(); i++) {
				final MatOfPoint contour = inputContours.get(i);
				final MatOfPoint2f mopHull = new MatOfPoint2f();
				Imgproc.convexHull(contour, hull);
				mopHull.create((int) hull.size().height, 1, CvType.CV_32FC2);
				for (int j = 0; j < hull.size().height; j++) {
					int index = (int) hull.get(j, 0)[0];
					double[] point = new double[] {contour.get(index, 0)[0], contour.get(index, 0)[1]};
					mopHull.put(j, 0, point);
				}
				outputContours.add(mopHull);
			}
		}
		
		private void findAndrewCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, Rect[] rects, Mat output) {
			input.copyTo(output);
			Rect rect = new Rect();
			int n = 0;
			Point[][] corners = new Point[50][4];
			//final Scalar circleColor = new Scalar(255, 0, 0);
			for(int i=0; i< inputContours.size(); i++) { //For every contour
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 && n < 20){
	            	rect = Imgproc.boundingRect(inputContours.get(n)); //Find the rectangle with the corners
	            	rects[n] = rect;
					corners[n][0] = new Point(rect.x, rect.y); //Define the corners
					corners[n][1] = new Point(rect.x + rect.width, rect.y);
					corners[n][2] = new Point(rect.x + rect.width, rect.y + rect.height);
					corners[n][3] = new Point(rect.x, rect.y + rect.height);
					/*Imgproc.circle(output, new Point(rect.x, rect.y), 5, circleColor); //Circle the corners in the image
					Imgproc.circle(output, new Point(rect.x + rect.width, rect.y), 5, circleColor);
					Imgproc.circle(output, new Point(rect.x + rect.width, rect.y + rect.height), 5, circleColor);
					Imgproc.circle(output, new Point(rect.x, rect.y + rect.height), 5, circleColor);*/
		        	for (int j = 0; j < 4; j++){  
		        		//System.out.printf("%d %d %d %d\n", vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y);
		            		Imgproc.line(output, corners[n][j], corners[n][(j+1)%4], new Scalar(0,255,0,255), 3);
		        	}
	            		n++;
	            	}
			}
		}	
		
		private void findAmandaCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, RotatedRect[] rRectArray, Mat output) { 
			Mat cornerPoints = new Mat();
			Point[] vertices = new Point[4]; 
			int found = 0;
			input.copyTo(output);
			RotatedRect rRect = null;
			//System.out.printf("size: %d\n", inputContours.size());
			for (int i = 0; i < inputContours.size(); i++) {
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 && found < 20){
	            	rRect = Imgproc.minAreaRect(inputContours.get(i));
	            	rRectArray[i] = rRect; //creates array to pass into tape finding stuff
					Imgproc.boxPoints(rRect, cornerPoints);
		        	rRect.points(vertices);
		        	//angles[i] = rRect.angle;
		        	for (int j = 0; j < 4; j++){  
		            	Imgproc.line(output, vertices[j], vertices[(j+1)%4], new Scalar(150,150,255,255), 3);
		            }
		        	found++;
	            }
			}
		}
		
		private void findCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, RotatedRect[] rRectArray, Rect[] rectArray, Mat output) {
			input.copyTo(output);
			Mat andrewOutput = new Mat();
			findAndrewCorners(input, inputContours, rectArray, andrewOutput);
			findAmandaCorners(andrewOutput, inputContours, rRectArray, output);
		}
		
		//Takes all corner shapes removes all that are below a certain perimeter
		private void rRectSizeFilter(Mat inputImage, RotatedRect[] input, Rect[] andrewInput, int minimum, Mat outputImage, RotatedRect[] output, Rect[] andrewOutput) {
			Point[] corners = new Point[4];
			int n = 0;
			inputImage.copyTo(outputImage);
			for (int i = 0; i < input.length && input[i] != null; i++) {
				input[i].points(corners);
				if (getPerimeter(corners) >= minimum && n < 10) {
					output[n] = input[i];
					andrewOutput[n] = andrewInput[i];
					output[n].points(corners);
					for (int j = 0; j < 4; j++) {
						Imgproc.line(outputImage, corners[j], corners[(j+1)%4], new Scalar(0,0,255,255), 5);
					}
					n++;
				}
			}
		}
		
		//Takes in rRects and finds pairs of rRects
		private void findPairs(Mat inputImage, RotatedRect[] input, Rect[] andrewInput, Mat outputImage, RotatedRect[][] output, Rect[][] andrewOutput/*, Mat coolOutput*/) {
			int n = 0;
			Point[] corners = new Point[4];
			inputImage.copyTo(outputImage);
			for (int i = 1; i < input.length && input[i] != null; i++) {
				for (int j = i - 1; j >= 0; j--) {
					//I love this next line of code its so elegant
					if (n < 4 && withinRange(input[i].center.y, input[j].center.y, 25.0) /*&& withinRange(input[i].angle + input[j].angle, 90.0, 10.0)*/ && withinRange(andrewInput[i].height, andrewInput[j].height, 150.0)) {
						//Ain't that just beautiful? Anyways, back to your regularly scheduled programming
						//System.out.println(input[i].angle + input[j].angle);
						output[n][0] = input[i];
						output[n][1] = input[j];
						andrewOutput[n][0] = andrewInput[i];
						andrewOutput[n][1] = andrewInput[j];
						n++;
						for (int x = 0; x < 4; x++) {
							input[i].points(corners);
							Imgproc.line(outputImage, corners[x], corners[(x+1)%4], new Scalar(255,0,0,255), 7);
							//Imgproc.line(coolOutput, corners[x], corners[(x+1)%4], new Scalar(255,0,0,255), 7);
							input[j].points(corners);
							Imgproc.line(outputImage, corners[x], corners[(x+1)%4], new Scalar(255,0,0,255), 7);
							//Imgproc.line(coolOutput, corners[x], corners[(x+1)%4], new Scalar(255,0,0,255), 7);
						}
					}
				}
			}
		}
		
		
		//Takes in rRect pairs and returns one rRect pair
		private void closestPair (Mat inputImage, RotatedRect[][] input, Rect[][] andrewInput, Mat outputImage, RotatedRect[] output, Rect[] andrewOutput) {
			double maxheight = 0.0;
			int best = 0; //
			Point[] corners = new Point[4];
			inputImage.copyTo(outputImage);
			for (int i = 0; i < input.length && input[i] != null; i++) {
				if ((andrewInput[i][0].height + andrewInput[i][1].height) / 2 > maxheight)//finds size of 90 degree rect
					best = i; //bigger is better!
			}
			for (int i = 0; i < 2; i++) {
				output[i] = input[best][i]; //takes best pair and makes output[1 or 0] best pair[1 or 0]
				andrewOutput[i] = andrewInput[best][i]; 
				output[i].points(corners);
				//draws two best
				for (int j = 0; j < 4; j++) {
					Imgproc.line(outputImage, corners[j], corners[(j+1)%4], new Scalar(0,100,255,255), 10);
				}
			}
		}
		
		
		private boolean withinRange(double one, double two, double range) {
			return (Math.abs(one - two) <= range);
		}
		
		private double getPerimeter(Point[] pt) {
			return getDifference(pt[0], pt[1]) + getDifference(pt[1], pt[2]) + getDifference(pt[2], pt[3]) + getDifference(pt[3], pt[0]);
		}
	
		//to find distance between two Points created above
		private double getDifference(Point a, Point b) {
			double xDiff = a.x-b.x;
			double yDiff = a.y-b.y;
			double distance = Math.sqrt( (yDiff*yDiff)+(xDiff*xDiff));
			return distance;
		}		
		/*private double getAngle() {
			
		}*/
		
		//measures whether robot is directly in front of tape marks
		//this is a public method because we should probably make the robot move and adjust elsewhere
		//just checking if this is true as it moves
		/*private boolean isStraight(Rect[] input) {	
			
			//passes in andrew rect
			double left = getDifference(, tapeTargets[0][1]);
			double right = getDifference(tapeTargets[1][0], tapeTargets[1][1]);
			
			//0.1 is a placeholder for now, we can decide how much room we need
			if(Math.abs(right-left)<tolerance) {
				return true;
			}
			else {
				return false;
			}
		}*/

		public void drawFieldMarkers(Mat input, RotatedRect[] pairs, Mat output) {
			input.copyTo(output);
			double midpoint = (pairs[0].center.x + pairs[1].center.x)/2;
			Scalar target = new Scalar(255,125,100,255);
			Point[][] corners = new Point[2][4];
			Point[] topLefts = new Point[2];
			Point[] topRights = new Point[2];
			topLefts[0] = corners[0][0];
			topLefts[1] = corners[1][0];
			pairs[0].points(corners[0]);
			pairs[1].points(corners[1]);
			for (int i=0; i<2; i++) {
				for (int j=1; j<4; j++) {
					if (corners[i][j].x > pairs[i].center.x && corners[i][j].y < pairs[i].center.y) {
						topRights[i] = corners[i][j];
						break;
					}
				}
			}
			for (int i=0; i<2; i++) {
				for (int j=1; j<4; j++) {
					if (corners[i][j].x < pairs[i].center.x && corners[i][j].y < pairs[i].center.y) {
						topLefts[i] = corners[i][j];
						break;
					}
				}
			}
			double xScale = Math.abs(topRights[0].x-topLefts[1].x)/8;
			double yScale = ((Math.abs(topRights[0].y-topLefts[0].y)*2) + (Math.abs(topRights[1].y-topLefts[1].y)*2)) / 2;
			Point center = new Point(midpoint, topRights[0].y-(4.7*yScale)-(8.25*yScale));
			Point rectTopRight = new Point(midpoint+(8.25*xScale), topRights[0].y-(4.7*yScale));
			Point rectBotLeft = new Point(midpoint-(8.25*xScale), topRights[0].y-(4.7*yScale)-(16.5*yScale));
			Size size = new Size(rectTopRight.x-rectBotLeft.x, rectTopRight.y-rectBotLeft.y);
			RotatedRect outline = new RotatedRect(center, size, 0);
			Scalar circle = new Scalar(255,175,150,255);
			Imgproc.ellipse(output, outline, circle);
			Imgproc.line(output, new Point(midpoint,0), new Point(midpoint,360), target, 5);
		}
	}

  /**
   * Main.
   */
  public static void main(String... args) {
    if (args.length > 0) {
      configFile = args[0];
    }

    // read configuration
    if (!readConfig()) {
      return;
    }

    // start NetworkTables
    NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
    if (server) {
      System.out.println("Setting up NetworkTables server");
      ntinst.startServer();
    } else {
      System.out.println("Setting up NetworkTables client for team " + team);
      ntinst.startClientTeam(team);
    }

    // start cameras
    List<VideoSource> cameras = new ArrayList<>();
    CameraServer inst;
    for (CameraConfig cameraConfig : cameraConfigs) {
      cameras.add(startCamera(cameraConfig, inst));
    }
    
    CvSink cvSink = inst.getVideo();
    CvSource outputStream = CameraServer.getInstance().putVideo("Camera", 160, 120);

    Mat source = new Mat();
    Mat output = new Mat();

	MyPipeline pipe = new MyPipeline();
    // start image processing on camera 0 if present
    if (cameras.size() >= 1) {
      VisionThread visionThread = new VisionThread(cameras.get(0),
    		new MyPipeline(), pipeline -> {
    			cvSink.grabFrame(source);
    			pipeline.process(source);
	            outputStream.putFrame(source);
      });
      visionThread.start();
    }

    // loop forever
    while (true) {
      try {
    	Thread.sleep(10000);

      } catch (InterruptedException ex) {
        return;
      }
    }
  }
}