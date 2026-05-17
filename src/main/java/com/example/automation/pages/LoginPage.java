package com.example.automation.pages;

import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage {

    private final SHAFT.GUI.WebDriver driver;

    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");

    public LoginPage(SHAFT.GUI.WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to login page")
    public LoginPage navigateToLoginPage() {
        driver.browser().navigateToURL(System.getProperty("baseUri") + "login");
        return this;
    }

    @Step("Login with email: {email}")
    public LoginPage login(String email, String password) {
        driver.element().type(emailField, email);
        driver.element().type(passwordField, password);
        driver.element().click(loginButton);
        return this;
    }

    @Step("Validate user is logged in")
    public LoginPage validateLoggedIn() {
        driver.assertThat().element(By.cssSelector(".dashboard")).exists();
        return this;
    }
}
