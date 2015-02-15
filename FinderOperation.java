/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014vision;

import java.util.ArrayList;
import javax.swing.JFrame;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import org.bytedeco.javacv.CanvasFrame;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class FinderOperation implements Runnable{
    public void run(){
        Character[][] strict = {{90, 20, 20}, {115, 255, 150}};
        Filter cfilter = new ColoredThresholdFilter(new Threshold(strict));
        Calibrator cal = new MeanCalibrator();
        Calculation area = new Calculation() {

            @Override
            public double calculate(double x, double y) {
                return x * y - 2 * (x / 4 * (2 * y) / 3);
            }
        };
        ImageObject shirt = new ImageObject(area, 2, 50, 50, "shirt", 1);
        opencv_core.IplImage image = null;
        synchronized(Finder.class){
            image = Finder.getImage();
        }
        CanvasFrame frame = new CanvasFrame("frame", 1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for (;;) {
            image = CameraFeed.getFeed().getImage();
            opencv_core.CvMat mat = image.asCvMat();
            frame.showImage(image);
            cvCvtColor(image.asCvMat(), mat, CV_BGR2HSV);
            opencv_core.CvMat filtered = cfilter.runFilter(mat);
            Character[] calhsv = cal.calibrateHSV(filtered);
            ScoreFilter filter = new ScoreFilter(new MorphologyFilter(
                    new AreaFilter(
                            new HysteresisFilter(
                                    new Threshold(MeanCalibrator.getOffset(calhsv, 20, 100, 100)),
                                    new Threshold(MeanCalibrator.getOffset(calhsv, 10, 60, 60))),
                            1000),
                    opencv_imgproc.CV_SHAPE_RECT, 5, 5),
                    shirt);
            mat = image.asCvMat();
            filtered = filter.runFilter(mat);
            ArrayList<opencv_core.CvSeq> vals = filter.getScores().get(shirt);
            if(vals != null){
                opencv_core.CvRect rect = opencv_imgproc.cvBoundingRect(vals.get(0), 0);
            }
        }
    }
}
