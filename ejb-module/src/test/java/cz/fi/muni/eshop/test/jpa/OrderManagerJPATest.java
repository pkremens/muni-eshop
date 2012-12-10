/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class OrderManagerJPATest {

    @Inject
    @MuniEshopLogger
    Logger log;

    @Test
    @Ignore("TODO")
    public void dummy() {
        //TODO
    }
}
