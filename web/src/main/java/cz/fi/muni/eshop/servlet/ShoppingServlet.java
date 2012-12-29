/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.servlet;

import cz.fi.muni.eshop.controller.BasketBean;
import cz.fi.muni.eshop.controller.LoginBean;
import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.Identity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pkremens
 */
@WebServlet(name = "ShoppingServlet", urlPatterns = {"/ShoppingServlet"})
public class ShoppingServlet extends HttpServlet {

    @Inject
    private Identity identity;
    @EJB
    private CustomerManager customerManager;
    @Inject
    private LoginBean loginBean;
    @EJB
    private ProductManager productManager;
    @EJB
    private OrderManager orderManager;
    @Inject
    private BasketBean basketBean;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!identity.isLoggedIn()) {
            Customer customer = customerManager.getRandomCustomer();
            if (customer == null) {
                return;
            }
            loginBean.setEmail(customer.getEmail());
            loginBean.setPassword(customer.getPassword());
            loginBean.logIn();
        }
        long count = productManager.getProductTableCount();
        if (count == 0) {
            return; // TODO sth
        }
        if (count > 5) {
            count = 5;
        }
        for (int i = 0; i < count; i++) {
            // new product object is created in the basket
            basketBean.addToBasket(productManager.getRandomProduct());
        }
        basketBean.makeOrder();
        loginBean.logOut();
                String location = request.getContextPath() + "/orders.jsf";
        response.sendRedirect(location);

//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            /*
//             * TODO output your page here. You may use following sample code.
//             */
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet ShoppingServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet ShoppingServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {
//            out.close();
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
