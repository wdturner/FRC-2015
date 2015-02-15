/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014vision;

import javax.swing.JFrame;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import org.bytedeco.javacv.CanvasFrame;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class CalibratorTest {
    public static void main(String[] args) {
        opencv_core.IplImage image = CameraFeed.getFeed("0").getImage();
        CameraFeed.getFeed().writeImage("/home/jagen31/Coding/image.jpg");
        opencv_core.CvMat mat = image.asCvMat();
        cvCvtColor(image.asCvMat(), mat, CV_BGR2HSV);

        Character[][] thresh = {{100, 50, 0}, {140, 255, 255}};
        
        Filter filter = new ColoredThresholdFilter(new Threshold(thresh));
        Calibrator cal = new MeanCalibrator();
        
        opencv_core.CvMat filtered = filter.runFilter(mat);
        System.out.println((int)cal.calibrateHSV(filtered)[1]);
        CanvasFrame frame = new CanvasFrame("frame", 1);
        cvCvtColor(filtered, filtered, opencv_imgproc.CV_HSV2BGR);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.showImage(filtered.asIplImage());
        opencv_highgui.cvWaitKey();
    }
}
