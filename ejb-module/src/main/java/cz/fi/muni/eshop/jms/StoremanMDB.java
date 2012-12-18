/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.jms;

import cz.fi.muni.eshop.service.InvoiceManager;
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
@MessageDriven(name = "StoremanMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class StoremanMDB implements MessageListener {

    @Inject
    private Logger log;
    @Inject
    private InvoiceManager invoiceManager;

    @Override
    public void onMessage(Message rcvMessage) {
        try {
            if (rcvMessage instanceof MapMessage) {
                MapMessage msg = (MapMessage) rcvMessage;
                //  log.info("Received Message");
                StoremanMessage message = StoremanMessage.valueOf(msg.getStringProperty("type"));
                switch (message) {
                    case CLOSE_ORDER:
                        long id = msg.getLongProperty("orderId");
                        invoiceManager.closeOrder(id);
                        log.warning("Order closed id: " + id);
                        break;
                    default:
                        throw new IllegalArgumentException("Recieved unknown storeman message!");
                }
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
