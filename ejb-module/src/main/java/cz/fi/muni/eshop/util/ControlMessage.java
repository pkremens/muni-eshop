/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public enum ControlMessage {  // POUZIVAM JI V TOM MOJEM JMS TESTU TAK PROTO TU JESTE JE, PAK JI HNED SMAZNU

    TOO_MANY_CLOSED("There are too many closed orders in database, make a clean up!"),
    FILL_THE_STORE("There are too many orders, which can not be closed, fill the store!"),
    JMS_CONTROL_MESSAGE("This is my test message for JMS, see you on the other side!");
    private final String message;

    private ControlMessage(String message) {
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
