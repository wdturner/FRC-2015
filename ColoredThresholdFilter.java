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
public class ColoredThresholdFilter implements Filter {

    private Threshold threshold;

    public ColoredThresholdFilter(Threshold threshold) {
        this.threshold = threshold;
    }

    @Override
    public CvMat runFilter(CvMat image) {
        CvMat dest = image;
        for (int i = 0; i < image.rows(); ++i) {
            for (int j = 0; j < image.cols(); ++j) {
                CvScalar pixel = cvGet2D(image, i, j);
                double h = pixel.getVal(0),
                        s = pixel.getVal(1),
                        v = pixel.getVal(2);
                if (!threshold.filter(h, s, v)) {
                    dest.put(i, j, 0, 0);
                    dest.put(i, j, 1, 0);
                    dest.put(i, j, 2, 0);
                }
            }
        }
        return dest;
    }
    
    public void setThreshold(Threshold... thresh){
        this.threshold = thresh[0];
    }

}
