/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.playground.translate;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
* Qualifier used to help determine the intended injection object.
*
* Qualifiers are special annotations created in a CDI application. The {@link Qualifier} annotation
* specifies this as a CDI qualifier, which is used during injection to narrow the intended type
* to be injected.
*
* This particular qualifier will be used to specify types which should be used for the Spanish language.
* @author Jason Porter
*/
@Qualifier
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Spanish {
}