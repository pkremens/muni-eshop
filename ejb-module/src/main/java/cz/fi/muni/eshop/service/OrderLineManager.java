/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.OrderLineEntity;
import java.util.List;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface OrderLineManager {

    void addOrderLine(OrderLineEntity orderLine);

    List<OrderLineEntity> getOrderLines();
}