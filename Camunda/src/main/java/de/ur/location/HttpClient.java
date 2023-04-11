package de.ur.location;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpClient {
    static java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
    static Gson gson = new Gson();

    static final String NODE_RED_BASE_URL = "http://localhost:1880/";

    public static String get(String path, String urlParam) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(NODE_RED_BASE_URL + path + (urlParam != null ? "/" + urlParam : "")))
                    .GET()
                    .version(java.net.http.HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 400) {
                throw new RuntimeException("Couldn't extract body from response (" + resp.body() + ")");
            }
            return resp.body();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Couldn't fetch data from given URL", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI Syntax invalid", e);
        }
    }

    public static String get(String path) {
        return get(path, null);
    }

    public static String post(String path, String urlParam, Map<String, String> body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(NODE_RED_BASE_URL + path + (urlParam != null ? "/" + urlParam : "")))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                    .version(java.net.http.HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() >= 400) {
                throw new RuntimeException("Couldn't extract body from response (" + resp.body() + ")");
            }
            return resp.body();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Couldn't post body to given URL", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI Syntax invalid", e);
        }
    }

    public static String post(String path, String urlParam) {
        return post(path, urlParam, null);
    }
}
