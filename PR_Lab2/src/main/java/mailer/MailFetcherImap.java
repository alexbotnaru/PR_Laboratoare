package mailer;

import com.sun.mail.imap.IMAPFolder;

import javax.activation.DataHandler;
import javax.mail.*;
import java.util.Arrays;
import java.util.Properties;

public class MailFetcherImap {

    public static void readInboundEmails() {
        Session session = getImapSession();
        try {
            Store store = session.getStore("imap");
            store.connect("imap.gmail.com", 993, Constants.EMAIL, Constants.PASSWORD);

            IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages();

            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                System.out.println("\nEmail Number " + (i + 1));
                System.out.println("FROM: " + fromAddress[0].toString());
                System.out.println("SUBJECT: " + msg.getSubject());
                System.out.println("TO: " + Arrays.toString(msg.getRecipients(Message.RecipientType.TO)));
                System.out.println("TYPE: " + msg.getContentType());
                System.out.println("CONTENT: ");

                Multipart multipart = (Multipart) msg.getContent();

                for (int j = 0; j < multipart.getCount(); j++) {
                    BodyPart bodyPart = multipart.getBodyPart(j);
                    String disposition = bodyPart.getDisposition();

                    if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                        System.out.println("The email has an attachment: ");

                        DataHandler handler = bodyPart.getDataHandler();
                        System.out.println("file name: " + handler.getName());
                    } else {
                        System.out.println(bodyPart.getContent());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Session getImapSession() {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.debug", "false");
        props.setProperty("mail.imap.host", "imap.gmail.com");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");
        return Session.getDefaultInstance(props, null);
    }
}