/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.playground.paymentprocessor;
@Synchronous
public class SynchronousPaymentProcessor implements PaymentProcessor {

	public String process() {
		return "Synch";
	}
}