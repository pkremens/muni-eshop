/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.dao.CustomerDao;
import cz.fi.muni.eshop.model.dao.OrderDao;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.EntityValidator;
import java.util.ArrayList;
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<Customer> listAllProducts() {
        log.info("Find all customers");
        return customerManager.getCustomers();
    }

    @GET  // curl 'http://localhost:8080/web/rest/customers/name/Pbun8lu5bmRd7@random.xx'
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_XML)
    public CustomerDao lookupCustomerById(@PathParam("id") long id) {
        log.info("Get customer by id: " + id);
//        Customer customer = customerManager.getWholeCustomerById(id);
//        log.warning(customer.getEmail());
//        log.warning(customer.getName());
//        log.warning(customer.getPassword());
//        List<Order> orders = customer.getOrder();
//        for (Order order1 : orders) {
//            log.warning(order1.getId().toString());
//            for (OrderItem item : order1.getOrderItems()) {
//                log.warning(item.toString());
//            }
//        }
//        List<Invoice> invoice = customer.getInvoice();
//        for (Invoice invoice1 : invoice) {
//            log.warning(invoice1.getId().toString());
//            for (InvoiceItem invoiceItem : invoice1.getInvoiceItems()) {
//                log.warning(invoiceItem.toString());
//            }
//        }
//
//        CustomerDao cao = new CustomerDao();
//        cao.setId(customer.getId());
//        cao.setName(customer.getName());
//        cao.setPassword(customer.getPassword());
//        cao.setEmail(customer.getEmail());
//        List<OrderDao> daorder = new ArrayList<OrderDao>();
//        for (Order order : customer.getOrder()) {
//            daorder.add(new OrderDao(order, customer));
//        }
//        cao.setOrder(daorder);
//

        return new CustomerDao(customerManager.getWholeCustomerById(id));

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
