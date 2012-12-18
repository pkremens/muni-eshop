/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.DataGenerator;
import cz.fi.muni.eshop.util.EntityValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    @EJB
    private CustomerManager customerManager;
    @Inject
    private DataGenerator datagenerator;

    // curl -X POST 'http://localhost:8080/web/rest/customers/create/xasdxxx@asdsadsa.cz?name=asdsa&password=asdsad'
    @POST     // no needs for regex, validator ensures that email has correct format
    @Path("/create/{email:.*}")
    public Response createProduct(@PathParam("email") String email,
            @QueryParam("name") String name,
            @QueryParam("password") String password) {
        Response.ResponseBuilder builder = null;

        Customer customer = new Customer(email, name, password);
        EntityValidator<Customer> validator = new EntityValidator<Customer>();
        Set<ConstraintViolation<Customer>> violations = validator.validateIgnoreId(customer);
        if (violations.isEmpty()) {
            try {
                customer = customerManager.addCustomer(email, name, password);
            } catch (Exception ex) {
                log.warning(ex.getMessage());
            }
            if (customer != null) {
                builder = Response.ok();
            }
        } else {
            Map<String, String> responseObj = new HashMap<String, String>();
            for (ConstraintViolation<Customer> constraintViolation : violations) {
                responseObj.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessageTemplate());
            }
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        log.warning("Added product: " + customer.toString());
        return builder.build();
    }

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/customers/random/3
    @Path("/random/{count:[1-9][0-9]*}")
    public Response createRandomProducts(@PathParam("count") long count) {
        log.warning("create random customer(s): " + count);
        Response.ResponseBuilder builder = null;
        try {
            for (int i = 0; i < count; i++) {
                datagenerator.generateRandomCustomer();
            }
        } catch (Exception ex) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", ex.getMessage());
            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj);
        }
        builder = Response.ok();
        return builder.build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<Customer> listAllProducts() {
        log.info("Find all customers");
        return customerManager.getCustomers();
    }

    @GET  // curl 'http://localhost:8080/web/rest/customers/name/Pbun8lu5bmRd7@random.xx'
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.TEXT_PLAIN)
    public Customer lookupCustomerById(@PathParam("id") long id) {
        log.info("Get customer by id: " + id);
        Customer customer;
        try {
            customer = customerManager.getCustomerById(id);
        } catch (NoResultException nre) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return customer;
    }

    @GET // curl 'http://localhost:8080/web/rest/customers/2'
    @Path("/name/{byEmail}")
    @Produces(MediaType.TEXT_PLAIN)
    public Customer lookupProductByEmail(@PathParam("byEmail") String email) {
        log.info("Get customer by email: " + email);
        Customer customer;
        try {
            customer = customerManager.getCustomerByEmail(email);
        } catch (NoResultException nre) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return customer;
    }
}
