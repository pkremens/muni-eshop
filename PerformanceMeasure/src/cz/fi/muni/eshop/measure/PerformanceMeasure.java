/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.measure;

import java.io.IOException;

/**
 *
 * @author pkremens
 */
public class PerformanceMeasure {

    public static void main(String[] args) throws IOException {
        Measure measure = null;
        try {
            measure = new Measure();
        } catch (Exception ex) {
            System.out.println(ex.getClass() + " : " + ex.getMessage());
        }
        try {
            System.out.println(measure.getFootprint());
        } catch (Exception ex) {
            System.out.println(ex.getClass() + " : " + ex.getMessage());
        }
        try {
            measure.destroy();
        } catch (Exception ex) {
            System.out.println(ex.getClass() + " : " + ex.getMessage());
        }
    }
}
