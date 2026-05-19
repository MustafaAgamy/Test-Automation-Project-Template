package com.example.automation.tests.e2e;

import com.example.automation.apis.products.ProductsApiHelper;
import com.example.automation.pages.products.CreateProductPage;
import com.example.automation.pages.products.ProductsListPage;
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
@Feature("Products")
@Story("Product E2E Tests")
public class ProductE2ETest extends BaseE2ETest {

    private final ThreadLocal<SHAFT.GUI.WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<SHAFT.API> api = new ThreadLocal<>();
    private SHAFT.TestData.JSON testData;

    @Test(groups = {"browser"}, description = "Verify The User Can Create Product Successfully")
    public void verifyTheUserCanCreateProductSuccessfully() {
        new CreateProductPage(driver.get())
                .navigateToCreateProduct()
                .createProduct(testData.getTestData("product.name"),
                        testData.getTestData("product.sku"), testData.getTestData("product.price"))
                .validateProductCreatedSuccessfully(testData.getTestData("successMessage"));
    }

    @Test(groups = {"browser"}, description = "A product created via API appears in the products list")
    public void createdProductAppearsInList() {
        new ProductsApiHelper(api.get())
                .createProduct(testData.getTestData("product.name"),
                        testData.getTestData("product.sku"), testData.getTestData("product.price"));

        new ProductsListPage(driver.get())
                .navigateToProductsList()
                .searchForProduct(testData.getTestData("product.name"))
                .validateProductStatus(testData.getTestData("approvedStatus"));
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        testData = new SHAFT.TestData.JSON("src/test/resources/testDataFiles/E2E/Product.json");
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
