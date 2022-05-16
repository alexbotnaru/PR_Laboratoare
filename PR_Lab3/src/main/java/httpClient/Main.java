package httpClient;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static final String API_URL = "https://jsonplaceholder.typicode.com/posts";

    public static void main(String[] args) throws IOException, InterruptedException {

        RequestSender requestSender = new RequestSender();
        System.out.println("\n************ Menu ************");
        System.out.println("Send a GET request                [1]");
        System.out.println("Send a POST request               [2]");
        System.out.println("Send a HEAD request               [3]");
        System.out.println("Send a OPTIONS request            [4]");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                requestSender.sendGetRequest(API_URL);
                main(null);
                break;
            case 2:
                requestSender.sendPostRequest(API_URL);
                main(null);
                break;
            case 3:
                requestSender.sendHeadRequest(API_URL);
                main(null);
                break;
            case 4:
                requestSender.sendOptionsRequest(API_URL);
                main(null);
                break;
            default:
                System.exit(1);
        }

    }
}
