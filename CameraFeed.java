/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014vision;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class CameraFeed {
    private static volatile CameraFeed instance;
    private FrameGrabber grabber;
    private static String feed = "";
    
    private CameraFeed(String feed){
        this.feed = feed;
        if(feed.matches("^[0-9]+$"))
            grabber = new OpenCVFrameGrabber(Integer.parseInt(feed));
        else
            grabber = new OpenCVFrameGrabber(feed);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(CameraFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Finalize(){
        try {
            grabber.release();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(CameraFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static synchronized CameraFeed getFeed(String feed){
        if(instance == null)
            instance = new CameraFeed(feed);
        else
            if(feed != CameraFeed.feed)
                throw new IllegalArgumentException("CameraFeed can only be instanciated once.");
        return instance;
    }
    
    public static synchronized CameraFeed getFeed(){
        return getFeed(feed);
    }
    
    public opencv_core.IplImage getImage(){
        try {
            return grabber.grab();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(CameraFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void writeImage(String loc){
        try {
            opencv_highgui.cvSaveImage(loc, grabber.grab());
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(CameraFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}