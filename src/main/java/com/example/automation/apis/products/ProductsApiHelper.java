package com.example.automation.apis.products;

import com.example.automation.apis.BaseApiHelper;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;

public class ProductsApiHelper extends BaseApiHelper {

    public ProductsApiHelper(SHAFT.API api) {
        super(api);
    }

    @Step("Create Product")
    public ProductsApiHelper createProduct(String name, String sku, String price) {
        String body = SHAFT.CLI.file()
                .readFile("src/test/resources/testDataFiles/APIs/CreateProduct.json")
                .replace("{{name}}", name + TestData.getString(TestData.TestDataKey.UUID))
                .replace("{{sku}}", sku + TestData.getString(TestData.TestDataKey.UUID))
                .replace("{{price}}", price);

        post("api/products")
                .setRequestBody(body)
                .perform();

        TestData.set(TestData.TestDataKey.PRODUCT_ID,
                Integer.parseInt(api.getResponseJSONValue("id")));
        return this;
    }

    @Step("Get all products")
    public ProductsApiHelper getProducts() {
        get("api/products").perform();
        return this;
    }

    @Step("Validate product was created successfully")
    public ProductsApiHelper validateProductCreated(String expectedName) {
        get("api/products").perform();
        api.assertThatResponse()
                .extractedJsonValue("[?(@.id == " +
                        TestData.get(TestData.TestDataKey.PRODUCT_ID, Integer.class) + ")].name")
                .isEqualTo(expectedName + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Get product ID by name")
    public String getProductId(String name) {
        get("api/products").perform();
        return api.getResponseJSONValue("[?(@.name== '" + name + "')].id");
    }
}
