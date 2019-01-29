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
		private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
		private ArrayList<MatOfPoint> convexHullsOutput = new ArrayList<MatOfPoint>();
		private Point[][] andrewCornersOutput = new Point[20][4];
		private Mat andrewCornersImageOutput = new Mat();

		private Mat harrisOutput = new Mat();
	    private int threshold = 200;
	    
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
			andrewCorners(andrewCornersInput, convexHullsOutput, andrewCornersOutput, andrewCornersImageOutput);
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
			TimeUnit.SECONDS.sleep(1);
			return andrewCornersImageOutput;
		}
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
		 * An indication of which type of filter to use for a blur.
		 * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
		 */
		/*enum BlurType{
			BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
				BILATERAL("Bilateral Filter");

			private final String label;

			BlurType(String label) {
				this.label = label;
			}

			public static BlurType get(String type) {
				if (BILATERAL.label.equals(type)) {
					return BILATERAL;
				}
				else if (GAUSSIAN.label.equals(type)) {
				return GAUSSIAN;
				}
				else if (MEDIAN.label.equals(type)) {
					return MEDIAN;
				}
				else {
					return BOX;
				}
			}

			@Override
			public String toString() {
				return this.label;
			}
		}*/

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
		private void convexHulls(List<MatOfPoint> inputContours, ArrayList<MatOfPoint> outputContours) {
			final MatOfInt hull = new MatOfInt();
			outputContours.clear();
			for (int i = 0; i < inputContours.size(); i++) {
				final MatOfPoint contour = inputContours.get(i);
				final MatOfPoint mopHull = new MatOfPoint();
				Imgproc.convexHull(contour, hull);
				mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
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
			//Point[][] corners = new Point[20][4];
			final Scalar circleColor = new Scalar(255, 0, 0);
			for(int i=0; i< inputContours.size(); i++) { //For every contour
	            if (Imgproc.contourArea(inputContours.get(i)) > 50 ){
	            	Rect rect = Imgproc.boundingRect(inputContours.get(i)); //Find the rectangle with the corners
					corners[i][0] = new Point(rect.x, rect.y); //Define the corners
					corners[i][1] = new Point(rect.x + rect.width, rect.y);
					corners[i][2] = new Point(rect.x + rect.width, rect.y + rect.height);
					corners[i][3] = new Point(rect.x, rect.y + rect.height);
					Imgproc.circle(output, new Point(rect.x, rect.y), 5, circleColor); //Circle the corners in the image
					Imgproc.circle(output, new Point(rect.x + rect.width, rect.y), 5, circleColor);
					Imgproc.circle(output, new Point(rect.x + rect.width, rect.y + rect.height), 5, circleColor);
					Imgproc.circle(output, new Point(rect.x, rect.y + rect.height), 5, circleColor);
	            }
			}
		}	
		//to find distance between two Points created above
		private double getDifference(Point a, Point b) {
			double xDiff = a.x-b.x;
			double yDiff = a.y-b.y;
			double distance = Math.sqrt( (yDiff*yDiff)+(xDiff*xDiff));
			return distance;
		}		
		
		//so a few methods needed where we can pass in points- isStraight getAngle, getDistance, hasArrived
		//we have options on how we want to do it though, we could keep having it getAngle and getDistance
		//and then adjust until isStraight and hasArrived is true
		//must order points a-d clockwise starting top left
		private boolean isStraight(Point a, Point b, Point c, Point d) {	
			//goes clockwise around a rectangle to get side values
			double top = getDifference(a,b);
			double right = getDifference(b,c);
			double bottom = getDifference(c,d);
			double left = getDifference(d,a);
			
			//0.1 is a placeholder for now, we can decide how much room we need
			if(Math.abs(right-left)<0.1 && Math.abs(top-bottom)<0.1) {
				return true;
			}
			else {
				return false;
			}
		
			/*steps: 
			 * order coordinates clockwise, so we can get distance of individual sides
			 * compare distance of individual sides to other sides to determine rotation
			 * compare to irl measurements to get distance away
		
		*/
		}
		private double getDistance() {
			double distance = 0; //0 is placeholder for now
			//use camera to see how large tape appears when robot is in correct location
			//compare rectangle size then to rectangle size now, 
			return distance;
		}
		
	}
}