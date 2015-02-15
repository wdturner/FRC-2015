/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import org.bytedeco.javacpp.opencv_core.CvMat;

/**
 *
 * @author jagen31
 */
public interface Filter {

    CvMat runFilter(CvMat image);
    
    void setThreshold(Threshold... thresh);
}
