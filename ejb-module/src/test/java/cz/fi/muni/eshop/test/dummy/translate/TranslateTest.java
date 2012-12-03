/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.dummy.translate;

import cz.fi.muni.eshop.playground.translate.English;
import cz.fi.muni.eshop.playground.translate.EnglishTranslate;
import cz.fi.muni.eshop.playground.translate.Spanish;
import cz.fi.muni.eshop.playground.translate.SpanishTranslate;
import cz.fi.muni.eshop.playground.translate.TranslateService;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author pkremens
 */
@RunWith(Arquillian.class)
public class TranslateTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(English.class, EnglishTranslate.class, Spanish.class, SpanishTranslate.class, TranslateService.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }
    @Inject
    @English
    private TranslateService englishTranslateService;
    
    @Inject @Spanish
    private TranslateService spanishTranslateService;

    @Test
    public void englishBeanTest() {
        String message = englishTranslateService.hello();
        Assert.assertTrue("Should return \"Hello\", but found: " + message, message.equals("Hello"));
    }
    
    @Test
    public void spanishBeanTest() {
        String message = spanishTranslateService.hello();
        Assert.assertTrue("Should return \"Hola\", but found: " + message, message.equals("Hola"));
    }
}
