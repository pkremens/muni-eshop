/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.validator;

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
	 * @param entity
	 *            instance of some entity
	 * @return true if and only if the entity is valid
	 * @throws InvalidEntryException
	 *             if there are some constraint violations
	 */
	public Set<ConstraintViolation<T>> validate(T entity) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(entity);
		return constraintViolations;

	}

	/**
	 * This version ignores all violations containing id property.
	 * 
	 * @param entity
	 *            instance of some entity
	 * @return true if and only if the entity is valid
	 * @throws InvalidEntryException
	 *             if there are some constraint violations
	 */
	public List<String> validateIgnoreId(T entity) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(entity);
		List<String> violations = new ArrayList<String>();
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			// Ignore Id
			if (!constraintViolation.getPropertyPath().toString().equals("id")) {
				violations.add(constraintViolation.toString());
			}
		}
		return violations; 

	}
}
