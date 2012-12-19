/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.dao.InvoiceDao;
import cz.fi.muni.eshop.model.dao.OrderDao;
import cz.fi.muni.eshop.service.OrderManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Path("/orders")
@RequestScoped
public class OrderResourcesRESTService {

    @Inject
    private Logger log;
    @EJB
    private OrderManager orderManager;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<OrderDao> listAllOrders() {
        log.info("Find all orderss");
        List<OrderDao> orders = new ArrayList<OrderDao>();
        List<Long> ids = orderManager.getOrderIds();
        for (Long id : ids) {
            // orders.add(new OrderDao(orderManager.get
        }
        return orders;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_XML)
    public OrderDao lookupOrderById(@PathParam("id") long id) {
        log.info("Get order by id: " + id);
        //return new orderDao(OrderManager.getWholeOrderById(id));
        return null;
    }
}
