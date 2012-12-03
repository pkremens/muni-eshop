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
package cz.fi.muni.eshop.test;

import cz.fi.muni.eshop.data.member.MemberListProducer;
import cz.fi.muni.eshop.data.member.MemberRepository;
import cz.fi.muni.eshop.model.Member;
import cz.fi.muni.eshop.service.MemberRegistration;
import cz.fi.muni.eshop.testpackage.Dummy;
import cz.fi.muni.eshop.util.Resources;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MemberRegistrationTest {
   @Deployment
   public static Archive<?> createTestArchive() {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Member.class, MemberRegistration.class, Resources.class, Dummy.class, MemberListProducer.class, MemberRepository.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            // Deploy our test datasource
            .addAsWebInfResource("test-ds.xml", "test-ds.xml");
   }

   @Inject
   MemberRegistration memberRegistration;

   @Inject
   MemberListProducer memberListProducer;
   
   @Inject
   Logger log;

   @Test
   @InSequence(1)
   public void testListProducerBefore() {
       Assert.assertTrue(memberListProducer.getMembers().isEmpty());
   }
   
   @Test
   @InSequence(2)
   public void testRegister() throws Exception {
      Member newMember = new Member();
      newMember.setName("Jane Doe");
      newMember.setEmail("jane@mailinator.com");
      newMember.setPhoneNumber("2125551234");
      memberRegistration.register(newMember);
      assertNotNull(newMember.getId());
      log.info(newMember.getName() + " was persisted with id " + newMember.getId());
   }
   
   @Test
   @InSequence(3)
   public void testListProducerAfter() {
       Member member = memberListProducer.getMembers().get(0);
       Assert.assertTrue(member.getName().equals("Jane Doe"));
   }
   
}
