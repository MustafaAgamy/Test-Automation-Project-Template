package com.example.automation.apis;

import com.shaft.api.RequestBuilder;
import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

/**
 * Base class for API helpers. Provides pre-configured {@link RequestBuilder} factory
 * methods that apply shared defaults to every request: relaxed HTTPS validation,
 * JSON content type, and the {@code API-KEY} header sourced from the {@code apiKey}
 * system property.
 */
public abstract class BaseApiHelper {

    protected final SHAFT.API api;

    protected BaseApiHelper(SHAFT.API api) {
        this.api = api;
    }

    /**
     * Returns a POST {@link RequestBuilder} for {@code endpoint} with shared defaults applied.
     */
    protected RequestBuilder post(String endpoint) {
        return api.post(endpoint)
                .useRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("API-KEY", System.getProperty("apiKey"));
    }

    /**
     * Returns a GET {@link RequestBuilder} for {@code endpoint} with shared defaults applied.
     */
    protected RequestBuilder get(String endpoint) {
        return api.get(endpoint)
                .useRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("API-KEY", System.getProperty("apiKey"));
    }

    /**
     * Returns a PUT {@link RequestBuilder} for {@code endpoint} with shared defaults applied.
     */
    protected RequestBuilder put(String endpoint) {
        return api.put(endpoint)
                .useRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("API-KEY", System.getProperty("apiKey"));
    }
}
