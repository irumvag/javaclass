package com.dineix.web.payment;

public class MTNMomoConfig {
    // MTN MOMO API Configuration
    private static final String SUBSCRIPTION_KEY = "YOUR_SUBSCRIPTION_KEY";
    private static final String API_USER = "YOUR_API_USER";
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String ENVIRONMENT = "sandbox"; // or "production"
    
    // API Endpoints
    private static final String BASE_URL = ENVIRONMENT.equals("sandbox") 
        ? "https://sandbox.momodeveloper.mtn.com" 
        : "https://momodeveloper.mtn.com";
    
    public static final String TOKEN_URL = BASE_URL + "/collection/token/";
    public static final String PAYMENT_REQUEST_URL = BASE_URL + "/collection/v1_0/requesttopay";
    public static final String PAYMENT_STATUS_URL = BASE_URL + "/collection/v1_0/requesttopay/{referenceId}";
    public static final String ACCOUNT_BALANCE_URL = BASE_URL + "/collection/v1_0/account/balance";
    
    public static String getSubscriptionKey() {
        return SUBSCRIPTION_KEY;
    }
    
    public static String getApiUser() {
        return API_USER;
    }
    
    public static String getApiKey() {
        return API_KEY;
    }
    
    public static String getEnvironment() {
        return ENVIRONMENT;
    }
}