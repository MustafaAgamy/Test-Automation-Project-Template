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
        String uuid = TestData.getString(TestData.TestDataKey.UUID);
        String body = SHAFT.CLI.file()
                .readFile("src/test/resources/testDataFiles/APIs/CreateProduct.json")
                .replace("{{name}}", name + uuid)
                .replace("{{sku}}", sku + uuid)
                .replace("{{price}}", price);

        post("api/v1/products")
                .setRequestBody(body)
                .perform();

        TestData.set(TestData.TestDataKey.PRODUCT_ID, Integer.parseInt(api.getResponseJSONValue("product.id")));
        return this;
    }

    @Step("Validate product was created successfully")
    public ProductsApiHelper validateProductCreated(String expectedName) {
        String uuid = TestData.getString(TestData.TestDataKey.UUID);
        get("api/v1/products?per_page=100&page=1")
                .perform();
        api.assertThatResponse()
                .extractedJsonValue("products[?(@.id == '" +
                        TestData.getString(TestData.TestDataKey.PRODUCT_ID) + "')].name")
                .isEqualTo(expectedName + uuid);
        return this;
    }

    @Step("Get product ID by name")
    public String getProductId(String name) {
        get("api/v1/products?per_page=100&page=1")
                .perform();
        return api.getResponseJSONValue("products[?(@.name== '" + name + "')].id");
    }
}
