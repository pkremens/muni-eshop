package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.dao.ProductDao;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.ProductManager;
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
import javax.validation.ConstraintViolation;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<ProductDao> listAllProducts() {
        log.info("FindAllProducts");
        List<ProductDao> products = new ArrayList<ProductDao>();
        for (Product product : productManager.getProducts()) {
            products.add(new ProductDao(product));
        }
        return products;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_XML)
    public ProductDao lookupProductById(@PathParam("id") long id) {
        log.info("lookupProductById");
        return new ProductDao(productManager.getProductById(id));
    }

    @GET
    @Path("/name/{byName:[A-Za-z0-9]*}")
    @Produces(MediaType.APPLICATION_XML)
    public ProductDao lookupProductByName(@PathParam("byName") String name) {
        log.info("lookupProductByName");
        return new ProductDao(productManager.getProductByName(name));
    }

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
        log.info("Added product: " + product.toString());
        return builder.build();
    }
}
