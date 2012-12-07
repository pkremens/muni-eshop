/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.security;

import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;

/**
 *
 * @author Tomas Kodaj, Petr Kremensky <207855@mail.muni.cz>
 */
public class Authenticator extends BaseAuthenticator{
    @Inject
    private Credentials credentials;
    
    @Inject
    private FacesContext facesContext;
    
    @Inject
    @JPA
    private CustomerManager customerManager;
    
    // TODO finish me
    @Override
    public void authenticate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
