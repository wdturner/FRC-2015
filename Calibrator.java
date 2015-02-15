package pkg2014vision;

import java.io.File;
import org.bytedeco.javacpp.opencv_core.CvMat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public abstract class Calibrator {

    public abstract Character[] calibrateHSV(CvMat image);

    public static Character[] combineCalibratedArrays(Character[]  
        ... cal){
        int length = 0;
        for (Character[] arr : cal) {
            if(arr == null){
                continue;
            }
            length += arr.length;
        }
        System.out.println("LENGTH:" + length);
        Character[] ret = new Character[length];
        int pos = 0;
        for (Character[] arr : cal) {
            if(arr == null){
                continue;
            }
            int len = arr.length;
            System.arraycopy(arr, 0, ret, pos, len);
            pos += len;
        }
        return ret;
    }

    public static Threshold calibratedArrayToThreshold(Character[] cal,
            int[] lower, int[] upper) {
        
        if(cal == null)
            return new Threshold(new Character[] {0,0,0}, new Character[] {0,0,0});
        Character[][] thresh = new Character[((cal.length / 3) * 2)][3];
        for (int i = 0; i < (cal.length / 3); ++i) {
            for (int j = 0; j < 3; ++j) {
                char low = (char) (cal[(i+1) * j - i] - lower[(i+1) * j - i]);
                if (low < 0 || low > cal[i]) {
                    low = 0;
                }
                thresh[i][j] = low;
                char high = (char) (cal[(i+1)*j - i] + upper[(i+1)*j - i]);
                if (high > 255 || high < cal[i]) {
                    high = 255;
                }
                if (j == 0 && high > 180) {
                    high = 180;
                }
                thresh[i + 1][j] = high;
            }
        }
        return new Threshold(thresh);
    }

    public static void writeCalibratedArray(String out, Character[] cal) {

    }

    public static File readCalibratedArray(String in) {
        return null;
    }
}
