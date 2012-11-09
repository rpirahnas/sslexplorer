
				/*
 *  SSL-Explorer
 *
 *  Copyright (C) 2003-2006 3SP LTD. All Rights Reserved
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
			
package com.sslexplorer.notification.smtp;

import java.io.IOException;
import java.io.Writer;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

import com.sslexplorer.core.UserDatabaseManager;
import com.sslexplorer.notification.AbstractMessageSender;
import com.sslexplorer.notification.Message;
import com.sslexplorer.notification.MessageSink;
import com.sslexplorer.notification.Notifier;
import com.sslexplorer.notification.Recipient;
import com.sslexplorer.properties.Property;
import com.sslexplorer.properties.impl.systemconfig.SystemConfigKey;
import com.sslexplorer.security.User;
import com.sslexplorer.security.UserDatabase;

/**
 */
public class SMTPMessageSink implements MessageSink {
    private static final Log LOG = LogFactory.getLog(SMTPMessageSink.class);
    private final SMTPClient client;
    private Notifier notifier;

    /**
     */
    public SMTPMessageSink() {
        client = new SMTPClient();
    }

    public void start(Notifier notifier) throws Exception {
        this.notifier = notifier;
    }

    public void stop() throws Exception {
    }

    public boolean send(final Message message) throws Exception {
        try {
            connect();
            notifier.send(message.getRecipients(), new AbstractMessageSender(){
                public int performSendMessage(Recipient recipient) throws Exception {
                    send(message, recipient);
                    return 0;
                }
            });
            return true;
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
    }

    private void connect() throws SocketException, IOException, Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting timeout");
            LOG.debug("Connecting to SMTP server");
        }
        String hostname = Property.getProperty(new SystemConfigKey("smtp.hostname"));
        int port = Property.getPropertyInt(new SystemConfigKey("smtp.port"));
        client.connect(hostname, port);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting reply");
        }
        
        int reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            throw new Exception("SMTP server refused connection.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Logging in");
        }

        String helo = Property.getProperty(new SystemConfigKey("smtp.login"));
        if (helo.equals("")) {
            client.login();
        } else {
            client.login(helo);
        }
    }
    
    private void send(Message message, Recipient recipient) throws Exception {
        UserDatabase userDatabase = UserDatabaseManager.getInstance().getUserDatabase(recipient.getRealmName());
        User user = userDatabase.getAccount(recipient.getRecipientAlias());

        String sender = Property.getProperty(new SystemConfigKey("smtp.senderAddress"));
        client.setSender(sender);
        client.addRecipient(user.getEmail());
        Writer writer = client.sendMessageData();
        if (writer == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to send message data to " + user.getEmail());
            }
            return;
        }

        SimpleSMTPHeader header = new SimpleSMTPHeader(sender, user.getEmail(), message.getSubject());
        writer.write(header.toString());
        writer.write(message.getContent());
        writer.close();
        if (!client.completePendingCommand()) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to send message data to " + user.getEmail());
            }
        }
    }

    public String getName() {
        return "SMTP";
    }

    public String getShortNameKey() {
        return "notification.smtp.shortName";
    }

    public String getDescriptionKey() {
        return "notification.smtp.description";
    }

    public String getBundle() {
        return "setup";
    }
}