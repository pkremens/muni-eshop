/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.jms;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.ConnectionFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@WebServlet("/JmsTestServlet")
public class JmsTestServlet extends HttpServlet {

    private static final int MSG_COUNT = 5; // TODO what is this for???
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    public JmsTestServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection connection = null;
        out.write("<h3>Servlet and MDB test</h3>");
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            connection.start();

            MapMessage message = session.createMapMessage();
            out.write("Sending object");
            out.write("<br><b>See console for more</b>");
            message.setStringProperty("type", "JMS_CONTROL_MESSAGE");
            messageProducer.send(message);

        } catch (JMSException e) {
            e.printStackTrace();
            out.write("<h2>A problem occurred during the delivery of this message</h2>");
            out.write("</br>");
            out.write("<p><i>Go your the JBoss Application Server console or Server log to see the error stack trace</i></p>");
            out.write("</br><a href=\"/eshop/listBooks.jsf\">");
            out.write("list books</a>");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
