/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller.test;
@Asynchronous
public class AsynchronousPaymentProcessor implements PaymentProcessor {

	@Override
	public String process() {
		return "Async";
	}

}

