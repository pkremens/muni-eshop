package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.ProductManager;
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

@Path("/products")
@RequestScoped
public class ProductResourceRESTService {

    // curl:
    // The -i flag tells cURL to print the returned headers. Notice that the Location header contains the URI of the resource corresponding to the new task you have just created.
    // The -u flag provides the authentication information for the request.
    // The -H flag adds a header to the outgoing request.
    // The -X flag tells cURL which HTTP method to use. The HTTP POST is used to create resources.
    // The Location header of the response contains the URI of the resource representing the newly created task.
    @Inject
    private Logger log;
    @EJB
    private ProductManager productManager;
    @Inject
    private DataGenerator datagenerator;

    // BACHA NA UVOZOVKY!!!!
    // curl -X POST 'http://localhost:8080/web/rest/products/create/xasdxxx?price=343&stored=123&type=4'
    @POST
    @Path("/create/{name:[A-Za-z0-9]*}")
    public Response createProduct(@PathParam("name") String name,
            @QueryParam("price") @DefaultValue("1") Long price,
            @QueryParam("type") @DefaultValue("1") Integer type,
            @QueryParam("stored") @DefaultValue("100") Long stored) {
        Response.ResponseBuilder builder = null;
        Category category;
        switch (type) {
            case 1:
                category = Category.TYPE1;
                break;
            case 2:
                category = Category.TYPE2;
                break;
            case 3:
                category = Category.TYPE3;
                break;
            case 4:
                category = Category.TYPE4;
                break;
            case 5:
                category = Category.TYPE5;
                break;
            case 6:
                category = Category.TYPE6;
                break;
            case 7:
                category = Category.TYPE7;
                break;
            default:
                throw new IllegalArgumentException("Invalid type entered, could be number from 1-7");

        }
        Product product = new Product(name, price, category, stored, 0L);
        EntityValidator<Product> validator = new EntityValidator<Product>();
        Set<ConstraintViolation<Product>> violations = validator.validateIgnoreId(product);
        if (violations.isEmpty()) {
            try {
                product = productManager.addProduct(name, price, product.getCategory(), stored, 0L);
            } catch (Exception ex) {
                log.warning(ex.getMessage());
            }
            if (product != null) {
                builder = Response.ok();
            }
        } else {
            Map<String, String> responseObj = new HashMap<String, String>();
            for (ConstraintViolation<Product> constraintViolation : violations) {
                responseObj.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessageTemplate());
            }
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        log.warning("Added product: " + product.toString());
        return builder.build();
    }

    @POST
    // curl -i -X POST http://localhost:8080/web/rest/products/random/3
    @Path("/random/{count:[1-9][0-9]*}")
    public Response createRandomProducts(@PathParam("count") long count) {
        log.warning("create random product(s): " + count);
        Response.ResponseBuilder builder = null;
        try {
            for (int i = 0; i < count; i++) {
                datagenerator.generateRandomProduct();
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
    public List<Product> listAllProducts() {
        log.info("FindAllProducts");
        return productManager.getProducts();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.TEXT_PLAIN)
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

    @GET
    @Path("/name/{byName:[A-Za-z0-9]*}")
    @Produces(MediaType.TEXT_PLAIN)
    public Product lookupProductByName(@PathParam("byName") String name) {
        log.info("lookupProductByName");
        Product product;
        try {
            product = productManager.getProductByName(name);
        } catch (NoResultException nre) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return product;
    }

//    @DELETE
//    // curl -i -X DELETE http://localhost:8080/web/rest/products/name/proKb8MEioeh
//    @Path("/name/{byName:[A-Za-z0-9]*}")
//    public void deleteProductByName(@PathParam("byName") String name) {
//        log.warning("Delete product here"); // I don't think I want this!
//    }
}
