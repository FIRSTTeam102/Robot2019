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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.opencv.core.*;
import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.opencv.imgproc.Imgproc;

public class SubsystemCameras extends Subsystem {
	
	public ArrayList<VideoSource> visibleVideoOutputs = new ArrayList<>();
	
	public SubsystemCameras() {
		super("Cameras");
		
		VideoSource visionCamera = VisionCameraHelper.openAndVerifyCamera("Vision Camera", RobotMap.CAMERA_ID_VISION, 480, 360, 15, 1, false);
		visibleVideoOutputs.add(visionCamera);
		
		VideoSource pipelineOutput = VisionCameraHelper.startPipeline(visionCamera, 320, 240, "Vision Pipeline", false, new Pipe());
		visibleVideoOutputs.add(pipelineOutput);
	}
	
	protected void initDefaultCommand() {}
	
	private class Pipe extends VisionCameraHelper.Pipeline {
		
		private Mat rgbThresholdOutput = new Mat();
		private Mat blurOutput = new Mat();
		private Mat cutToContourOutput = new Mat();
		private Mat isolateTapeOutput = new Mat();
		private Mat findCornersOutput = new Mat();
		private Mat cornersInput = new Mat();
		private Mat closestInput = new Mat();
		private Mat closestOutput = new Mat();
		private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
		private ArrayList<MatOfPoint2f> convexHullsOutput = new ArrayList<MatOfPoint2f>();
		private ArrayList<MatOfPoint2f> amandaCornersInputCnts = new ArrayList<MatOfPoint2f>();
		
		private Rect[] findCornersAndrewRects = new Rect[100];
		private RotatedRect[] findCornersRects = new RotatedRect[100];
		private RotatedRect leftTape = new RotatedRect();
		private RotatedRect rightTape = new RotatedRect();
		//Point[rectangle] leftmost=first [point 1-4] start top left- listed clockwise
		
		//Point[rectangle] leftmost=first [point 1-4] start top left- listed clockwise
		private Point[][] andrewCornersOutput = new Point[100][4];
		private Point[][] amandaCornersOutput = new Point[100][4];
		//private ArrayList<Point> andrewCornersOutput = new ArrayList<Point>();
		//private Point[][] andrewCornersOutput = new Point[20][4];
		private Mat andrewCornersImageOutput = new Mat();
		
		//point[rectangle][corner] narrows down after getting corners output to ONLY tape corners, 3 rectangles max and 4 corners
		private int tapeMarkNumber = 3;
		private Point[][] tapeMarks = new Point[tapeMarkNumber][4];
		
		//Point[rectangle][corner], once two pieces of tape are narrowed down, used to direct robot!! 
		Point[][] tapeTargets = new Point[2][4];
		
		//pixel size tolerance, 0.1 right now but will change
		//its about as much tolerance as ken has for us ;)
		double tolerance = 100;
		
		
		
		private int threshold = 200;
		
