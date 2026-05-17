package com.example.automation.tests.api;

import com.example.automation.apis.orders.OrdersApiHelper;
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
@Feature("Orders")
@Story("Order API Tests")
public class CreateOrderApiTest {

    private final ThreadLocal<SHAFT.API> api = new ThreadLocal<>();
    private SHAFT.TestData.JSON testData;
    private SHAFT.TestData.JSON prerequisites;

    @Test(description = "Verify an order can be created via API")
    public void verifyCreateOrder() {
        new OrdersApiHelper(api.get())
                .createOrder(
                        testData.getTestData("order.reference"),
                        prerequisites.getTestData("customer.name"),
                        prerequisites.getTestData("product.name"),
                        "1",
                        "25.00")
                .validateOrderStatus("draft");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        testData = new SHAFT.TestData.JSON("src/test/resources/testDataFiles/E2E/Order.json");
        prerequisites = new SHAFT.TestData.JSON(
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
