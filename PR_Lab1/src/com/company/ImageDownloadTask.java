package com.company;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class ImageDownloadTask implements Runnable {
    private String urlString;
    private File path;
    private String writeTo;
    private Semaphore semaphore;
    private Integer port;

    public ImageDownloadTask(String urlString, Semaphore semaphore, String suffix, Integer port) {
        this.path = new File("C:\\Users\\Olexei\\IdeaProjects\\PR_Lab1\\images" + suffix);
        this.urlString = urlString;
        this.semaphore = semaphore;
        this.writeTo = path.toPath().toString() + "\\" + urlString.substring(urlString.lastIndexOf("/"), urlString.length());
        this.port = port;
    }

    @Override
    public void run() {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(writeTo);

            byte[] buffer = new byte[2048];
            int length = 0;
            System.out.println("\nDownloading from: " + urlString);
            System.out.println("This is thread: " + Thread.currentThread().getName());

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        semaphore.release();
//        try {
//
//            Socket socket = getNewSocket();
//
//            PrintWriter pw = new PrintWriter(socket.getOutputStream());
//            pw.println("GET " + url.getPath() + " HTTP/1.1");
//            pw.println("Host: " + url.getHost());
//            pw.println();
//            pw.flush();
//
////            System.out.println("url host: " + url.getHost());
//            System.out.println("\nUrl path: " + url.getPath());
//            System.out.println("This is thread: " + Thread.currentThread().getName());
//
//            try {
//                semaphore.acquire();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            final FileOutputStream fileOutputStream = new FileOutputStream(writeTo);
//            final InputStream inputStream = socket.getInputStream();
//
////            System.out.println("Input Stream: " + Arrays.toString(inputStream.readAllBytes()));
//
//
//
//// Header end flag.
//            boolean headerEnded = false;
//
//            //try with substring
//            byte[] bytes = new byte[2048];
//            int length;
//
//            while ((length = inputStream.read(bytes)) != -1) {
//                // If the end of the header had already been reached, write the bytes to the file as normal.
//                if (headerEnded)
//                    fileOutputStream.write(bytes, 0, length);
//
//                    // This locates the end of the header by comparing the current byte as well as the next 3 bytes
//                    // with the HTTP header end "\r\n\r\n" (which in integer representation would be 13 10 13 10).
//                    // If the end of the header is reached, the flag is set to true and the remaining data in the
//                    // currently buffered byte array is written into the file.
//                else {
//                    for (int i = 0; i < 2048; i++) {
//                        if (bytes[i] == 13 && bytes[i + 1] == 10 && bytes[i + 2] == 13 && bytes[i + 3] == 10) {
//                            headerEnded = true;
//                            fileOutputStream.write(bytes, i + 4, 2048 - i - 4);
//                            break;
//                        }
//                    }
//                }
//            }
//            inputStream.close();
//            fileOutputStream.close();
////            socket.close();
//
//
//        } catch (IOException e) {
//            System.out.println(e);
//        }

    }

    public Socket getNewSocket() throws IOException {

        if (this.port == 80) {
            SocketFactory socketFactory = (SocketFactory) javax.net.SocketFactory.getDefault();
            return (Socket) socketFactory.createSocket("me.utm.md", port);
        } else if (this.port == 443) {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("utm.md", port);
            sslSocket.startHandshake();
            return sslSocket;
        }

        return null;
    }

}
