/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class ImageObjectBuilder {
    
    private Calculation areaCalculation;
    private double ratio;
    private int areaThreshold;
    private int ratioThreshold;
    private String name;
    private int id;

    public ImageObject getImageObject() {
        return new ImageObject(areaCalculation, ratio, areaThreshold, ratioThreshold, name, id);
    }

    public void setID(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param areaCalculation the areaCalculation to set
     */
    public void setAreaCalculation(Calculation areaCalculation) {
        this.areaCalculation = areaCalculation;
    }

    /**
     * @param ratio the ratio to set
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    /**
     * @param areaThreshold the areaThreshold to set
     */
    public void setAreaThreshold(int areaThreshold) {
        this.areaThreshold = areaThreshold;
    }

    /**
     * @param ratioThreshold the ratioThreshold to set
     */
    public void setRatioThreshold(int ratioThreshold) {
        this.ratioThreshold = ratioThreshold;
    }
}
