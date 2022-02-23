package com.company;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Where to download the images from?");
        System.out.println("me.utm.md        [1]");
        System.out.println("utm.md           [2]");

        Integer answer = Integer.valueOf(scanner.nextLine());

        if (answer == 1) {
            String host = "me.utm.md";
            int port = 80;

            SocketFactory SocketFactory = (SocketFactory) javax.net.SocketFactory.getDefault();
            Socket socket = (Socket) SocketFactory.createSocket(host, port);

            GetRequest getRequest = new GetRequest(socket, host);
            String response = getRequest.sendGetRequest();
            Set<String> imageLinks = filterResposne(response);
            System.out.println(imageLinks);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            final Semaphore semaphore = new Semaphore(2);

            ArrayList<Runnable> taskList = new ArrayList<>();

            for (String link : imageLinks) {

                Runnable task = new ImageDownloadTask(link, semaphore, "_me_utm", port);
                taskList.add(task);
            }

            for (Runnable task : taskList) {
                executor.execute(task);
            }
            executor.shutdown();

            printFinalMessage(executor);


        } else if (answer == 2) {
            String host = "utm.md";
            int port = 443;

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try {
                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
                sslSocket.startHandshake();

                GetRequest getRequest = new GetRequest(sslSocket, host);
                String response = getRequest.sendGetRequest();

                Set<String> imageLinks = filterResposne(response);
                System.out.println(imageLinks);

                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
                final Semaphore semaphore = new Semaphore(2);

                ArrayList<Runnable> taskList = new ArrayList<>();

                for (String link : imageLinks) {

                    Runnable task = new ImageDownloadTask(link, semaphore, "_utm", port);
                    taskList.add(task);
                }

                for (Runnable task : taskList) {
                    executor.execute(task);
                }
                executor.shutdown();

                printFinalMessage(executor);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static Set<String> filterResposne(String response) {
        Pattern pattern = Pattern.compile("<img\\s[^>]*?src\\s*=\\s*['\\\"]([^'\\\"]*?)['\\\"][^>]*?>");

        Set<String> allImages = new HashSet<>();

        Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            allImages.add(matcher.group(1));
        }

        System.out.println("all Images: "+allImages);

        Set<String> allImagesLinks = new HashSet<>();
        allImages.forEach((image) -> {

            if (image.endsWith(".jpg") || image.endsWith(".png") || image.endsWith(".gif")) {

                if (image.startsWith("http:") || image.startsWith("https:")) {
                    allImagesLinks.add(image);
                } else {
                    allImagesLinks.add("http://me.utm.md/" + image);
                }

            }
        });
        allImages.clear();

        return allImagesLinks;
    }
    public static void printFinalMessage(ThreadPoolExecutor executor){
        try {
            if (executor.awaitTermination(5, TimeUnit.MINUTES)) {
                System.out.println("\n********** Download completed successfully! **********");
            } else {
                System.out.println("\n********** Time limit exceeded! ********** ");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// <img\s+[^>]*src="([^"]*)"[^>]*>
