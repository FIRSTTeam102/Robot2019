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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.opencv.core.*;
import org.team102.robots.robot2019.RobotMap;
import org.team102.robots.robot2019.lib.VisionCameraHelper;

import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.opencv.imgproc.Imgproc;
import java.util.concurrent.TimeUnit;

public class SubsystemCameras extends Subsystem {
	
	public ArrayList<VideoSource> visibleVideoOutputs = new ArrayList<>();
	
	public SubsystemCameras() {
		super("Cameras");
		
		VideoSource visionCamera = VisionCameraHelper.openAndVerifyCamera("Vision Camera", RobotMap.CAMERA_ID_VISION, 480, 360, 15, false);
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
		private Mat amandaCornersOutput = new Mat();
		private Mat amandaCornersSomething = new Mat();
		private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
		private ArrayList<MatOfPoint2f> convexHullsOutput = new ArrayList<MatOfPoint2f>();
		private ArrayList<MatOfPoint2f> amandaCornersInputCnts = new ArrayList<MatOfPoint2f>();
		
		//Point[rectangle] leftmost=first [point 1-4] start top left- listed clockwise
		
		//Point[rectangle] leftmost=first [point 1-4] start top left- listed clockwise
		private Point[][] andrewCornersOutput = new Point[20][4];
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
			double[] rgbThresholdRed = {233.90286621644344, 255.0};
			double[] rgbThresholdGreen = {235.4316663399017, 255.0};
			double[] rgbThresholdBlue = {238.48919660496196, 255.0};
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
			
			//Cut image to convex hulls (this gon be weird)
			//cutToContour(input, convexHullsOutput, cutToContourOutput);
			
			//Harris Corners
			/*Mat harrisInput = blurOutput;
			findHarrisCorners(harrisInput, harrisOutput, 200);*/
			
			Mat andrewCornersInput = blurOutput;
			/*andrewCorners(andrewCornersInput, convexHullsOutput, andrewCornersOutput, andrewCornersImageOutput);
			final int centerHeights[] = new int[20];
			final int cornersFound = 0;
			cornersFound = 0;
			for (int i = 0; i < 0; i++) {
				if (andrewCornersOutput[i][0] != null)
					cornersFound++;
			}
			for (int i = 0; i < cornersFound; i++) {
				centerHeights[i] = (andrewCornersOutput[i][0].y + anrewCornersOutput[i][2].y) / 2;
				for (int j = 0; j < i; j++) {
					if (centerHeights[i] IS CLOSE TO THE OTHER THEN THEY GOOD)
						
				}
				System.out.println(andrewCornersOutput[i][0]);
			}
			TimeUnit.SECONDS.sleep(1);*/

			//andrewCorners(andrewCornersInput, convexHullsOutput, andrewCornersOutput, andrewCornersImageOutput);

			/*int cornersFound = findTape(andrewCornersOutput);
			if (cornersFound == 3) {
				isolateTape(blurOutput, tapeMarks,isolateTapeOutput);
				return isolateTapeOutput;
			}
			else if (cornersFound > 3 || cornersFound < 2) {
				System.out.println("Too many or too few corners found!");
				return input;
			}*/

			//TimeUnit.SECONDS.sleep(1);
			//andrewCorners(andrewCornersInput, convexHullsOutput/*, andrewCornersOutput*/, andrewCornersImageOutput);
			
			/*for (int i = 0; i < convexHullsOutput.size() && i < 10; i++) {
				convexHullsOutput.get(i).convertTo(amandaCornersInputCnts.get(i), CvType.CV_32F);
			}*/
			
			amandaCorners(blurOutput, convexHullsOutput, amandaCornersSomething, amandaCornersOutput);
			
			return amandaCornersOutput;
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
		
		/*public void findHarrisCorners(Mat input, Mat dst){
	        dst = Mat.zeros(input.size(), CvType.CV_32F);
	        int blockSize = 2;
	        int apertureSize = 3;
	        double k = 0.04;
	        Imgproc.cornerHarris(input, dst, blockSize, apertureSize, k);
	        Core.normalize(dst, dstNorm, 0, 255, Core.NORM_MINMAX);
	        Core.convertScaleAbs(dstNorm, dstNormScaled);
	        float[] dstNormData = new float[(int) (dstNorm.total() * dstNorm.channels())];
	        dstNorm.get(0, 0, dstNormData);
	        for (int i = 0; i < dstNorm.rows(); i++) {
	            for (int j = 0; j < dstNorm.cols(); j++) {
	                if ((int) dstNormData[i * dstNorm.cols() + j] > threshold) {
	                    Imgproc.circle(dstNormScaled, new Point(j, i), 5, new Scalar(0), 2, 8, 0);
	                }
	            }
	        }
	        cornerLabel.setIcon(new ImageIcon(HighGui.toBufferedImage(dstNormScaled)));
		}	
		public void findHarrisCorners(Mat Scene, Mat Object, int thresh) {

		    // This function implements the Harris Corner detection. The corners at intensity > thresh
		    // are drawn.
		    Mat Harris_scene = new Mat();
		    Mat Harris_object = new Mat();

		    Mat harris_scene_norm = new Mat(), harris_object_norm = new Mat(), harris_scene_scaled = new Mat(), harris_object_scaled = new Mat();
		    int blockSize = 9;
		    int apertureSize = 5;
		    double k = 0.1;
		    Imgproc.cornerHarris(Scene, Harris_scene,blockSize, apertureSize,k);
		    Imgproc.cornerHarris(Object, Harris_object, blockSize,apertureSize,k);

		    Core.normalize(Harris_scene, harris_scene_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
		    Core.normalize(Harris_object, harris_object_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());

		    Core.convertScaleAbs(harris_scene_norm, harris_scene_scaled);
		    Core.convertScaleAbs(harris_object_norm, harris_object_scaled);

		    for( int j = 0; j < harris_scene_norm.rows() ; j++){
		        for( int i = 0; i < harris_scene_norm.cols(); i++){
		            if ((int) harris_scene_norm.get(j,i)[0] > thresh){
		                Imgproc.circle(harris_scene_scaled, new Point(i,j), 5 , new Scalar(0), 2 ,8 , 0);
		            }
		        }
		    }

		    for( int j = 0; j < harris_object_norm.rows() ; j++){
		        for( int i = 0; i < harris_object_norm.cols(); i++){
		            if ((int) harris_object_norm.get(j,i)[0] > thresh){
		                Imgproc.circle(harris_object_scaled, new Point(i,j), 5 , new Scalar(0), 2 ,8 , 0);
		            }
		        }
		    }
		}*/

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
		private void andrewCorners(Mat input, ArrayList<MatOfPoint> inputContours, Point[][] corners, Mat output) {
			input.copyTo(output);
			Rect rect = new Rect();
			//Point[][] corners = new Point[20][4];
			final Scalar circleColor = new Scalar(255, 0, 0);
			for(int i=0; i< inputContours.size(); i++) { //For every contour
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 ){
	            	rect = Imgproc.boundingRect(inputContours.get(i)); //Find the rectangle with the corners
					corners[i][0] = new Point(rect.x, rect.y); //Define the corners
					corners[i][1] = new Point(rect.x + rect.width, rect.y);
					corners[i][2] = new Point(rect.x + rect.width, rect.y + rect.height);
					corners[i][3] = new Point(rect.x, rect.y + rect.height);
					/*Imgproc.circle(output, new Point(rect.x, rect.y), 5, circleColor); //Circle the corners in the image
					Imgproc.circle(output, new Point(rect.x + rect.width, rect.y), 5, circleColor);
					Imgproc.circle(output, new Point(rect.x + rect.width, rect.y + rect.height), 5, circleColor);
					Imgproc.circle(output, new Point(rect.x, rect.y + rect.height), 5, circleColor);*/
	            }
			}
		}	
		