		//if Harris Corners is a thing we gon use
		//private Mat harrisOutput = new Mat();
	    
	    
		public Mat process(Mat input) {
			//Step rgbThresholdInput:
			Mat rgbThresholdInput = input;
			double[] rgbThresholdRed = {0.0, 150.0};
			double[] rgbThresholdGreen = {100.0, 255.0};
			double[] rgbThresholdBlue = {0.0, 150.0};
			rgbThreshold(rgbThresholdInput, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);

			// Step Blur:
			Mat blurInput = rgbThresholdOutput;
			double blurRadius = 6.006007151560739;
			blur(blurInput, blurRadius, blurOutput);

			// Step Find_Contours:
			Mat findContoursInput = blurOutput;
			boolean findContoursExternalOnly = false;
			findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

			// Step Convex_Hulls:
			ArrayList<MatOfPoint> convexHullsContours = findContoursOutput;
			convexHulls(convexHullsContours, convexHullsOutput);
			
			//Cut image to convex hulls
			//cutToContour(input, convexHullsOutput, cutToContourOutput);
			
			//Find andrew and amanda corners
			Imgproc.cvtColor(blurOutput, cornersInput, 8); //8 = GRAY2BGR
			Arrays.fill(findCornersRects, null);
			findCorners(input, convexHullsOutput, andrewCornersOutput, amandaCornersOutput, findCornersRects, findCornersAndrewRects, findCornersOutput);
			for (int i = 0; i < findCornersRects.length && findCornersRects[i] != null; i++) {
				System.out.println(findCornersRects[i].center);
			}
			//return findCornersOutput;
			
			//Filter corners that are too small
			Mat sizeFilterOutput = new Mat();
			RotatedRect[] sizeFilterRRects = new RotatedRect[100];
			Rect[] sizeFilterRects = new Rect[100];
			rRectSizeFilter(findCornersOutput, findCornersRects, findCornersAndrewRects, 200, sizeFilterOutput, sizeFilterRRects, sizeFilterRects);
			
			return sizeFilterOutput;
			
			/*//Find closest tape and its pair
			Hopefully we never see this again (unless you can get it to work)
			
			closestInput = findCornersOutput;
			findClosestTape(closestInput, findCornersRects, leftTape, rightTape, closestOutput);
			
			return closestOutput;*/
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
		private void rgbThreshold(Mat input, double[] red, double[] green, double[] blue,
			Mat out) {
			Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2RGB);
			Core.inRange(out, new Scalar(red[0], green[0], blue[0]),
				new Scalar(red[1], green[1], blue[1]), out);
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
		
		private void cutToContour(Mat input, ArrayList<MatOfPoint> inputContours, Mat output) {
			for(int i=0; i< inputContours.size();i++){
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 ){
	                Rect rect = Imgproc.boundingRect(inputContours.get(i));
	                if (rect.height > 28){
	                    Imgproc.rectangle(input, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
	                    output = input.submat(rect.x, rect.x + rect.height, rect.x, rect.x + rect.width);
	                }
	            }
	        }
	    }
		private void findAndrewCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, Point[][] corners, Rect[] rects, Mat output) {
			input.copyTo(output);
			Rect rect = new Rect();
			int n = 0;
			//Point[][] corners = new Point[20][4];
			final Scalar circleColor = new Scalar(255, 0, 0);
			for(int i=0; i< inputContours.size(); i++) { //For every contour
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 ){
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
		
		private void findAmandaCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, Point[][] corners, RotatedRect[] rRectArray, Mat output) { 
			Mat cornerPoints = new Mat();
			Point[] vertices = new Point[4]; 
			
			input.copyTo(output);
			RotatedRect rRect = null;
			//System.out.printf("size: %d\n", inputContours.size());
			for (int i = 0; i < inputContours.size(); i++) {
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 ){
	            	rRect = Imgproc.minAreaRect(inputContours.get(i));
	            	rRectArray[i] = rRect; //creates array to pass into tape finding stuff
					Imgproc.boxPoints(rRect, cornerPoints);
		        	rRect.points(vertices);
		        	//angles[i] = rRect.angle;
		        	for (int j = 0; j < 4; j++){  
		            	Imgproc.line(output, vertices[j], vertices[(j+1)%4], new Scalar(150,150,255,255), 3);
		            	corners[i][j] = vertices[j];
		            }
	            }
			}
		}
		
		private void findCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, Point[][] andrewCorners, Point[][] amandaCorners, RotatedRect[] rRectArray, Rect[] rectArray, Mat output) {
			input.copyTo(output);
			Mat andrewOutput = new Mat();
			findAndrewCorners(input, inputContours, andrewCorners, rectArray, andrewOutput);
			findAmandaCorners(andrewOutput, inputContours, amandaCorners, rRectArray, output);
		}
		
