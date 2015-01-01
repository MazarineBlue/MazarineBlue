/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class MessageNotFoundException extends Exception {

    public MessageNotFoundException(String string) {
        super(string);
    }

    public MessageNotFoundException(String string, Throwable thrwbl) {
        super(string, thrwbl); // @TODO Geen afkortingen gebruiken; de naam cause is gebruikelijk
    }

    public MessageNotFoundException(Throwable thrwbl) {
        super(thrwbl);
    }
}
