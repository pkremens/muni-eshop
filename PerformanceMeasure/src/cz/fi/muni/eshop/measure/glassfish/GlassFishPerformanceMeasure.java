/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.measure.glassfish;

import cz.fi.muni.eshop.measure.Measure;
import cz.fi.muni.eshop.measure.Server;
import java.io.IOException;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class GlassFishPerformanceMeasure {

    public static void main(String[] args) throws Exception {
        Measure measure = null;
        try {
            measure = new Measure(Server.GLASSFISH3);
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
