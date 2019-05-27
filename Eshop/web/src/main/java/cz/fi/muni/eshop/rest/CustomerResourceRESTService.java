/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.dao.CustomerDao;
import cz.fi.muni.eshop.model.dao.CustomerLiteDao;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.EntityValidator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Path("/customers")
@RequestScoped
public class CustomerResourceRESTService {

    @Inject
    private Logger log;
    @EJB
    private CustomerManager customerManager;

    // curl -X POST 'http://localhost:8080/web/rest/customers/create/xasdxxx@asdsadsa.cz?name=asdsa&password=asdsad'
    @POST     // no needs for regex, validator ensures that email has correct format
    @Path("/create/{email:.*}")
    public Response createCustomer(@PathParam("email") String email,
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
        log.info("Added customert: " + customer.toString());
        return builder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<CustomerLiteDao> listAllCustomers() {
        log.info("Find all customers");
        List<CustomerLiteDao> customers = new ArrayList<CustomerLiteDao>();
        for (Customer customer : customerManager.getCustomers()) {
            customers.add(new CustomerLiteDao(customer));
        }
        return customers;
    }

    @GET  // curl 'http://localhost:8080/web/rest/customers/name/Pbun8lu5bmRd7@random.xx'
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_XML)
    public CustomerDao lookupCustomerById(@PathParam("id") long id) {
        log.info("Get customer by id: " + id);
        return new CustomerDao(customerManager.getWholeCustomerById(id));

    }

    @GET // curl 'http://localhost:8080/web/rest/customers/2'
    @Path("/name/{byEmail}")
    @Produces(MediaType.APPLICATION_XML)
    public CustomerDao lookupCustomerByEmail(@PathParam("byEmail") String email) {
        log.info("Get customer by email: " + email);
        return new CustomerDao(customerManager.getCustomerByEmail(email));
    }
}
