package com.dineix.web.payment;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.UUID;
import org.json.JSONObject;

public class MTNMomoService {
    private final HttpClient httpClient;
    private String accessToken;
    
    public MTNMomoService() {
        this.httpClient = HttpClient.newHttpClient();
    }
    
    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    private String getAccessToken() throws IOException, InterruptedException {
        if (accessToken != null) {
            return accessToken;
        }
        
        String credentials = Base64.getEncoder().encodeToString(
            (MTNMomoConfig.getApiUser() + ":" + MTNMomoConfig.getApiKey()).getBytes()
        );
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(MTNMomoConfig.TOKEN_URL))
            .header("Authorization", "Basic " + credentials)
            .header("Ocp-Apim-Subscription-Key", MTNMomoConfig.getSubscriptionKey())
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            accessToken = jsonResponse.getString("access_token");
            return accessToken;
        }
        
        throw new IOException("Failed to get access token: " + response.body());
    }
    
    public String initiatePayment(String phoneNumber, double amount, String currency) throws IOException, InterruptedException {
        String referenceId = generateUUID();
        String token = getAccessToken();
        
        JSONObject requestBody = new JSONObject()
            .put("amount", String.format("%.2f", amount))
            .put("currency", currency)
            .put("externalId", referenceId)
            .put("payer", new JSONObject()
                .put("partyIdType", "MSISDN")
                .put("partyId", phoneNumber))
            .put("payerMessage", "Payment for Dineix meal package")
            .put("payeeNote", "Dineix package purchase");
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(MTNMomoConfig.PAYMENT_REQUEST_URL))
            .header("Authorization", "Bearer " + token)
            .header("X-Reference-Id", referenceId)
            .header("X-Target-Environment", MTNMomoConfig.getEnvironment())
            .header("Ocp-Apim-Subscription-Key", MTNMomoConfig.getSubscriptionKey())
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 202) {
            return referenceId;
        }
        
        throw new IOException("Payment initiation failed: " + response.body());
    }
    
    public String getPaymentStatus(String referenceId) throws IOException, InterruptedException {
        String token = getAccessToken();
        
        String url = MTNMomoConfig.PAYMENT_STATUS_URL.replace("{referenceId}", referenceId);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + token)
            .header("X-Target-Environment", MTNMomoConfig.getEnvironment())
            .header("Ocp-Apim-Subscription-Key", MTNMomoConfig.getSubscriptionKey())
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getString("status");
        }
        
        throw new IOException("Failed to get payment status: " + response.body());
    }
    
    public double getAccountBalance() throws IOException, InterruptedException {
        String token = getAccessToken();
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(MTNMomoConfig.ACCOUNT_BALANCE_URL))
            .header("Authorization", "Bearer " + token)
            .header("X-Target-Environment", MTNMomoConfig.getEnvironment())
            .header("Ocp-Apim-Subscription-Key", MTNMomoConfig.getSubscriptionKey())
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getDouble("availableBalance");
        }
        
        throw new IOException("Failed to get account balance: " + response.body());
    }
}