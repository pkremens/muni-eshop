/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.util.Controller;
import cz.fi.muni.eshop.util.DataGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Path("/controller")
@RequestScoped
public class ControllerResourceRESTService {

    @Inject
    private Logger log;
    @EJB
    private Controller controller;
    @Inject
    private DataGenerator dataGenerator;

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/products/random/3
    @Path("/storeman/{bool:[01]}")
    public Response setStoremanService(@PathParam("bool") Long bool) {
        Response.ResponseBuilder builder = null;
        log.warning("Setting storeman service to: " + ((bool == 1) ? "on" : "off"));
        controller.setStoreman((bool == 1) ? true : false);
        builder = Response.ok();
        return builder.build();
    }

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/products/random/3
    @Path("/autoclean/{bool:[01]}")
    public Response setAutocleanService(@PathParam("bool") Long bool) {
        Response.ResponseBuilder builder = null;
        log.warning("Setting autoclean service to: " + ((bool == 1) ? "on" : "off"));
        controller.setAutoClean((bool == 1) ? true : false);
        builder = Response.ok();
        return builder.build();
    }

    /**
     * Service for cleaning DB
     *
     * @param bool 0 for Invoice and Order, 1 for all
     * @return
     */
    @POST
    // curl -i -X POST http://localhost:8080/web/rest/products/random/3
    @Path("/wipeout/{bool:[01]}")
    public Response wipeOutDB(@PathParam("bool") Long bool) {
        Response.ResponseBuilder builder = null;
        log.warning("Wiping out DB: " + ((bool == 1) ? "all tables" : "just Orders and Invoices"));
        if (bool == 0) {
            controller.cleanInvoicesAndOrders();
        } else {
            controller.wipeOutDb();
        }
        builder = Response.ok();
        return builder.build();
    }

    @GET
    public String printReport() {
        log.info("Printing report");
        StringBuilder sb = new StringBuilder();
        return controller.report();
    }

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/customers/random/3
    @Path("/randomCustomers/{count:[1-9][0-9]*}")
    public Response createRandomCustomers(@PathParam("count") Long count) {
        log.warning("create random customer(s): " + count);
        Response.ResponseBuilder builder = null;
        try {
            for (int i = 0; i < count; i++) {
                dataGenerator.generateRandomCustomer();
            }
        } catch (Exception ex) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", ex.getMessage());
            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj);
        }
        builder = Response.ok();
        return builder.build();
    }

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/products/random/3
    @Path("/randomProducts/{count:[1-9][0-9]*}")
    public Response createRandomProducts(@PathParam("count") Long count) {
        log.warning("create random product(s): " + count);
        Response.ResponseBuilder builder = null;
        try {
            for (int i = 0; i < count; i++) {
                dataGenerator.generateRandomProduct();
            }
        } catch (Exception ex) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", ex.getMessage());
            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj);
        }

        builder = Response.ok();
        return builder.build();
    }

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/products/random/3
    @Path("/randomOrders/{count:[1-9][0-9]*}")
    public Response createRandomOrders(@PathParam("count") Long count) {
        log.warning("create random product(s): " + count);
        Response.ResponseBuilder builder = null;
        
        try {
            for (int i = 0; i < count; i++) {
                dataGenerator.generateRandomOrder();
            }
        } catch (Exception ex) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", ex.getMessage());
            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj);
        }
        builder = Response.ok();
        return builder.build();
    }
}
