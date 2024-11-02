package com.tkms3.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;

public class ApiManager {

    HttpClient client;

    public ApiManager() {
        client = HttpClient.newHttpClient();
    }

    public JsonObject getWeather(String place) throws IOException, InterruptedException {
        String url = "https://geocoding-api.open-meteo.com/v1/search?name="+place+"&count=1&language=en&format=json";

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send the request synchronously
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject search = JsonParser.parseString(response.body()).getAsJsonObject();
        System.out.println("Latitude: " + search);
        System.out.println("Longitude: " + search.get("longitude"));

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude="+search.getAsJsonArray("results").get(0).getAsJsonObject().get("latitude").getAsLong()+"&longitude="+search.getAsJsonArray("results").get(0).getAsJsonObject().get("longitude").getAsLong()+
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=auto"))
                .GET()
                .build();

        // Parse the response body into a JsonObject
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        return JsonParser.parseString(response2.body()).getAsJsonObject();
    }

    public String getPlaceDetails(String place) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyDnSIPJijuoOpmYKpnSMS3X5Qojmxdz6b8";
        String prompt = "{\"contents\":{\"parts\":[{\"text\":\"Create a fully-styled HTML page describing " + place + " and its tourist attractions, famous food and things needed to be takien while travelling there using only inline styles. Ensure every section has vibrant colors, clean layout, and appropriate font sizes for headings, paragraphs, and highlights. Make the background colour to be #40E0D0 and the text is visible by setting appropriate colours. Include detailed descriptions of each place, and use subtle gradients, borders, and margins for visual clarity. Ensure modern, professional styling with no missing elements. Ensure it is readable with contrast and padding for small screens.Dont use images\"}]}}";

        JsonObject payload = JsonParser.parseString(prompt).getAsJsonObject();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")  // Set Content-Type header
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString())) // POST with JSON payload
                .build();

        // Send the request and handle the response
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject resjson = JsonParser.parseString(response.body()).getAsJsonObject();
            return resjson.getAsJsonArray("candidates").get(0).getAsJsonObject().getAsJsonObject("content").getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

