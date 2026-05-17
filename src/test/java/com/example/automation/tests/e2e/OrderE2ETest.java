package com.example.automation.tests.e2e;

import com.example.automation.apis.orders.OrdersApiHelper;
import com.example.automation.pages.orders.CreateOrderPage;
import com.example.automation.pages.orders.OrdersListPage;
import com.example.automation.tests.BaseE2ETest;
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
@Story("Order E2E Tests")
public class OrderE2ETest extends BaseE2ETest {

    private final ThreadLocal<SHAFT.GUI.WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<SHAFT.API> api = new ThreadLocal<>();
    private SHAFT.TestData.JSON testData;
    private SHAFT.TestData.JSON prerequisites;

    @Test(description = "Verify an order can be created via UI and approved")
    public void verifyCreateOrderAndApprove() {
        new CreateOrderPage(driver.get())
                .navigateToCreateOrder()
                .typeReference(testData.getTestData("order.reference"))
                .selectCustomer(prerequisites.getTestData("customer.name"))
                .selectProduct(prerequisites.getTestData("product.name"))
                .approve();

        new OrdersListPage(driver.get())
                .searchForOrder(testData.getTestData("order.reference"))
                .validateOrderStatus(testData.getTestData("approvedStatus"));
    }

    @Test(description = "Verify an order created via API appears in UI with correct status")
    public void verifyOrderCreatedViaApiAppearsInUI() {
        new OrdersApiHelper(api.get())
                .createOrder(
                        testData.getTestData("order.reference"),
                        prerequisites.getTestData("customer.name"),
                        prerequisites.getTestData("product.name"),
                        "1", "25.00")
                .validateOrderStatus(testData.getTestData("draftStatus"));

        new OrdersListPage(driver.get())
                .searchForOrder(testData.getTestData("order.reference"))
                .validateOrderStatus(testData.getTestData("draftStatus"));
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
        driver.set(new SHAFT.GUI.WebDriver());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.get().quit();
        TestData.clear();
    }
}
