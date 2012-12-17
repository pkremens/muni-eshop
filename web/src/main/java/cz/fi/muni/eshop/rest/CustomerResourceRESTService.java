/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.service.CustomerManager;
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
    private Logger log;
    @Inject
    private CustomerManager customerManager;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<Customer> listAllProducts() {
//        log.info("FindAllCustomers");
//        return customerManager.getCustomers();
        return null; // TODO
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{email:^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$}") 
    public Customer lookupCustomerById(@PathParam("email") String email) {
//        log.info("lookupProductById");
//        Customer customer;
//        try {
//            customer = customerManager.getCustomerByEmail(email);
//        } catch (NoResultException nre) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//        return customer;
        return null; // TODO
    }
}
