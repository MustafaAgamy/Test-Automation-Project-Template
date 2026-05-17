package com.example.automation.tests;

import com.example.automation.apis.auth.SessionHelper;
import com.shaft.driver.SHAFT;
import org.testng.annotations.BeforeSuite;

public class BaseE2ETest {

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        new SessionHelper(new SHAFT.API(System.getProperty("baseUri"))).login();
    }
}
