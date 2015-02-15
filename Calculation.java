/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.io.Serializable;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public interface Calculation extends Serializable{

    double calculate(double x, double y);
}
