/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class EntityValidator<T> {

    /**
     *
     * @param entity instance of some entity
     * @return true if and only if the entity is valid
     * @throws InvalidEntryException if there are some constraint violations
     */
    public boolean validate(T entity) throws InvalidEntryException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations =
                validator.validate(entity);
        if (constraintViolations.isEmpty()) {
            return true;
        } else {
            List<String> violations = new ArrayList<String>();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                violations.add(constraintViolation.toString());
            }
            throw new InvalidEntryException(entity.getClass(), violations);
        }
    }

    /**
     * This version ignores all violations containing id property.
     * @param entity instance of some entity
     * @return true if and only if the entity is valid
     * @throws InvalidEntryException if there are some constraint violations
     */
    public boolean validateIgnoreId(T entity) throws InvalidEntryException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations =
                validator.validate(entity);
        List<String> violations = new ArrayList<String>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            // Ignore Id
            if (!constraintViolation.getPropertyPath().toString().equals("id")) {
                violations.add(constraintViolation.toString());
            }
        }        
        if (violations.isEmpty()) {
            return true;
        } else {
            throw new InvalidEntryException(entity.getClass(),violations);
        }
        
    }
}
