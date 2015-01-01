/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ExampleToTest {

    public static void main(String[] args) {
// @TODO hoort niet op deze plaatst thuis

        ServerProfile profile = new ServerProfile("sendProfile");
        profile.setDefaultGMailSmtpSettings();

        SmtpServer server = Main.connectToSMTPServer(profile, "testersutrecht@gmail.com", "geenidee2");
        String from = "testersutrecht@gmail.com";
        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht2@gmail.com");
        String subject = "ExampleToTest: " + new Date();
        String body = "Dit is een test van de methods die door de programmeurs gebruikt worden.";

        Mail mail = new Mail(from, toList, subject, body);
        mail.createMessage(server.getEmailSession());
        server.sendMail(mail);
    }

}
