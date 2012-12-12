/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.service.ProductManager;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Singleton
public class MainController {

    @Inject
    private ProductManager productManager;
    @Inject
    private Logger log;

    public void generateData() {
        
    }
    
    public void controlData() {
        
    }
    
    public void clearData() {
        
    }
    
    
    public void onControlMessageReception(
            @Observes(notifyObserver = Reception.ALWAYS) final ControlMessage controlMessage) {
        log.warning("Catching event: " + controlMessage.getMessage());
        switch (controlMessage) {
            case TOO_MANY_CLOSED:
                log.info("A");
                break;
            case FILL_THE_STORE:
                log.warning("It seems, that someone forgot to fill the store again.");
                log.info("Doing nothig for now, update to take control");
                break;
            default:
                throw new IllegalArgumentException("Unrecognized type of control messages has been caught");
        }

    }
}
