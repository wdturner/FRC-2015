/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 *
 * @author jagen31
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        filterTest();
    }

    public static void serverTest() {
        /*IplImage image = null;
         CvMat mat = null;

         OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("rtsp://10.0.20.11/axis-media/media.amp?videocodec=h264&resolution=320x240");
         try {
         grabber.start();
         image = grabber.grab();
         mat = image.asCvMat();
         cvCvtColor(mat, mat, CV_BGR2HSV);
         } catch (FrameGrabber.Exception ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }

         Calibrator cal = new MeanCalibrator();
         Character[][] calthreshgreen;
         calthreshgreen = new Character[][]{{20, 100, 100}, {150, 255, 255}};
         Filter filtergreen = new ColoredThresholdFilter(new Threshold(calthreshgreen));
         CvMat calibrategreen = filtergreen.runFilter(mat.clone());
         Character[] calibratedgreen = cal.calibrateHSV(calibrategreen);
         Threshold lax = Calibrator.calibratedArrayToThreshold(calibratedgreen, new int[]{30, 100, 50}, new int[]{10, 200, 50});
         ScoreFilter sfilter = new ScoreFilter(new SimpleBinaryFilter(lax));

         TargetServer server = TargetServer.getServer();
         Socket client = null;
         try {
         client = server.accept();
         } catch (IOException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }
         ServerOperation op = new ServerOperation(server, client, sfilter);
         op.scanObjects("/home/jagen31/Robotics/data/");
         Thread runop = new Thread(op.getServerOperation());
         runop.start();

         for (;;) {
         try {

         cvCvtColor(grabber.grab().asCvMat(), mat, CV_BGR2HSV);
         } catch (FrameGrabber.Exception ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }
         sfilter.runFilter(mat);
         }*/
    }

    public static void filterTest() {
        CanvasFrame frame = new CanvasFrame("frame", 1);
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(;;){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            IplImage image = null;
            
            try {    
                image = grabber.grab();
            } catch (FrameGrabber.Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.showImage(image);
        }
    }
}
