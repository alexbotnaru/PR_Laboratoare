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
        String responseString = "";

        System.out.println("Where to download the images from?");
        System.out.println("me.utm.md        [1]");
        System.out.println("utm.md           [2]");

        Integer answer = Integer.valueOf(scanner.nextLine());

        if (answer == 1) {
            String host = "me.utm.md";
            int port = 80;

            SocketFactory SocketFactory = (SocketFactory) javax.net.SocketFactory.getDefault();
            Socket socket = (Socket) SocketFactory.createSocket(host, port);
//            sslSocket.startHandshake();

            GetRequest getRequest = new GetRequest(socket, host);
            String response = getRequest.sendGetRequest();
            Set<String> imageLinks = filterResposne(response);
            System.out.println(imageLinks);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            final Semaphore semaphore = new Semaphore(2);

            ArrayList<Runnable> taskList = new ArrayList<>();

            for (String link : imageLinks) {

                Runnable task = new ImageDownloadTask(link, semaphore, "", port);
                taskList.add(task);
            }

            for (Runnable task : taskList) {
                executor.execute(task);
            }
            executor.shutdown();

            try {
                if (executor.awaitTermination(3L, TimeUnit.MINUTES)) {
                    System.out.println("\n********** Download completed successfully! **********");
                } else {
                    System.out.println("\n********** Time limit exceeded! ********** ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } else if (answer == 2) {
            String host = "utm.md";
            int port = 443;

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try {
                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
                sslSocket.startHandshake();
//                InputStream in = sslSocket.getInputStream();
//                OutputStream out = sslSocket.getOutputStream();

                GetRequest getRequest = new GetRequest(sslSocket, host);
                String response = getRequest.sendGetRequest();

                Set<String> imageLinks = filterResposne(response);
                System.out.println(imageLinks);

                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
                final Semaphore semaphore = new Semaphore(2);

                ArrayList<Runnable> taskList = new ArrayList<>();

                for (String link : imageLinks) {

                    Runnable task = new ImageDownloadTask(link, semaphore, "Utm", port);
                    taskList.add(task);
                }

                for (Runnable task : taskList) {
                    executor.execute(task);
                }
                executor.shutdown();

                try {
                    if (executor.awaitTermination(15L, TimeUnit.MINUTES)) {
                        System.out.println("\n********** Download completed successfully! **********");
                    } else {
                        System.out.println("\n********** Time limit exceeded! ********** ");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

//        System.out.println("\n********** Download starting **********");


//        try (var socket = new Socket(host, port)) {
//
//
//            try (var wtr = new PrintWriter(socket.getOutputStream())) {
//
//                // create GET request
//                wtr.print("GET / HTTP/1.1\r\n");
//                wtr.print("Host: www." + host + "\r\n");
//                wtr.print("\r\n");
//                wtr.flush();
//                socket.shutdownOutput();
//
//                String outStr;
//
//                try (var bufRead = new BufferedReader(new InputStreamReader(
//                        socket.getInputStream()))) {
//
//                    while ((outStr = bufRead.readLine()) != null) {
//
//                        responseString += outStr + "\n";
//
//                    }
//
//                    socket.shutdownInput();
//                    bufRead.close();
//                    wtr.close();
//                }
//            }
//        }

//        Pattern pattern = Pattern.compile("<img\\s[^>]*?src\\s*=\\s*['\\\"]([^'\\\"]*?)['\\\"][^>]*?>");
//
//        List<String> allImages = new ArrayList<>();
//
//        Matcher matcher = pattern.matcher(responseString);
//        while (matcher.find()) {
//            allImages.add(matcher.group(1));
//        }
//
//        Set<String> allImagesLinks = new HashSet<>();
//        allImages.forEach((image) -> {
//            if (image.startsWith("http://")) {
//                allImagesLinks.add(image);
//            } else if (!image.startsWith("//counter")) {
//                allImagesLinks.add("http://me.utm.md/" + image);
//
//            }
//        });
//        allImages.clear();


//        allImagesLinks.forEach((link) -> {
//
//
//            ImageDownloader imageDownloader = new ImageDownloader(link);
//            try {
//                imageDownloader.download(host, port);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });


//        allImagesLinks.forEach((link) -> {
//
//        });

//        myStr = myStr.substring(myStr.lastIndexOf("/") + 1, myStr.length());

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

                System.out.println("Ends with jpg, png: " + image);

//                if (image.contains("\n1f40\n")) {
//                    image.sub
//                }

                if (image.startsWith("http:") || image.startsWith("https:")) {
                    allImagesLinks.add(image);
                } else {
                    allImagesLinks.add("http://me.utm.md/" + image);
                }

            }
        });
        allImages.clear();
        //            if (image.startsWith("http://")) {
//                allImagesLinks.add(image);
//            } else if (!image.startsWith("//counter")) {
//                allImagesLinks.add("http://me.utm.md/" + image);
//
//            }

        return allImagesLinks;
    }
}
// <img([\w\W]+?)>
// <img\s+[^>]*src="([^"]*)"[^>]*>
//30 img