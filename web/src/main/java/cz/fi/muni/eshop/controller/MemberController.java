/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.playground.paymentprocessor.Asynchronous;
import cz.fi.muni.eshop.playground.paymentprocessor.PaymentProcessor;
import cz.fi.muni.eshop.model.Member;
import cz.fi.muni.eshop.service.MemberRegistration;
import java.lang.annotation.Annotation;


import javax.annotation.PostConstruct;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MemberController {

    @Inject
    private FacesContext facesContext;
    @Inject
    private MemberRegistration memberRegistration;
    private Member newMember;
    
    

    @Produces
    @Named
    public String testString(@Any Instance<PaymentProcessor> paymentProcessorSources) {
        for (PaymentProcessor payment : paymentProcessorSources) {
            System.out.println(payment.process());

        }
        Annotation qualifier = new AsynchronousAnnotation();
        PaymentProcessor p = paymentProcessorSources.select(qualifier).get();
        
// Annonymous version        
//        PaymentProcessor p = paymentProcessorSources.select(
//				new AnnotationLiteral() {
//					@Override
//					public Class annotationType() {
//						return Asynchronous.class;
//					}
//				}).get();
        
        return "Say sth my fancy runtime type resolved bean: \n" + p.process();
    }

    @Produces
    @Named
    public Member getNewMember() {
        return newMember;
    }

    public void register() throws Exception {
        memberRegistration.register(newMember);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
        initNewMember();
    }

    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }

    public class AsynchronousAnnotation extends AnnotationLiteral<Asynchronous>
            implements Asynchronous {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7063193284322241802L;
    }
}
