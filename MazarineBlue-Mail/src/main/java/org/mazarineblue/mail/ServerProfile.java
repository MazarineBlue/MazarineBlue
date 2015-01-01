/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import java.util.Properties;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ServerProfile {

    // @TODO statische variable komen voor niet-statische variabelen
    static public final int defaultPort = -1;
    static public final int imapSslPort = 993;
    static public final int imapNoSslPort = 143;
    static public final int smtpSslPort = 465;
    static public final int smtpNoSslPort = 587;
    static public final String defaultAuth = "true";

    // @TODO is zou de default waarde 0 laten.
    private String host;
    private int port = defaultPort;
    private boolean ssl = false, startTLS = false;

    private String protocol = "imaps";
    private final Properties properties = new Properties(); // @TODO geen attribute van deze class!
    private final String identifier;

    public ServerProfile(String identifier) {
        this.identifier = identifier;
    }

    // @TODO liever als statische methode in een utility class
    public void setDefaultGMailImapSettings() {
        host = "imaps.gmail.com";
        port = imapSslPort;
        ssl = true;
        startTLS = true;
    }

    // @TODO liever als statische methode in een utility class
    public void setDefaultGMailSmtpSettings() {
        host = "smtp.gmail.com";
        port = smtpNoSslPort;
        ssl = false;
        startTLS = true;
    }

    public Properties getProperties() {
        if (host == null)
            throw new InstructionException("The instruction Server need to be called for identifier: " + identifier); // @TODO geen InstructionException gooien, verzien maar gooi HostNotDeclaredException oid.

        // @TODO deze methodes zijn hier op de verkeerde plaats
        protocol = determineProtocol();
        port = determinePort(); // @TODO return value zodat gedag exmpliet wordt

        setProperties(); // @TODO deze methode mag hier integraal worden overgenomen.
        return properties;
    }

    private String determineProtocol() {
        switch(protocol) { // @TODO switch duidelijker
            case "imap":
            case "imaps":
                return ssl ? "imaps" : "imap";
            case "smtp":
            case "smtps":
                return ssl ? "smtps" : "smtp";
        }
    }

    private int determinePort() {
        if (port != defaultPort)
            return port;
        switch(protocol) {
            case "imap":
            case "imaps":
                return ssl ? imapSslPort : imapNoSslPort;
            case "smtp":
            case "smtps":
                return ssl ? smtpSslPort : smtpNoSslPort;
        }
    }

    private void setProperties() {
        String sslBoolean = Boolean.toString(ssl);
        String startTlsBoolean = Boolean.toString(startTLS);

        properties.put(getStartTLSKey(protocol), startTlsBoolean);
        properties.put(getProtocolKey(), protocol);
        properties.put(getPortKey(protocol), Integer.toString(port));
        properties.put(getHostKey(protocol), host);
        properties.put(getSSLKey(protocol), sslBoolean);
        properties.put(getAuthKey(protocol), defaultAuth);
    }

    // @TODO deze zou niet nodig moeten zijn.
    public String getStartTLSKey(String protocol) {
        return "mail." + protocol + ".starttls.enable";
    }

    // @TODO deze zou niet nodig moeten zijn.
    public String getProtocolKey() {
        return "mail.store.protocol";
    }

    // @TODO deze zou niet nodig moeten zijn.
    public String getPortKey(String protocol) {
        return "mail." + protocol + ".port";
    }

    // @TODO deze zou niet nodig moeten zijn.
    public String getHostKey(String protocol) {
        return "mail." + protocol + ".host";
    }

    // @TODO deze zou niet nodig moeten zijn.
    public String getSSLKey(String protocol) {
        return "mail." + protocol + ".ssl";
    }

    // @TODO deze zou niet nodig moeten zijn.
    public String getAuthKey(String protocol) {
        return "mail." + protocol + ".auth";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean getStartTLS() {
        return startTLS;
    }

    public void setStartTLS(boolean startTLS) {
        this.startTLS = startTLS;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIdentifier() {
        return identifier;
    }
}
