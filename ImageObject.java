/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class ImageObject {

    private final Calculation area;
    private final double ratio;
    private final String name;

    private final int ratiothreshold;
    private final int areathreshold;
    private final int id;

    public ImageObject(Calculation area, double ratio,
            final int areathreshold, final int ratiothreshold, String name, int id) {
        this.area = area;
        this.ratio = ratio;
        this.ratiothreshold = ratiothreshold;
        this.areathreshold = areathreshold;
        this.name = name;
        this.id = id;
    }
    
    public void toFile(String iname, String serializecalc){
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(new FileOutputStream(new File(serializecalc)));
            stream.writeObject(this.area);
            FileWriter writer = new FileWriter(iname);
            writer.write(serializecalc + ":" + ratio + ":" + areathreshold + ":"
                    + ratiothreshold + ":" + name + ":" + id);
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ImageObject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ImageObject fromFile(File file) {
        try {
            String[] argv = new BufferedReader(new FileReader(file)).readLine().split(":");
            ObjectInputStream calc = new ObjectInputStream(new FileInputStream(argv[0]));
            Calculation calculation = (Calculation) calc.readObject();
            double rat = Double.parseDouble(argv[1]);
            int areathresh = Integer.parseInt(argv[2]);
            int ratiothresh = Integer.parseInt(argv[3]);
            String name = argv[4];
            int id = Integer.parseInt(argv[5]);
            return new ImageObject(calculation, rat, areathresh, ratiothresh, name, id);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ImageObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public double getArea(double width, double height) {
        return this.area.calculate(width, height);
    }

    public double getRatio() {
        return this.ratio;
    }

    public int getAreaThreshold() {
        return this.areathreshold;
    }

    public int getRatioThreshold() {
        return this.ratiothreshold;
    }

    public String toString() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }
}
