/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.playground.translate;

/**
 * A simple implementation for the Spanish language.
 * 
* The {@link Spanish} qualifier tells CDI this is a special instance of the {@link TranslateService}.
 * 
* @author Jason Porter
 */
@Spanish
public class SpanishTranslate implements TranslateService {

    @Override
    public String hello() {
        return "Hola";
    }
}
