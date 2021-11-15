package main;

import api.DonkeyCar;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

/**
 * A basic example of how to use the API to control the car.
 */
public class Example {

    public static void main(String[] args) {

        // Instantiates the API
        DonkeyCar car = new DonkeyCar();

        try {
            // Opens a websocket to the API and Initializes OpenCV
            car.init();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            // Gets a frame from the camera of the car
            Mat frame = car.fetchFrame();

            // Makes the image smaller and blurs it in order to find the brightest part of the image
            Mat resized = getResized(frame);
            Mat blurred = getBlurred(resized);
            Point maxLoc = getMaxLoc(blurred);

            // Determines the angle of the wheels depending on which side of the image is brightest
            double angle = getAngle(maxLoc, blurred.size());

            // Sets a low throttle, the car can drive pretty fast!
            double throttle = 0.2;

            // Sends the instructions via a websocket to the car
            car.send(angle, throttle);
        }
    }

    private static double getAngle(Point maxLoc, Size size) {
        double width = size.width;
        return (maxLoc.x - (width / 2)) / (width / 2) * 2;
    }

    private static Point getMaxLoc(Mat blurred) {
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(blurred);
        return minMaxLocResult.maxLoc;
    }

    private static Mat getBlurred(Mat resized) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(resized, blurred, new Size(5, 5), 1.5);
        return blurred;
    }

    private static Mat getResized(Mat frame) {
        Mat resized = new Mat();
        Imgproc.resize(frame, resized, new Size(frame.size().width / 2, frame.size().height / 2));
        return resized;
    }

}
