package com.company;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class ImageDownloadTask implements Runnable {
    private String link;
    private String writeTo;
    private Semaphore semaphore;

    public ImageDownloadTask(String link, Semaphore semaphore, String suffix) {
        this.link = link;
        this.semaphore = semaphore;
        this.writeTo = "./images" + suffix + "\\" + link.substring(link.lastIndexOf("/"), link.length());
    }

    @Override
    public void run() {

        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {

            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(writeTo);

            semaphore.acquire();

            byte[] buffer = new byte[2048];
            int length = 0;
            System.out.println("\nDownloading from: " + link);
            System.out.println("This is thread: " + Thread.currentThread().getName());

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        semaphore.release();

    }
}



