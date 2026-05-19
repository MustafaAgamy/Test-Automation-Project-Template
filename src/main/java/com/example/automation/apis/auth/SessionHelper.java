package com.example.automation.apis.auth;

import com.example.automation.apis.BaseApiHelper;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import lombok.Getter;

public class SessionHelper extends BaseApiHelper {

    @Getter
    private static volatile String sessionCookie;

    public SessionHelper(SHAFT.API api) {
        super(api);
    }

    @Step("Login")
    public SessionHelper login() {
        if (sessionCookie == null) {
            String body = SHAFT.CLI.file().readFile("src/test/resources/testDataFiles/Auth/Login.json")
                    .replace("{{email}}", System.getProperty("testEmail"))
                    .replace("{{password}}", System.getProperty("testPassword"));
            post("api/auth/login")
                    .setRequestBody(body)
                    .perform();
            sessionCookie = api.getResponse().header("Set-Cookie");
        }
        return this;
    }

    @Step("Validate login was successful")
    public SessionHelper validateLoginSuccessful() {
        api.assertThatResponse().body().contains("success");
        return this;
    }
}
