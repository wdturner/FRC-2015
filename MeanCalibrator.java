/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class MeanCalibrator extends Calibrator {

    @Override
    public Character[] calibrateHSV(CvMat image) {
        Character[] hsv = new Character[3];
        int sumh = 0;
        int sums = 0;
        int sumv = 0;
        int num = 0;
        for (int i = 0; i < image.rows(); ++i) {
            for (int j = 0; j < image.cols(); ++j) {
                if (image.get(i, j) != 0) {
                    ++num;
                    CvScalar pixel = cvGet2D(image, i, j);
                    sumh += pixel.getVal(0);
                    sums += pixel.getVal(1);
                    sumv += pixel.getVal(2);
                }
            }
        }
        if (num != 0) {
            return new Character[]{(char) (sumh / num), (char) (sums / num), (char) (sumv / num)};
        } else {
            return null;
        }
    }
    
    public static Character[][] getOffset(Character[] avg, int h, int s, int v){
        int[][] vals = new int[2][3];
        Character[][] chars = new Character[2][3];
        vals[0][0] = avg[0] - h;
        vals[0][1] = avg[1] - s;
        vals[0][2] = avg[2] - v;
        vals[1][0] = avg[0] + h;
        vals[1][1] = avg[1] + s;
        vals[1][2] = avg[2] + v;
        for(int i = 0; i < 3; ++i){
            if(vals[0][i] < 0)
                vals[0][i] = 0;
            chars[0][i] = (char)vals[0][i];
        }
        for(int i = 0; i < 3; ++i){
            if(vals[1][i] > 255)
                vals[1][i] = 255;
            chars[1][i] = (char)vals[1][i];
        }
        return chars;
    }
}
