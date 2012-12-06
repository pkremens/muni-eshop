/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.util.quilifier.MyLogger;
import java.util.logging.Logger;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Model
public class LoggingController {
    @Inject
    @MyLogger
    Logger log;
    
  
    public void makeLog() {
        System.out.println("Making log");
        log.fine("Fine log");
        log.info("Info log");
        log.warning("Warning log");
    }
    
}
