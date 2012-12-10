package cz.fi.muni.eshop.servlet;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.security.Authenticator;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.InvalidEntryException;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.TypeResolved;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.picketlink.idm.impl.api.PasswordCredential;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static boolean generated = false;
    // Change all this to some other presence like evn.props or sth, but should
    // stick to using constants
    private static final int PRODUCTS = 20; // change later
    private static final long PRODUCT_MAX_PRICE = 100;
    private static final long PRODUCT_MIN_PRICE = 10;
    private static final int ADMINS = 2;
    private static final int SELLERS = 5;
    private static final int BASICS = 15;
    private static final int ORDERS = 20;
    private static final int CUSTOMERS = ADMINS + SELLERS + BASICS;
    private static final int LINES = 10; // some coefficient to determine size of
    private static final int QUANTITY = 10; // TODO set some MAX nad MIN as it is with price?
    private static final Random random = new Random();
    // orders // opt
    @Inject
    @MuniEshopLogger
    private Logger log;
    @Inject
    private Credentials credentials;
    @Inject
    @TypeResolved
    private ProductManager productManager;
    @Inject
    @TypeResolved
    private CustomerManager customerManager;
    @Inject
    @TypeResolved
    private OrderManager orderManager;
    @Inject
    private Identity identity;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
        super();
    }
    @Inject
    private Authenticator authenticator;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String location = request.getContextPath() + "/index.jsf";
        if (!generated) {
            generateData();
            // TODO REMOVE, little security hack to be logged after data generation
            CustomerEntity customer = null;
            try {
                customer = customerManager.isRegistered("admin0@admin.cz");
            } catch (InvalidEntryException e) {
            }
            credentials.setUsername(customer.getEmail());
            PasswordCredential pc = new PasswordCredential("admin0");
            credentials.setCredential(pc);
            identity.login();

        }
        response.sendRedirect(location);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    private void generateData() {
        generateProducts();
        generateCustomers();
        generateOrders();
        generated = true;
    }

    private void generateProducts() {
        log.info("Generating products...");
        for (int i = 0; i < PRODUCTS; i++) {
            ProductEntity product = new ProductEntity();
            product.setProductName("product" + i);
            product.setBasePrice(PRODUCT_MIN_PRICE
                    + (long) (Math.random() * ((PRODUCT_MAX_PRICE - PRODUCT_MIN_PRICE) + 1)));
            productManager.addProduct(product);
        }
    }

    private void generateCustomers() {
        generateCustomersWithRole(Role.ADMIN, ADMINS);
        generateCustomersWithRole(Role.SELLER, SELLERS);
        generateCustomersWithRole(Role.BASIC, BASICS);
    }

    private void generateCustomersWithRole(Role role, int count) {
        log.info("generating " + role.toString());
        for (int i = 0; i < count; i++) {
            CustomerEntity customer = new CustomerEntity();
            String base;
            switch (role) {
                case ADMIN:
                    base = "admin" + i;
                    customer.setEmail(base + "@admin.cz");
                    customer.setName(base);
                    customer.setPassword(base);
                    customer.setRole(Role.ADMIN);
                    break;

                case SELLER:
                    base = "seller" + i;
                    customer.setEmail(base + "@seller.cz");
                    customer.setName(base);
                    customer.setPassword(base);
                    customer.setRole(Role.SELLER);
                    break;

                case BASIC:
                    base = "basic" + i;
                    customer.setEmail(base + "@basic.cz");
                    customer.setName(base);
                    customer.setPassword(base);
                    customer.setRole(Role.BASIC);
                    break;
            }
            customerManager.addCustomer(customer);
        }
    }

    private void generateOrders() {
        List<CustomerEntity> customer = customerManager.getCustomers();
        log.warning("Generating Orders:");
        for (CustomerEntity customerEntity : customer) {
            log.warning(customerEntity.toString());
        }
        List<ProductEntity> products = productManager.getProducts();
        for (ProductEntity productEntity : products) {
            log.warning(productEntity.toString());
        }
        
        OrderLineEntity orderline;
        OrderEntity order;
        for (int i = 0; i < ORDERS; i++) {
            order = new OrderEntity();
            order.setCustomer(customer.get(random.nextInt(CUSTOMERS)));
            for (int j = 0; j < LINES; j++) {
                
                orderline = new OrderLineEntity(products.get(random.nextInt(PRODUCTS)), (long) random.nextInt(QUANTITY + 1));
                
                order.addOrderLine(orderline);
            }
            orderManager.addOrder(order);
        }
    }
}
