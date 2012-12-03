/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.playground.paymentprocessor;
@Asynchronous
public class AsynchronousPaymentProcessor implements PaymentProcessor {

	@Override
	public String process() {
		return "I am your fancy runtime type resolved bean annotated with @Asynchronous... althougt there is nothing asynchronous on me.";
	}

}

