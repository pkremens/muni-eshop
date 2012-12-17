package cz.fi.muni.eshop.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.DataGenerator;
import cz.fi.muni.eshop.util.Identity;

@Model
public class OrderBean {
	@Inject
	private Identity identity;
	@Inject
	private CustomerManager customerManager;
	@Inject
	private OrderManager orderManager;
	@Inject
	private DataGenerator dataGenerator;

	@Inject
	private Logger log;

	public List<Order> getCustomerOrders() {
		return customerManager.getCustomerOrders(identity.getEmail());
	}

	private boolean detail = false; // Show order detail
	private Long zoomOrderId = null;
	private Long totalPrice = 0L;

	public List<OrderItem> getItemDetails() {
            totalPrice = 0L; // to ensure correct total price even if method is called more times
		List<OrderItem> items = orderManager
				.getOrderItemsOfOrderById(zoomOrderId);
		for (OrderItem orderItem : items) {
			totalPrice += orderItem.getQuantity()
					* orderItem.getProduct().getPrice();
		}
		return items;
	}

	public void hideDetail() {
		detail = false;
		zoomOrderId = null;
		totalPrice = 0L;
	}

	public void showDetail(Long id) {
		zoomOrderId = id;
		detail = true;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public Long getZoomOrderId() {
		return zoomOrderId;
	}

	public boolean isDetail() {
		return detail;
	}

	public List<Order> getOrders() {
		return orderManager.getOrders();
	}

	public void generateRanomOrder() {
		dataGenerator.generateRandomOrder();
	}

	public void clearOrders() {
		orderManager.clearOrderTable();
	}

}