		private void amandaCorners(Mat input, ArrayList<MatOfPoint2f> inputContours, Mat corners, Mat output) { 
			Mat cornerPoints = new Mat();
			input.copyTo(output);
			RotatedRect rRect = null;
			for (int i = 0; i < inputContours.size(); i++) {
				rRect = Imgproc.minAreaRect(inputContours.get(i));
				Imgproc.boxPoints(rRect, cornerPoints);
			}
			Point[] vertices = new Point[4];  
	        rRect.points(vertices);  
	        for (int j = 0; j < 4; j++){  
	            Imgproc.line(output, vertices[j], vertices[(j+1)%4], new Scalar(0,255,0));
	        }
			corners = cornerPoints;
		}
		
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
		
		//this isolates tape once 3 rectangles are found
		private void isolateTape(Mat input, Point[][] tape, Mat output){
			
			double leftDifference = getDifference(tapeMarks[0][0], tapeMarks[1][0]);
			double rightDifference = getDifference(tapeMarks[1][0], tapeMarks[2][0]);
			
			if (leftDifference>rightDifference) {
				//makes tape[0] [all points] = tape[1] [all points]
				for(int i=0; i<=1; i++) {
					for(int j=0; j<4; j++) {
						tapeMarks[i][j] = tapeTargets[i][j];
					}
				}
			}
			//!!
			else {
				for(int i=1; i<=2; i++) {
					for(int j=0; j<4; j++) {
						//i-1 because we don' t want to assign it to tapeTargets[1]
						tapeMarks[i][j] = tapeTargets[i-1][j];
					}
				}
			}
			final Scalar circleColor = new Scalar(255, 0, 0);
			for(int i=0;i<4;i++) {
	
				Imgproc.circle(output, tapeTargets[0][i],5 ,circleColor );
				Imgproc.circle(output, tapeTargets[1][i],5 ,circleColor );
			}
			System.out.println("Target 1 top left:" + tapeTargets[0][0]);
			System.out.println("Target 2 top left:" + tapeTargets[1][0]);
			
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