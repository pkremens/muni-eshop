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
            throw new InvalidEntryException(entity.getClass(),violations);
        }
    }
    // TODO for JPA, because is null before inserting to DB
    public boolean validateIgnoreId(T entity) throws InvalidEntryException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