		//isolate tape first with the in line stuff and then use this to determine left or right
		private void findClosestTape(Mat input, RotatedRect[] rRects, RotatedRect targetLeft, RotatedRect targetRight, Mat output) {
			int size;
			input.copyTo(output);
			//System.out.println("Ran correctly!");
			for (size=0; size < rRects.length; size++) {
				if (rRects[size] == null)
					break;
			}
			System.out.println(size);
			if (size < 2) {
				System.out.println("Insufficient (<2) rrects found! Stopping findClosestTape...");
				return;
			}
			if (size > 10) {
				System.out.println("Too many (>10) rrects found - could crash code! Stopping findClosestTape...");
				return;
			}
			Point[] vertices = new Point[4];
			//finds largest rotated rectangle, or the one that you are closest to
			double maxHeight = rRects[0].size.height;
			RotatedRect closestRect = null;
			for(int i=1; i<size; i++) {	
				if(rRects[i].size.height > maxHeight) {
					maxHeight = rRects[i].size.height;
					closestRect = rRects[i];
				}
				System.out.println(maxHeight);
			}
			
			//figures out if you're seeing a right tape or a left tape
			if (closestRect.angle<45) {
				targetLeft = closestRect;
				//targetRight=rRects[size-1];
				//cycles through until it finds a rotated rect to the right of original
				/*for(int i=0; i<size; i++) {
					if(rRects[i].center.x>targetLeft.center.x) {
						targetRight= rRects[i];
						break; //Breaks after finds one directly to the right
					}
				}*/
			}
			else {
				targetRight = closestRect;
				/*targetLeft = rRects[0];
				for(int i=size - 1; i>0; i--) {
					if(rRects[i].center.x<targetRight.center.x) {
						targetLeft= rRects[i];
						break; //Breaks after finds one directly to the left
					}
				}*/
			}
			for (int i=0; i<4; i++) { //Draws the best 2 amanda rectangles as blue
				/*targetLeft.points(vertices);
            	Imgproc.line(output, vertices[i], vertices[(i+1)%4], new Scalar(255,0,0,255), 5);
				targetRight.points(vertices);
            	Imgproc.line(output, vertices[i], vertices[(i+1)%4], new Scalar(255,0,0,255), 5);*/
			}
			//we could adjust so that the target rect right or left is in the right location and then look for
			//the one next to it which would now be either
		}
		
		//uses midpoints of tape to see if they are in line, if they are in line, it is likely they are tape
		
		private int findTape(Point corners[][]) {
			double centerHeights[] = new double[20];
			int midpointsFound = 0;
			int cornersFound = 0;
			int[] validCorners = new int[20];
			for (int i = 0; i < 20; i++)
				validCorners[i] = 0;
			int x = 0;
			for (int i = 0; i < 0; i++) {
				if (andrewCornersOutput[i][0] != null)
					cornersFound++;
			}
			//finds center heights on rectangles left to right and then compares
			for (int i = 0; i < cornersFound; i++) {
				//finds midpoint of y leg of all rectangles
				centerHeights[i] = (andrewCornersOutput[i][0].y + andrewCornersOutput[i][2].y) / 2;
				midpointsFound++;
			}
			for (int i = 1; i < midpointsFound; i++) {
				for (int j = 0; j < i; j++) {
					//sees if centers of rectangles are approximately same height
					if (Math.abs(centerHeights[i]-centerHeights[j])<tolerance) {
						if (validCorners[i] == 0) {
							tapeMarks[i][x]= andrewCornersOutput[i][x];
							x++;
							validCorners[i] = 1;
						}
						if (validCorners[j] == 0) {
							tapeMarks[j][x]= andrewCornersOutput[j][x];
							x++;
							validCorners[j] = 1;
						}
					}
					//System.out.println(andrewCornersOutput[i][0]);
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(tapeMarks[0][0]);
				}
			}
			return x;
		}
		
