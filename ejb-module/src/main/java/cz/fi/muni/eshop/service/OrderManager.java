/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.OrderEntity;
import java.util.List;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface OrderManager {

    void addOrder(OrderEntity order);

    void switchOrderOpen(Long id);

    OrderEntity getOrderById(Long id);

    List<OrderEntity> getOrders();

    List<OrderEntity> getActiveOrders();

    List<OrderEntity> getClosedOrders();
}
