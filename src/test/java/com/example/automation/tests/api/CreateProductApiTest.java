package com.example.automation.tests.api;

import com.example.automation.apis.products.ProductsApiHelper;
import com.example.automation.utils.CustomMethods;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Demo Store")
@Feature("Products")
@Story("Product API Tests")
public class CreateProductApiTest {

    private final ThreadLocal<SHAFT.API> api = new ThreadLocal<>();
    private SHAFT.TestData.JSON testData;

    @Test(groups = {"api"}, description = "Creating a product returns HTTP 201 with the created object")
    public void createProductReturns201() {
        new ProductsApiHelper(api.get())
                .createProduct(
                        testData.getTestData("product.name"),
                        testData.getTestData("product.sku"),
                        "25.00");
        Assert.assertEquals(api.get().getResponse().getStatusCode(), 201,
                "Expected HTTP 201 Created");
        api.get().assertThatResponse().body()
                .contains(testData.getTestData("product.name"));
    }

    @Test(groups = {"api"}, description = "Getting the products list returns HTTP 200")
    public void getProductsReturnsList() {
        new ProductsApiHelper(api.get()).getProducts();
        Assert.assertEquals(api.get().getResponse().getStatusCode(), 200,
                "Expected HTTP 200 OK");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        testData = new SHAFT.TestData.JSON("src/test/resources/testDataFiles/E2E/Product.json");
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