		//Takes all corner shapes removes all that are below a certain perimeter
		private void rRectSizeFilter(Mat inputImage, RotatedRect[] input, Rect[] andrewInput, int minimum, Mat outputImage, RotatedRect[] output, Rect[] andrewOutput) {
			Point[] corners = new Point[4];
			int n = 0;
			inputImage.copyTo(outputImage);
			for (int i = 0; i < input.length; i++) {
				input[i].points(corners);
				if (getPerimeter(corners) >= minimum) {
					output[n] = input[n];
					andrewOutput[n] = andrewInput[n];
					output[n].points(corners);
					for (int j = 0; j < 4; j++) {
						Imgproc.line(outputImage, corners[j], corners[(j+1)%4], new Scalar(0,0,255,255), 5);
					}
					n++;
				}
			}
		}
		
		//Takes in rRects and finds pairs of rRects
		private void findPairs(Mat inputImage, RotatedRect[] input, Rect[] andrewInput, Mat outputImage, RotatedRect[][] output, Rect[][] andrewOutput) {
			int n = 0;
			inputImage.copyTo(outputImage);
			Point[] corners = new Point[4];
			for (int i = 1; i < input.length; i++) {
				for (int j = i - 1; j >= 0; j--) {
					//I love this next line of code its so elegant
					if (withinRange(input[i].center.y, input[j].center.y, 25.0) && withinRange(input[i].angle + input[j].angle, 90.0, 5.0) && withinRange(andrewInput[i].height, andrewInput[j].height, 50.0)) {
						//Ain't that just beautiful? Anyways, back to your regularly scheduled programming
						output[n][0] = input[i];
						output[n][1] = input[j];
						andrewOutput[n][0] = andrewInput[i];
						andrewOutput[n][1] = andrewInput[j];
						n++;
						for (int x = 0; x < 4; x++) {
							input[i].points(corners);
							Imgproc.line(outputImage, corners[x], corners[(x+1)%4], new Scalar(255,0,0,255), 7);
							input[j].points(corners);
							Imgproc.line(outputImage, corners[x], corners[(x+1)%4], new Scalar(255,0,0,255), 7);
						}
					}
				}
			}
		}
		
		
		//Takes in rRect pairs and returns one rRect pair
		private void closestPair (Mat inputImage, RotatedRect[][] input, Rect[][] andrewInput, Mat outputImage, RotatedRect[] output, Rect[] andrewOutput) {
			double maxheight = 0.0;
			int best = 0;
			Point[] corners = new Point[4];
			inputImage.copyTo(outputImage);
			for (int i = 0; i < input.length; i++) {
				if ((andrewInput[i][0].height + andrewInput[i][1].height) / 2 > maxheight)
					best = i;
			}
			for (int i = 0; i < 2; i++) {
				output[i] = input[best][i];
				andrewOutput[i] = andrewInput[best][i];
				output[i].points(corners);
				for (int j = 0; j < 4; j++) {
					Imgproc.line(outputImage, corners[j], corners[(j+1)%4], new Scalar(0,100,255,255), 10);
				}
			}
		}
		
		private boolean withinRange(double one, double two, double range) {
			if (Math.abs(one - two) <= range)
				return true;
			return false;
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
		
		//measures whether robot is directly in front of tape marks
		//this is a public method because we should probably make the robot move and adjust elsewhere
		//just checking if this is true as it moves
		private boolean isStraight(Point[][] tapeTargets) {	
			
			double left = getDifference(tapeTargets[0][0], tapeTargets[0][1]);
			double right = getDifference(tapeTargets[1][0], tapeTargets[1][1]);
			
			//0.1 is a placeholder for now, we can decide how much room we need
			if(Math.abs(right-left)<tolerance) {
				return true;
			}
			else {
				return false;
			}
		}

		
		private double getDistance() {
			double distance = 0; //0 is placeholder for now
			//use camera to see how large tape appears when robot is in correct location
			//compare rectangle size irl to pixel size
			return distance;
		}
		
	}
}