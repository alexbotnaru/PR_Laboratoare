package com.company;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

public class GetRequest {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private String host;

    GetRequest(Socket socket, String host) throws IOException {
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream());
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.host = host;
    }


    public String sendGetRequest() throws IOException {
    String response = "";
    String outStr;
        printWriter.print("GET / HTTP/1.1\r\n");
        printWriter.print("Host: " + host + "\r\n");
        printWriter.print("Connection: keep-alive\r\n");
        printWriter.print("Accept-Language: ro,en\r\n");
        printWriter.print("DNT: 1\r\n");
        printWriter.print("Save-Data: <sd-token>\r\n");
        printWriter.print("\r\n");
        printWriter.flush();

        while((outStr = bufferedReader.readLine()) != null){
            response += outStr + "\n";
        }
        socket.shutdownInput();
        bufferedReader.close();
        printWriter.close();

        return response;
    }

}
