/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util.exceptions;

import java.util.List;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class InvalidEntryException extends Exception {

    private Class entityClass;
    private List<String> violations;

    public InvalidEntryException(Class entityClass, List<String> violations) {
        this.entityClass = entityClass;
        this.violations = violations;
    }

    @Override
    public String getMessage() {
        return "Found constraint violations for entity type: " + entityClass.getName() + "\n" + violations.toString();
    }
}