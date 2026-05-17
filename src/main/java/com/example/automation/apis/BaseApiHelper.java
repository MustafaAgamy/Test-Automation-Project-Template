package com.example.automation.apis;

import com.shaft.api.RequestBuilder;
import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

public abstract class BaseApiHelper {

    protected final SHAFT.API api;

    protected BaseApiHelper(SHAFT.API api) {
        this.api = api;
    }

    protected RequestBuilder post(String endpoint) {
        return api.post(endpoint)
                .useRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("API-KEY", System.getProperty("apiKey"));
    }

    protected RequestBuilder get(String endpoint) {
        return api.get(endpoint)
                .useRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("API-KEY", System.getProperty("apiKey"));
    }

    protected RequestBuilder put(String endpoint) {
        return api.put(endpoint)
                .useRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("API-KEY", System.getProperty("apiKey"));
    }
}
