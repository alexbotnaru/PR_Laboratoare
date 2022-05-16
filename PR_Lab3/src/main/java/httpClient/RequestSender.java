package httpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestSender {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void sendGetRequest(String url) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());
    }

    public void sendPostRequest(String url) throws IOException, InterruptedException {
        String requestBody = "{\n" +
                "  \"userId\": 18,\n" +
                "  \"title\": \"test title\",\n" +
                "  \"body\": \"test body\",\n" +
                "}";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());
        System.out.println(response);
    }

    public void sendHeadRequest(String url) throws IOException, InterruptedException {
        HttpRequest headRequest = HttpRequest.newBuilder()
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(url))
                .build();

        HttpResponse<Void> headResponse = httpClient.send(headRequest, HttpResponse.BodyHandlers.discarding());

        System.out.println("Headers: ");
        HttpHeaders headers = headResponse.headers();
        headers.map().forEach((key, value) -> System.out.printf("%s = %s%n", key, value));
    }

    public void sendOptionsRequest(String url) throws IOException, InterruptedException {
        HttpRequest optionsRequest = HttpRequest.newBuilder()
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(url))
                .build();

        HttpResponse<Void> optionsResponse = httpClient.send(optionsRequest, HttpResponse.BodyHandlers.discarding());

        HttpHeaders optionHeaders = optionsResponse.headers();
        optionHeaders.map().forEach((key, value) -> System.out.printf("%s = %s%n", key, value));
    }

}
