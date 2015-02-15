/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.opencv_core.CV_WHOLE_SEQ;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_TREE;
import static org.bytedeco.javacpp.opencv_imgproc.cvBoundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.cvContourArea;
import static org.bytedeco.javacpp.opencv_imgproc.cvFindContours;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class ScoreFilter extends FilterDecorator {
    
    private ImageObject[] templates;
    private Map<ImageObject, ArrayList<CvSeq>> scores;
    private volatile boolean setting;

    public ScoreFilter(Filter filter, ImageObject... templates) {
        super(filter);
        this.templates = templates;
        scores = new ConcurrentHashMap<>();
    }

    public int getNumTemplates() {
        return templates.length;
    }

    public synchronized void setTemplates(ImageObject[] objects) {
        templates = objects;
    }

    @Override
    protected synchronized CvMat decorateFilter(CvMat image) {
        scores.clear();
        CvMemStorage storage = CvMemStorage.create();
        CvMat temp = image.clone();
        CvSeq contours = new CvSeq(temp);
        cvFindContours(temp, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
        for (ImageObject object : templates) {
            ArrayList<CvSeq> results = new ArrayList<>();
            if (contours.isNull()) {
                return CvMat.create(image.rows(), image.cols());
            }
            for (CvSeq seq = contours; seq != null; seq = seq.h_next()) {
                CvRect bounds = cvBoundingRect(seq, 0);
                if (calculateRatioScore(object, bounds) >= object.getRatioThreshold()
                        && calculateAreaScore(object, bounds, seq) >= object.getAreaThreshold()) {
                    results.add(seq);
                }
            }
            scores.put(object, results);
        }
        return image;
    }
    
    public Map<ImageObject, ArrayList<CvSeq>> getScores(){
        return this.scores;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<ImageObject, ArrayList<CvSeq>> entry : scores.entrySet()) {
            for (CvSeq seq : entry.getValue()) {
                CvRect rect = cvBoundingRect(seq, 0);
                result += entry.getKey() + "@ (" + (rect.x() + (rect.width() / 2))
                        + "," + (rect.y() + (rect.height() / 2)) + ")\n";
            }
        }
        return result;
    }

    private int calculateRatioScore(ImageObject object, CvRect rect) {
        double virtualratio = 0;
        if (rect.height() != 0) {
            virtualratio = (double) rect.width() / (double) rect.height();
        }
        double actualratio = object.getRatio();
        return asRatio(virtualratio, actualratio);
    }

    private int calculateAreaScore(ImageObject object, CvRect rect, CvSeq contour) {
        double virtualarea = object.getArea(rect.width(), rect.height());
        double actualarea = cvContourArea(contour, CV_WHOLE_SEQ, 0);
        return asRatio(virtualarea, actualarea);
    }

    private int asRatio(double virtual, double actual) {
        return virtual > actual ? (int) ((actual / virtual) * 100)
                : (int) ((virtual / actual) * 100);
    }

    public void setThreshold(Threshold[] thresh) {
        super.setThreshold(thresh);  
    }
}
