/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.security;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.security.permissions.Admin;
import cz.fi.muni.eshop.security.permissions.BasicPermission;
import cz.fi.muni.eshop.security.permissions.Seller;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
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

    /**
     *
     * @param identity
     * @return true, if user has ADMIN role, else false
     */
    @Secures
    @Admin
    public boolean isAdmin(Identity identity) {
        return (identity.isLoggedIn() ? ((CustomerEntity) identity.getUser()).getRole().equals(Role.ADMIN) : false);
    }

    /**
     *
     * @param identity
     * @return true, if user has SELLER role, else false
     */
    @Secures
    @Seller
    public boolean isSeller(Identity identity) {
        return (identity.isLoggedIn() ? ((CustomerEntity) identity.getUser()).getRole().equals(Role.SELLER) : false);
    }

    /**
     *
     * @param identity
     * @return true, if user has BASIC role, else false
     */
    @Secures
    @BasicPermission
    public boolean isBasic(Identity identity) {
        return (identity.isLoggedIn() ? ((CustomerEntity) identity.getUser()).getRole().equals(Role.BASIC) : false);
    }
}
