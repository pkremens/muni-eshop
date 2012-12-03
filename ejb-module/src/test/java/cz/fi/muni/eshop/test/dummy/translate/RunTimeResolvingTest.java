/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.dummy.translate;

import cz.fi.muni.eshop.playground.translate.English;
import cz.fi.muni.eshop.playground.translate.EnglishTranslate;
import cz.fi.muni.eshop.playground.translate.EnglishQualifier;
import cz.fi.muni.eshop.playground.translate.Spanish;
import cz.fi.muni.eshop.playground.translate.SpanishTranslate;
import cz.fi.muni.eshop.playground.translate.TranslateService;
import java.lang.annotation.Annotation;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author pkremens
 */
public class RunTimeResolvingTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(English.class, EnglishQualifier.class, EnglishTranslate.class, Spanish.class, SpanishTranslate.class, TranslateService.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }
//    @Inject @Any Instance<TranslateService> translateService;
    @Inject
    @Any
    private Instance<TranslateService> translationServiceResource;

    @Test
    @Ignore("No beans found = NullPoiterExeption")
    public void sTest() {
        System.out.println("AAA");
        for (TranslateService translateService : translationServiceResource) {
            Assert.assertFalse(translateService.hello().isEmpty());
        }
        Assert.assertTrue(translationServiceResource.isAmbiguous());
        System.out.println("AAA");
        Annotation quilifier = new EnglishQuilifierImpl();
        TranslateService translateService = translationServiceResource.select(quilifier).get();
        String message = translateService.hello();
        Assert.assertTrue("Should return \"Hello\", but found: " + message, message.equals("Hello"));
    }

    public class EnglishQuilifierImpl extends AnnotationLiteral<English> implements English {
    }
}
// // Working
// @Inject
// @Synchronous
// PaymentProcessor syncPaymentProcessor;
// @Inject
// @Asynchronous
// PaymentProcessor asyncPaymentProcessor;
//
// @Produces
// @Named
// public String testString() {
// System.out.println(syncPaymentProcessor.process());
// System.out.println(asyncPaymentProcessor.process());
// return "blala";
// }
//
// @Inject
// @Asynchronous
// Instance<PaymentProcessor> paymentProcessorSource;
//
// public String testString() {
// PaymentProcessor pp = paymentProcessorSource.get();
// return pp.process();
// }
// WORKING
// @Produces
// @Named
// public String testString(@Synchronous PaymentProcessor
// syncPaymentProcessor) {
// return syncPaymentProcessor.process();
// }
//	void initServices(@Any Instance<PaymentProcessor> paymentProcessorSource) {
//		for (PaymentProcessor p : paymentProcessorSource) {
//			System.out.println(p.process());
//		}
//	}