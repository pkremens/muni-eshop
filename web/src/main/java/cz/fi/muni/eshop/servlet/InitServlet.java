package cz.fi.muni.eshop.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static boolean generated = false;
	private static final int PRODUCTS = 20; // change later 
	private static final int CUSTOMERS = 20; // change later
	private static final int ORDERS = 20;
	private static final int LINES = 5; // some coeficient to determine size of orders // opt
	
	@Inject
	@MuniEshopLogger
	private Logger log;
	
	@Inject
	@JPA
	private ProductManager productManager;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitServlet() {
		super();
		//generateData();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String location = request.getContextPath() + "/index.jsf";
		System.out.println("generated?: " + generated);
		response.sendRedirect(location);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void generateData() {
		generateProducts();
	}
	
	private void generateProducts() {
		log.info("Generating products...");
		
		
	}

}
