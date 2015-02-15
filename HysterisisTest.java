/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import javax.swing.JFrame;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_highgui;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.CV_HSV2BGR;
import static org.bytedeco.javacpp.opencv_imgproc.CV_SHAPE_ELLIPSE;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import org.bytedeco.javacv.CanvasFrame;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class HysterisisTest {

    public static void main(String[] argv) {
        //opencv_core.IplImage image = cvLoadImage("/home/jagen31/Robotics/HotTarget/images/ballOnChair.jpg", CV_LOAD_IMAGE_COLOR);
        opencv_core.IplImage image = CameraFeed.getFeed("rtsp://10.0.20.11/axis-media/media.amp?videocodec=h264&resolution=320x240").getImage();
        opencv_core.CvMat mat = image.asCvMat();
        CanvasFrame frame2 = new CanvasFrame("origin", 1);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.showImage(image);
        opencv_highgui.cvSaveImage("image.jpg", image);
        cvCvtColor(image.asCvMat(), mat, CV_BGR2HSV);
        
        Calibrator cal = new MeanCalibrator();
        Character[][] calthresh = {{0, 0, 0}, {255, 179, 179}};
        Filter filter = new ColoredThresholdFilter(new Threshold(calthresh));
        CvMat calibrate = filter.runFilter(mat.clone());

        CanvasFrame frame3 = new CanvasFrame("Initial Filter", 1);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.showImage(calibrate.asIplImage());
        opencv_highgui.cvSaveImage("image-Threshold.jpg", calibrate.asIplImage());
        
        Character[] calibrated = cal.calibrateHSV(calibrate);

        Threshold thresh = Calibrator.calibratedArrayToThreshold(calibrated, new int[]{5, 10, 10}, new int[]{5, 10, 10});
        Threshold lax = Calibrator.calibratedArrayToThreshold(calibrated, new int[]{20, 50, 100}, new int[]{20, 100, 100});
/*
        filter = new AreaFilter(new MorphologyFilter(new HysteresisFilter(thresh, lax), CV_SHAPE_ELLIPSE, 5, 5), 500);
        long time = System.currentTimeMillis();
        CvMat filtered = filter.runFilter(mat);
        System.out.println(System.currentTimeMillis() - time);
        CanvasFrame frame = new CanvasFrame("frame", 1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.showImage(filtered.asIplImage());
        opencv_highgui.cvSaveImage("image-HSV.jpg", image);
*/
        opencv_highgui.cvWaitKey();
    }
}
