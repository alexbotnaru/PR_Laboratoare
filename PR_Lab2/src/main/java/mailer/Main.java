package mailer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        run();
    }


    private static int getUserAnswer() {
        System.out.println("\n********** Menu **********");
        System.out.println(
                "Read messages using POP3              [1] \n" +
                "Read messages using IMAP              [2] \n" +
                "Send message using SMTP               [3] \n" +
                "Exit                                  [4] \n");

        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static void run(){
        int option = getUserAnswer();

        if (option == 1) {
            try {

                MailFetcherPop3.receiveEmail();
                run();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (option == 2) {
            MailFetcherImap.readInboundEmails();
            run();
        } else if (option == 3) {
            MailSender.sendEmail("prla2btest@gmail.com");
            run();
        }
        else System.exit(0);
    }
}

