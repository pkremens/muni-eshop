package cz.fi.muni.eshop.rest;

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

import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.qualifier.JPA;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.TypeResolved;


@Path("/orders")
@RequestScoped
public class OrderResourceRESTService {
	@Inject
	@MuniEshopLogger
	private Logger log;
	
	@Inject
	@TypeResolved
	private OrderManager orderManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderEntity> listAllOrders() {
    	log.info("FindAllOrders");
        return orderManager.getOrders();
    }
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderEntity> listActiveProducts() {
    	log.info("FindActiveOrders");
        return orderManager.getActiveOrders();
    }
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderEntity> listClosedProducts() {
    	log.info("FindClosedOrders");
        return orderManager.getClosedOrders();
    }
    
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderEntity lookupOrderById(@PathParam("id") long id) {
		log.info("lookupOrderById");
		OrderEntity order;
		try {
		order = orderManager.getOrderById(id);
		} catch (NoResultException nre) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return order;
	}
}