package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.service.ProductManager;
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

@Path("/products")
@RequestScoped
public class ProductResourceRESTService {

    @Inject
    private Logger log;
    @Inject
    private ProductManager productManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listAllProducts() {
        log.info("FindAllProducts");
        return productManager.getProducts();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product lookupProductById(@PathParam("id") long id) {
        log.info("lookupProductById");
        Product product;
        try {
            product = productManager.getProductById(id);
        } catch (NoResultException nre) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return product;
    }
}
