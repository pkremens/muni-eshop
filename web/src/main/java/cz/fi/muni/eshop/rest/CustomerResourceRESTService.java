/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.InvalidEntryException;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Path("/customers")
@RequestScoped
public class CustomerResourceRESTService {

    @Inject
    @MuniEshopLogger
    private Logger log;
    @Inject
    @JPA
    private CustomerManager customerManager;
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerEntity> listAllProducts() {
        log.info("FindAllCustomers");
        return customerManager.getCustomers();
    }

    @GET
    @Path("/{email:.*}") // TODO fix pattern
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerEntity lookupMemberById(@PathParam("email") String email) {
        log.info("lookupProductById");
        CustomerEntity customer;
        try {
            customer = customerManager.isRegistered(email);
            if (customer == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
        } catch (InvalidEntryException nre) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return customer;
    }
}
