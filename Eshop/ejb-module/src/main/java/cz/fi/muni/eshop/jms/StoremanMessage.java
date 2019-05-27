/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.jms;

/**
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public enum StoremanMessage {

    CLOSE_ORDER("New order has been registered, close it!"),
    FILL_THE_STORE("There is some product missing on store, refill!"),
    JMS_CONTROL_MESSAGE("This is my test message for JMS, see you on the other side!");
    private final String message;

    StoremanMessage(String message) {
        this.message = message;
    }

    public String getMessage() { // Someone could actually want to get message and don't know, that he could use toString()...
        return message;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
