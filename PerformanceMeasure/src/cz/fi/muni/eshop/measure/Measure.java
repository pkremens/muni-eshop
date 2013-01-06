/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.measure;

import java.util.Calendar;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.MBeanServerConnection;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.ObjectName;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>, Jan Martiska
 * <jmartiska@redhat.com>,
 */
public class Measure {

    private static final String host = "localhost";
    private static int port; // management-native port
    private static String urlString;
    private final MBeanServerConnection connection;
    private final JMXConnector jmxConnector;

    public Measure(Server server) throws Exception {
        switch (server) {
            case JBOSSAS7:
                port = 9999;
                urlString = System.getProperty("jmx.service.url", "service:jmx:remoting-jmx://" + host + ":" + port);
                break;
            case GLASSFISH3:
                port = 8686;
                urlString = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port +"/jmxrmi";
                break;
            default:
                throw new IllegalArgumentException("Unknow type of server");
        }


        JMXServiceURL serviceURL = new JMXServiceURL(urlString);
        jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
        connection = jmxConnector.getMBeanServerConnection();
    }

    public void destroy() throws Exception {
        jmxConnector.close();
    }

    public double getFootprint() throws Exception {
        for (int i = 0; i < 8; i++) {
            System.out.println("Forcing garbage collection");
            forceGC();
            System.out.println("Forced garbage collection");
        }
        return performOneMeasurement();
    }

    private double performOneMeasurement() throws Exception {
        CompositeDataSupport edenSpace;
        edenSpace = (CompositeDataSupport) connection.getAttribute(new ObjectName("java.lang:type=MemoryPool,name=PS Eden Space"), "Usage");
        Long edenSpaceUsed = (Long) edenSpace.get("used");

        CompositeDataSupport oldGen;
        oldGen = (CompositeDataSupport) connection.getAttribute(new ObjectName("java.lang:type=MemoryPool,name=PS Old Gen"), "Usage");
        Long oldGenUsed = (Long) oldGen.get("used");

        CompositeDataSupport permGen;
        permGen = (CompositeDataSupport) connection.getAttribute(new ObjectName("java.lang:type=MemoryPool,name=PS Perm Gen"), "Usage");
        Long permGenUsed = (Long) permGen.get("used");

        CompositeDataSupport survivorSpace;
        survivorSpace = (CompositeDataSupport) connection.getAttribute(new ObjectName("java.lang:type=MemoryPool,name=PS Survivor Space"), "Usage");
        Long survivorSpaceUsed = (Long) survivorSpace.get("used");

        CompositeDataSupport heapMemory;
        heapMemory = (CompositeDataSupport) connection.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
        Long heapMemoryUsed = (Long) heapMemory.get("used");

        CompositeDataSupport nonHeapMemory;
        nonHeapMemory = (CompositeDataSupport) connection.getAttribute(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage");
        Long nonHeapMemoryUsed = (Long) nonHeapMemory.get("used");

        System.out.println("Current Time:    " + Calendar.getInstance().getTime() + " kbytes");
        System.out.println("Eden space:      " + (edenSpaceUsed / 1000) + " kbytes");
        System.out.println("Old gen:         " + (oldGenUsed / 1000) + " kbytes");
        System.out.println("Perm gen:        " + (permGenUsed / 1000) + " kbytes");
        System.out.println("Survivor space:  " + (survivorSpaceUsed / 1000) + " kbytes");
        System.out.println("Heap memory:     " + (heapMemoryUsed / 1000) + " kbytes");
        System.out.println("NonHeap memory:  " + (nonHeapMemoryUsed / 1000) + " kbytes");
        Long total = edenSpaceUsed + oldGenUsed + permGenUsed + survivorSpaceUsed;
        System.out.println("***************************************** Measured: $total **************************");

        return total / 1000;
    }

    private void forceGC() throws Exception {
        connection.invoke(new ObjectName("java.lang:type=Memory"), "gc", null, null);
    }
}