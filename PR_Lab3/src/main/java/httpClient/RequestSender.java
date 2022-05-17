package httpClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class RequestSender {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void sendGetRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());
    }

    public void sendPostRequest(String url) throws IOException, InterruptedException {
        var values = new HashMap<String, String>();
        values.put("userId", "18");
        values.put ("title", "test title");
        values.put("body", "test body");

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());
    }

    public void sendHeadRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(url))
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        System.out.println("Headers: ");
        HttpHeaders headers = response.headers();
        headers.map().forEach((key, value) -> System.out.printf("%s = %s\n", key, value));
    }

    public void sendOptionsRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpHeaders optionHeaders = response.headers();
        optionHeaders.map().forEach((key, value) -> System.out.printf("%s = %s\n", key, value));
    }

}
