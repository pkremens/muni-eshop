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

import cz.fi.muni.eshop.model.Member;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;

@Path("/products")
@RequestScoped
public class ProductResourceRESTService {
	@Inject
	@MuniEshopLogger
	private Logger log;
	
	@Inject
	@JPA
	ProductManager productManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductEntity> listAllProducts() {
    	log.info("FindAllProducts");
        return productManager.getProducts();
    }
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductEntity lookupMemberById(@PathParam("id") long id) {
		log.info("lookupProductById");
		ProductEntity product;
		try {
		product = productManager.findProductById(id);
		} catch (NoResultException nre) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return product;
	}

}
