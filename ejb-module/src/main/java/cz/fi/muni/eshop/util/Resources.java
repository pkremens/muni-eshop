package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.basket.BasketManager;
import cz.fi.muni.eshop.util.annotation.JPAAnnotation;
import cz.fi.muni.eshop.util.annotation.ListWithProductsAnnotation;
import cz.fi.muni.eshop.util.annotation.MapWithProductsAnnotation;
import cz.fi.muni.eshop.util.annotation.SetWithProductsAnnotation;
import cz.fi.muni.eshop.util.qualifier.MuniEshopDatabase;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.TypeResolved;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 *
 * <p> Example injection on a managed bean field: </p>
 *
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources implements Serializable { // TODO try remove JMT TEST
    // use @SuppressWarnings to tell IDE to ignore warnings about field not being referenced directly
//   @SuppressWarnings("unused")
//   @Produces
//   @PersistenceContext
//   private EntityManager em;

    private static int BASKET_TYPE = 2;
    @SuppressWarnings("unused")
    @Produces
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @SuppressWarnings("unused")
    @Produces
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    @Produces
    @MuniEshopLogger
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
    @PersistenceContext
    @Produces
    @MuniEshopDatabase
    private EntityManager em;

    @Produces
    @TypeResolved // Possible to change implementation later using eg. switch case, could use some constants .. TODO
    public CustomerManager resolveCustomerManager(@Any Instance<CustomerManager> customerManagerSources) {
        Annotation qualifier = new JPAAnnotation();
        CustomerManager customerManager = customerManagerSources.select(qualifier).get();
        return customerManager;
    }

    @Produces
    @TypeResolved
    public ProductManager resolveProductManager(@Any Instance<ProductManager> productManagerSources) {
        Annotation qualifier = new JPAAnnotation();
        ProductManager productManager = productManagerSources.select(qualifier).get();
        return productManager;
    }

    @Produces
    @TypeResolved
    public OrderManager resolveOrderManager(@Any Instance<OrderManager> orderManagerSources) {
        Annotation qualifier = new JPAAnnotation();
        OrderManager orderManager = orderManagerSources.select(qualifier).get();
        return orderManager;
    }

    @Produces
    @TypeResolved
    public BasketManager resolveBasketManager(@Any Instance<BasketManager> basketManagerSources) {
        Annotation qualifier = null;
        switch (BASKET_TYPE) {
            case 0:
                qualifier = new ListWithProductsAnnotation();
                break;
            case 1:
                qualifier = new SetWithProductsAnnotation();
                break;
            case 2:
                qualifier = new MapWithProductsAnnotation();
                break;
            default:
                throw new IllegalStateException("No basket implementation selected");
        }
        BasketManager basketManager = basketManagerSources.select(qualifier).get();
        return basketManager;
    }
//        Annonymou version, but soon as JPA type is called so often it make sense to create annotation type
//        CustomerManager customerManager = customerManagerSources.select(
//                new AnnotationLiteral() {
//                    @Override
//                    public Class annotationType() {
//                        return JPA.class;
//                    }
//                }).get();
//        return customerManager;
//    }
    // This doesn't have sense as entity manager lifecycle is controlled by container (throws exceptions)
//    @Produces
//    @MuniEshopDatabase
//    public EntityManager create() {
//        return em;
//    }
//
//    public void close(@Disposes @MuniEshopDatabase EntityManager em) {
//        em.close();
//    }
}
