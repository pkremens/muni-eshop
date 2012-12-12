/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.security;

import cz.fi.muni.eshop.model.Customer;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class AuthenticationValidatorTest {

    private static Validator validator;

    @Test
    public void invalidEmailTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Customer customer = new Customer("", "name", "password");
        Set<ConstraintViolation<Customer>> constraintViolations =
                validator.validate(customer);
        Assert.assertEquals(2, constraintViolations.size());
    }
}
