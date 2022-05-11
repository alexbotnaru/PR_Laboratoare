package mailer;

import javax.mail.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static mailer.Constants.EMAIL;
import static mailer.Constants.PASSWORD;

public class MailFetcherPop3 {
    public static void receiveEmail() {

        String pop3Host = "pop.gmail.com";
        String storeType = "pop3s";
        final String user = EMAIL;
        final String password = PASSWORD;

        Properties props = new Properties();
        props.setProperty("mail.pop3.host", pop3Host);
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.starttls.enable", "true");
        props.setProperty("mail.store.protocol", "pop3");

        Session session = Session.getInstance(props);
        try {
            Store mailStore = session.getStore(storeType);
            mailStore.connect(pop3Host, user, password);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            Message[] emailMessages = folder.getMessages();
            System.out.println("Number of messages: "
                    + emailMessages.length);

            for (int i = 0; i < emailMessages.length; i++) {
                Message message = emailMessages[i];
                Address[] toAddress =
                        message.getRecipients(Message.RecipientType.TO);
                System.out.println();
                System.out.println("Email Number " + (i + 1));
                System.out.println("SUBJECT: " + message.getSubject());
                System.out.println("FROM: " + message.getFrom()[0]);

                System.out.print("TO: ");
                for (int j = 0; j < toAddress.length; j++) {
                    System.out.println(toAddress[j].toString());
                }

                System.out.print("CONTENT : ");
                Multipart multipart = (Multipart) message.getContent();
                for (int k = 0; k < multipart.getCount(); k++) {
                    BodyPart bodyPart = multipart.getBodyPart(k);
                    InputStream stream =
                            bodyPart.getInputStream();
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(stream));

                    while (bufferedReader.ready()) {
                        System.out.println(bufferedReader.readLine());
                    }
                    System.out.println();
                }

            }

            folder.close(false);
            mailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//PRlab2password
//prla2btest@gmail.com
