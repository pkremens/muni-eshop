package cz.fi.muni.eshop.util;

/*
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and/or
 * its affiliates, and individual contributors by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of individual
 * contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
import cz.fi.muni.eshop.playground.paymentprocessor.Asynchronous;
import cz.fi.muni.eshop.playground.paymentprocessor.PaymentProcessor;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.annotation.JPAAnnotation;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.quilifier.MuniEshopDatabase;
import cz.fi.muni.eshop.util.quilifier.TypeResolved;
import java.lang.annotation.Annotation;
import java.util.logging.Logger;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
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
public class Resources {
    // use @SuppressWarnings to tell IDE to ignore warnings about field not being referenced directly
//   @SuppressWarnings("unused")
//   @Produces
//   @PersistenceContext
//   private EntityManager em;



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
    public ProductManager resolveproductManager(@Any Instance<ProductManager> productManagerSources) {
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
    
//        Annonymou version, but soon as JPA type is called so ofted it make sence to create annotation type
//        CustomerManager customerManager = customerManagerSources.select(
//                new AnnotationLiteral() {
//                    @Override
//                    public Class annotationType() {
//                        return JPA.class;
//                    }
//                }).get();
//        return customerManager;
//    }
    
    
    
    
    
    
    // This doesn't have sense as entity manager lifecycle is controled by container (throws exeptions)
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
