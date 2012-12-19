package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.util.Controller;
import cz.fi.muni.eshop.util.Identity;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class ControllerBean {

    @EJB
    private Controller controller;
    @Inject
    private Logger log;
    @Inject
    private Identity identity;

    public void wipeOutDb() {
        log.warning("Deleting all enties from db");

        if (!controller.wipeOutDb()) {
            System.out.println("print some message!");
        }
        identity.logOut();
    }


    public void switchCleanUp() {
        log.warning("switching clean up");
        controller.switchAutoClean();
    }

    public String autoCleanUpString() {
        return String.valueOf(controller.isAutoClean());
    }

     public void switchStoreman() {
        log.warning("switching storeman");
        controller.switchStoreman();
    }

    public String storemanString() {
        return String.valueOf(controller.isStoreman());
    }
    
     public void switchJmsStoreman() {
        log.warning("switching JMS storeman");
        controller.switchJmsStoreman();
    }

    public String jmsStoremanString() {
        return String.valueOf(controller.isJmsStoreman());
    }
    
    
}
