/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class MailFilter {

    private boolean fromSet, subjectSet, bodySet, toSet = false;
// @TODO Collection over List, List over ArrayList, init = null
    private final ArrayList<Pattern> toPatterns = new ArrayList<>();
    private Pattern fromPattern, subjectPattern, bodyPattern;

    public void addTo(String recipient) {
        // @TODO andere exceptie opgooien
        if (recipient.isEmpty())
            throw new InstructionException("The given recipient is empty");

        toSet = true; // @TODO overbodig
        toPatterns.add(Pattern.compile(recipient));
    }

    public void setFrom(String from) {
        // @TODO andere exceptie opgooien
        if (from.isEmpty())
            throw new InstructionException("The given from name is empty");

        fromSet = true; // @TODO overbodig
        fromPattern = Pattern.compile(from);
    }

    public void setSubject(String subject) {
        subjectSet = true; // @TODO overbodig
        subjectPattern = Pattern.compile(subject);
    }

    public void setBody(String body) {
        bodySet = true; // @TODO overbodig
        bodyPattern = Pattern.compile(body);
    }

    public SearchTerm getSearchCondition() {
	// @TODO anonieme klasse is te groot -> oplossingen?
        SearchTerm searchTerm = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                boolean flag = checkFrom(message);
                if (flag)
                    flag = checkSubject(message);
                if (flag)
                    flag = checkBody(message);
                if (flag)
                    flag = checkTo(message);
                return flag;
            }

            private boolean checkFrom(Message message) {
                if (fromSet == false)
                    return true;
                try {
                    Address[] froms = message.getFrom();
                    String fromAddress = (froms == null) ? null : ((InternetAddress) froms[0]).getAddress();
                    Matcher matcher = fromPattern.matcher(fromAddress);
                    return matcher.find();
                } catch (MessagingException ex) {
                    throw new InstructionException("An error occurred while trying to retrieve the FROM of the message", ex);
                }
            }

            private boolean checkSubject(Message message) {
                if (subjectSet == false)
                    return true;
                try {
                    String messageSubject = message.getSubject();
                    Matcher matcher = subjectPattern.matcher(messageSubject);
                    return matcher.find();
                } catch (MessagingException ex) {
                    throw new InstructionException("An error occurred while trying to retrieve the subject of the message", ex);
                }
            }

            private boolean checkBody(Message message) {
                if (bodySet == false)
                    return true;
                try {
                    String messageBody = message.getContent().toString();
                    Matcher matcher = bodyPattern.matcher(messageBody);
                    return matcher.find();
                } catch (MessagingException | IOException ex) {
                    throw new InstructionException("An error occurred while trying to retrieve the body of the message", ex);
                }
            }

            private boolean checkTo(Message message) {
                if (toSet == false)
                    return true;
                try {
                    Address[] toRecipients = message.getRecipients(Message.RecipientType.TO);
                    if (toPatterns.size() > toRecipients.length)
                        return false;

                    for (int i = 0; i < toPatterns.size(); i++) {
                        String toEmailAddress = toRecipients[i].toString();
                        Matcher matcher = toPatterns.get(i).matcher(toEmailAddress);
                        boolean found = matcher.find();
                        if (found == false)
                            return false;
                    }
                    return true;
                } catch (MessagingException ex) {
                    throw new InstructionException("An error occurred while trying to retrieve the TO recipients of the message", ex);
                }
            }
        };
        return searchTerm;
    }
}
