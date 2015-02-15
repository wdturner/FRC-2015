/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class SimpleBinaryFilter implements Filter {

    private Threshold threshold;

    public SimpleBinaryFilter(Character[][] HSVValues) {
        this.threshold = new Threshold(HSVValues);
    }

    public SimpleBinaryFilter(Threshold threshold) {
        this.threshold = threshold;
    }

    public SimpleBinaryFilter() {
    }

    @Override
    public CvMat runFilter(CvMat image) {
        CvMat dest = CvMat.create(image.rows(), image.cols(), CV_8U);
        for (int i = 0; i < image.rows(); ++i) {
            for (int j = 0; j < image.cols(); ++j) {
                CvScalar pixel = cvGet2D(image, i, j);
                double h = pixel.val(0),
                        s = pixel.val(1),
                        v = pixel.val(2);
                dest.put(i, j, threshold.filter(h, s, v) ? 0xFF : 0);
            }
        }
        return dest;
    }

    public void setThreshold(Threshold... threshold) {
        this.threshold = threshold[0];
    }
}
