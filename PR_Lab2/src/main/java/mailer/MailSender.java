package mailer;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Scanner;

public class MailSender {

    public static void sendEmail(String recipient){
        Properties properties = new Properties();

        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");

        String emailAddress = Constants.EMAIL;
        String password = Constants.PASSWORD;

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        try {
            prepareMessage(session, emailAddress, recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void prepareMessage(Session session, String emailAddress, String recipient) throws MessagingException {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            System.out.println("Introduceti Subiectul");
            Scanner scanner = new Scanner(System.in);
            String subject = scanner.nextLine();
            message.setSubject(subject);

            System.out.println("Introduceti Textul");
            String messageText = scanner.nextLine();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("****** Mesaj trimis! ******");

    }

}
