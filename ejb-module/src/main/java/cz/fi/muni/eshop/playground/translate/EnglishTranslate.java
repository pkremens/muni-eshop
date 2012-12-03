/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.playground.translate;

/**
* A simple implementation for the English language.
*
* The {@link English} qualifier tells CDI this is a special instance of
* the {@link TranslateService}.
*
* @author Jason Porter
*
*/
@English
public class EnglishTranslate implements TranslateService {

   @Override
   public String hello() {
     return "Hello";
   }

}
