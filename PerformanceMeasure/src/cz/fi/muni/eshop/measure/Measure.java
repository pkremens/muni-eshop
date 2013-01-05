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
 * @author Jan Martiska <jmartiska@redhat.com>, Petr Kremensky <207855@mail.muni.cz>
 */

public class Measure {

    private static final String host = "localhost"; //Get a connection to the JBoss AS MBean server on localhost
    private static final int port = 9999; // management-native port
    final MBeanServerConnection connection;
    JMXConnector jmxConnector;

    public Measure() throws Exception {
        String urlString =
                System.getProperty("jmx.service.url", "service:jmx:remoting-jmx://" + host + ":" + port);
        JMXServiceURL serviceURL = new JMXServiceURL(urlString);
        jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
        connection = jmxConnector.getMBeanServerConnection();
    }

    public void destroy() throws Exception {
        jmxConnector.close();
    }

    public double getFootprint() throws Exception {
        for (int i = 0; i < 12; i++) {
            System.out.println("Forcing garbage collection");
            forceGC();
            System.out.println("Forced garbage collection");
            Thread.sleep(5000);
        }
        forceGC();
        Thread.sleep(1000);
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

        System.out.println("Current Time: " + Calendar.getInstance().getTime());
        System.out.println("Eden space: " + edenSpaceUsed);
        System.out.println("Old gen: " + oldGenUsed);
        System.out.println("Perm gen: " + permGenUsed);
        System.out.println("Survivor space: " + survivorSpaceUsed);
        System.out.println("Heap memory: " + heapMemoryUsed);
        System.out.println("NonHeap memory: " + nonHeapMemoryUsed);
        Long total = edenSpaceUsed + oldGenUsed + permGenUsed + survivorSpaceUsed;
        System.out.println("***************************************** Measured: $total **************************");

        return total / 1000;
    }

    private void forceGC() throws Exception {
        connection.invoke(new ObjectName("java.lang:type=Memory"), "gc", null, null);
    }
}