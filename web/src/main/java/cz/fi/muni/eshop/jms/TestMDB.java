/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.jms;

import cz.fi.muni.eshop.util.ControlMessage;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@MessageDriven(name = "TestMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class TestMDB implements MessageListener {

    @Inject
    @MuniEshopLogger
    private Logger log;

    @Override
    public void onMessage(Message rcvMessage) {
        try {
            if (rcvMessage instanceof MapMessage) {
                MapMessage msg = (MapMessage) rcvMessage;
                log.info("Received Message");
                ControlMessage message = ControlMessage.valueOf(msg.getStringProperty("type"));

                log.info(message.toString());
            } else {
                log.warning("Message of wrong type: "
                        + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
