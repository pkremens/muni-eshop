/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.security;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.security.permissions.Admin;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

/**
 *
 * @author Tomas Kodaj, Petr Kremensky <207855@mail.muni.cz>
 */
public class Authorization {
    
    @Inject
    @MuniEshopLogger
    Logger log;
    
    @Secures
    @Admin
    public boolean isAdmin(Identity identity) {
        return (identity.isLoggedIn() ? ((CustomerEntity)identity.getUser()).getRole().equals(Role.ADMIN) : false);
    }

}
