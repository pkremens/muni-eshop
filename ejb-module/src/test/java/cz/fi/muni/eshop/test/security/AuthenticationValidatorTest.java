/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.security;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.util.Role;
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
        CustomerEntity customer = new CustomerEntity("", "name", "password", Role.BASIC);
        Set<ConstraintViolation<CustomerEntity>> constraintViolations =
                validator.validate(customer);
        Assert.assertEquals(2, constraintViolations.size());
//        for (ConstraintViolation<CustomerEntity> constraintViolation : constraintViolations) {
//            System.out.println(constraintViolation);
//        }                
    }
}
