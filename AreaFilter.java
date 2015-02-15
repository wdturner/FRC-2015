/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.helper.opencv_core.cvDrawContours;
import static org.bytedeco.javacpp.opencv_core.CV_FILLED;
import static org.bytedeco.javacpp.opencv_core.CV_WHOLE_SEQ;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_TREE;
import static org.bytedeco.javacpp.opencv_imgproc.cvContourArea;
import static org.bytedeco.javacpp.opencv_imgproc.cvFindContours;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class AreaFilter extends FilterDecorator {

    private int minarea;
    
    public AreaFilter(Filter filter, int minarea) {
        super(filter);
        this.minarea = minarea;
    }

    @Override
    protected CvMat decorateFilter(CvMat image) {
        CvMemStorage storage = CvMemStorage.create();
        CvMat dest = image.clone();
        CvSeq contours = new CvSeq(dest);
        cvFindContours(image, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, cvPoint(0,0));
        for (CvSeq seq = contours; seq != null; seq = seq.h_next()) {
            if (cvContourArea(seq, CV_WHOLE_SEQ, 0) < minarea) {
                cvDrawContours(dest, seq, CvScalar.ZERO, CvScalar.ZERO, -1, CV_FILLED, 0);
            }
        }
        return dest;
    }
}