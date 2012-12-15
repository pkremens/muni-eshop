/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.jms;

import com.sun.tools.corba.se.idl.InvalidArgument;
import cz.fi.muni.eshop.service.InvoiceManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.ControlMessage;
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
    @Inject
    private ProductManager productManager;

    @Override
    public void onMessage(Message rcvMessage) {
        try {
            if (rcvMessage instanceof MapMessage) {
                MapMessage msg = (MapMessage) rcvMessage;
                log.info("Received Message");
                StoremanMessage message = StoremanMessage.valueOf(msg.getStringProperty("type"));
                switch (message) {
                    case CLOSE_ORDER:
                        log.info(StoremanMessage.CLOSE_ORDER.getMessage());
                        long id = msg.getLongProperty("orderId");
                        log.warning("Storeman Should close order with id: " + id);
                        invoiceManager.closeOrder(id);
                        log.warning("Storeman closed order: " + id);
                        break;
                    case FILL_THE_STORE:
                        log.info(StoremanMessage.FILL_THE_STORE.getMessage());
                        productManager.refillProductWithReserved(msg.getLongProperty("productId"), 100L);
                        break;
                    default:
                        log.warning("Recieved unknown storeman message!");
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
