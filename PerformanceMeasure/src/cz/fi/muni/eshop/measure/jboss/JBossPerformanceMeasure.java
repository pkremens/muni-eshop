/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.measure.jboss;

import cz.fi.muni.eshop.measure.Server;
import java.io.IOException;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class JBossPerformanceMeasure {

    public static void main(String[] args) throws Exception {
        cz.fi.muni.eshop.measure.Measure measure = null;
        try {
            measure = new cz.fi.muni.eshop.measure.Measure(Server.JBOSSAS7);
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
