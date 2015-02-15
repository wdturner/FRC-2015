/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import org.bytedeco.javacpp.opencv_core.CvMat;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public abstract class FilterDecorator implements Filter {

    private Filter filter;

    public FilterDecorator(Filter filter) {
        this.filter = filter;
    }

    @Override
    public CvMat runFilter(CvMat image) {
        return decorateFilter(filter.runFilter(image));
    }
    
    @Override
    public void setThreshold(Threshold... thresh){
        filter.setThreshold(thresh);
    }

    protected abstract CvMat decorateFilter(CvMat image);
}
