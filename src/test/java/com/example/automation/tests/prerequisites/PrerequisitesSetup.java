package com.example.automation.tests.prerequisites;

import com.example.automation.apis.customers.CustomersApiHelper;
import com.example.automation.apis.products.ProductsApiHelper;
import com.example.automation.utils.CustomMethods;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Demo Store")
@Feature("Prerequisites")
@Story("Shared test data setup")
public class PrerequisitesSetup {

    private final ThreadLocal<SHAFT.API> api = new ThreadLocal<>();
    private SHAFT.TestData.JSON testData;

    @Test(description = "Create shared prerequisite product and customer")
    public void createPrerequisites() {
        new ProductsApiHelper(api.get())
                .createProduct(
                        testData.getTestData("product.name"),
                        testData.getTestData("product.sku"),
                        testData.getTestData("product.price"))
                .validateProductCreated(testData.getTestData("product.name"));

        new CustomersApiHelper(api.get())
                .createCustomer(
                        testData.getTestData("customer.name"),
                        "prereq@example.com")
                .validateCustomerCreated(testData.getTestData("customer.name"));
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        testData = new SHAFT.TestData.JSON(
                "src/test/resources/testDataFiles/Prerequisites/DemoStorePreRequisites.json");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        api.set(new SHAFT.API(System.getProperty("baseUri")));
        TestData.set(TestData.TestDataKey.UUID, CustomMethods.generateTimeStamp());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        TestData.clear();
    }
}
