/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class Finder {

    private static boolean reading = false;
    private static opencv_core.IplImage image;
    
    public static synchronized void main(String[] argv) {
        reading = true;
        image = CameraFeed.getFeed("rtsp://10.0.20.11/axis-media/media.amp?videocodec=h264&resolution=320x240").getImage();
        for(;;){
            reading = true;
            image = CameraFeed.getFeed().getImage();
            reading = false;
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static synchronized opencv_core.IplImage getImage() {
        while(reading){}
        return image;
    }
}
