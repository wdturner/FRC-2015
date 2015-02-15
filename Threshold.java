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
public class Threshold {

    Character[][] low, high;

    public Threshold(Character[]
        ... thresholds){
        low = new Character[thresholds.length / 2][];
        high = new Character[low.length][];
        for (int i = 0; i < thresholds.length - 1; i += 2) {
            low[i/2] = thresholds[i];
            high[i/2] = thresholds[i + 1];
        }
    }

    public boolean filter(double... vals) {
        if (vals.length != high[0].length) {
            throw new IllegalArgumentException("Test array and filter do not match");
        }
        
        boolean passg = true;
        for (int i = 0; i < low.length; ++i) {
            boolean passl = true;
            for (int j = 0; j < low[0].length; ++j) {
                passl &= (vals[j] >= low[i][j] && vals[j] <= high[i][j]);
            }
            if(passl)
                return true;
        }
        return false;
    }
    
    public static String rawToString(Character[] thresh){
        String data = "(";
        for(Character c : thresh){
            data += (int) c + ",";
        }
        data = data.substring(0, data.length() - 1);
        return data + "]";
    }
    
    @Override
    public String toString() {
        String[][] print = new String[low.length][low[0].length];
        for (int i = 0; i < print.length; ++i) {
            for (int j = 0; j < print[0].length; ++j) {
                print[i][j] = "(" + (int) low[i][j] + "," + (int) high[i][j] + ")";
            }
        }
        String res = "";
        for (String[] out : print) {
            for (String string : out) {
                res += (string + " ");
            }
            res += "\n";
        }
        return res;
    }
}
