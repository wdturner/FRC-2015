/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.awt.Point;
import java.util.Stack;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_core.cvZero;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class HysteresisFilter implements Filter {

    private Threshold initialThreshold;
    private Threshold secondaryThreshold;

    public HysteresisFilter(Threshold initialThreshold, Threshold secondaryThreshold) {
        this.initialThreshold = initialThreshold;
        this.secondaryThreshold = secondaryThreshold;
    }

    @Override
    public CvMat runFilter(CvMat image) {
        CvMat dest = CvMat.create(image.rows(), image.cols(), CV_8U);
        cvZero(dest);
        for (int i = 0; i < image.rows(); ++i) {
            for (int j = 0; j < image.cols(); ++j) {
                CvScalar pixel = cvGet2D(image, i, j);
                double h = pixel.getVal(0),
                        s = pixel.getVal(1),
                        v = pixel.getVal(2);
                if (initialThreshold.filter(h, s, v)) {
                    thresh(image, dest, i, j);
                }
            }
        }
        return dest;
    }

    private void thresh(CvMat image, CvMat dest, int i, int j) {
        Stack<Point> stack = new Stack<>();
        stack.add(new Point(i, j));
        while (!stack.isEmpty()) {
            Point point = stack.pop();
            j = (int)point.getY();
            i = (int)point.getX();

            dest.put(i, j, 0xFF);
            CvScalar pixel;
            if (!(i + 1 >= image.rows())) {
                if (dest.get(i + 1, j) != 0xFF) {
                    pixel = cvGet2D(image, i + 1, j);

                    double h = pixel.getVal(0),
                            s = pixel.getVal(1),
                            v = pixel.getVal(2);
                    if (secondaryThreshold.filter(h, s, v)) {
                        dest.put(i + 1, j, 0xFF);
                        stack.push(new Point(i + 1, j));
                    }
                }
            }
            if (!(j + 1 >= image.cols())) {
                if (dest.get(i, j + 1) != 0xFF) {
                    pixel = cvGet2D(image, i, j + 1);

                    double h = pixel.getVal(0),
                            s = pixel.getVal(1),
                            v = pixel.getVal(2);
                    if (secondaryThreshold.filter(h, s, v)) {
                        dest.put(i, j + 1, 0xFF);
                        stack.push(new Point(i, j + 1));
                    }
                }
            }
            if (i - 1 != -1) {
                if (dest.get(i - 1, j) != 0xFF) {

                    pixel = cvGet2D(image, i - 1, j);

                    double h = pixel.getVal(0),
                            s = pixel.getVal(1),
                            v = pixel.getVal(2);
                    if (secondaryThreshold.filter(h, s, v)) {
                        dest.put(i - 1, j, 0xFF);
                        stack.push(new Point(i - 1, j));
                    }
                }
            }
            if (j - 1 != -1) {
                if (dest.get(i, j - 1) != 0xFF) {

                    pixel = cvGet2D(image, i, j - 1);

                    double h = pixel.getVal(0),
                            s = pixel.getVal(1),
                            v = pixel.getVal(2);
                    if (secondaryThreshold.filter(h, s, v)) {
                        dest.put(i, j - 1, 0xFF);
                        stack.push(new Point(i, j - 1));
                    }
                }
            }
        }
    }

    /*private void recurseThresh(CvMat image, CvMat dest, int i, int j) {
     CvScalar pixel;
     if (!(i + 1 >= image.rows())) {
     if (dest.get(i + 1, j) != 0xFF) {
     pixel = cvGet2D(image, i + 1, j);

     double h = pixel.getVal(0),
     s = pixel.getVal(1),
     v = pixel.getVal(2);
     if (secondaryThreshold.filter(h, s, v)) {
     dest.put(i + 1, j, 0xFF);
     recurseThresh(image, dest, i + 1, j);
     }
     }
     }
     if (!(j + 1 >= image.cols())) {
     if (dest.get(i, j + 1) != 0xFF) {
     pixel = cvGet2D(image, i, j + 1);

     double h = pixel.getVal(0),
     s = pixel.getVal(1),
     v = pixel.getVal(2);
     if (secondaryThreshold.filter(h, s, v)) {
     dest.put(i, j + 1, 0xFF);
     recurseThresh(image, dest, i, j + 1);
     }
     }
     }
     if (i - 1 != -1) {
     if (dest.get(i - 1, j) != 0xFF) {
                
     pixel = cvGet2D(image, i - 1, j);

     double h = pixel.getVal(0),
     s = pixel.getVal(1),
     v = pixel.getVal(2);
     if (secondaryThreshold.filter(h, s, v)) {
     dest.put(i - 1, j, 0xFF);
     recurseThresh(image, dest, i - 1, j);
     }
     }
     }
     if (j - 1 != -1) {
     if (dest.get(i, j - 1) != 0xFF) {
                
     pixel = cvGet2D(image, i, j - 1);

     double h = pixel.getVal(0),
     s = pixel.getVal(1),
     v = pixel.getVal(2);
     if (secondaryThreshold.filter(h, s, v)) {
     dest.put(i, j - 1, 0xFF);
     recurseThresh(image, dest, i, j - 1);
     }
     }
     }
     }*/

    @Override
    public void setThreshold(Threshold... thresh) {
        this.initialThreshold = thresh[0];
        this.secondaryThreshold = thresh[1];
    }
}
