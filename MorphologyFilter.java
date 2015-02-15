/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplConvKernel;
import static org.bytedeco.javacpp.opencv_imgproc.cvDilate;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class MorphologyFilter extends FilterDecorator {

    private boolean expand;
    private int shape;
    private int kernelwidth;
    private int kernelheight;

    public MorphologyFilter(Filter filter, int shape, int kernelwidth, int kernelheight, boolean expand) {
        super(filter);
        this.kernelwidth = kernelwidth;
        this.kernelheight = kernelheight;
        this.shape = shape;
        this.expand = expand;
    }

    public MorphologyFilter(Filter filter, int shape, int kernelwidth, int kernelheight) {
        this(filter, shape, kernelwidth, kernelheight, false);
    }
    
    

    @Override
    protected CvMat decorateFilter(CvMat image) {
        CvMat dest = image.clone();
        IplConvKernel kernel = IplConvKernel.create(kernelheight, kernelwidth, 0, 0, shape, null);
        if(expand){
           cvDilate(dest, dest, kernel, 1);
           cvErode(image, dest, kernel, 1);
        }else{
            cvErode(image, dest, kernel, 1);
            cvDilate(dest, dest, kernel, 1);
        }
        return dest;
    }

}
