package com.example.automation.apis.customers;

import com.example.automation.apis.BaseApiHelper;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;

public class CustomersApiHelper extends BaseApiHelper {

    public CustomersApiHelper(SHAFT.API api) {
        super(api);
    }

    @Step("Create Customer")
    public CustomersApiHelper createCustomer(String name, String email) {
        String uuid = TestData.getString(TestData.TestDataKey.UUID);
        String body = SHAFT.CLI.file()
                .readFile("src/test/resources/testDataFiles/APIs/CreateCustomer.json")
                .replace("{{name}}", name + uuid)
                .replace("{{email}}", uuid + email);

        post("api/v1/customers")
                .setRequestBody(body)
                .perform();

        TestData.set(TestData.TestDataKey.CUSTOMER_ID, Integer.parseInt(api.getResponseJSONValue("customer.id")));
        return this;
    }

    @Step("Validate customer was created successfully")
    public CustomersApiHelper validateCustomerCreated(String expectedName) {
        String uuid = TestData.getString(TestData.TestDataKey.UUID);
        get("api/v1/customers?per_page=100&page=1")
                .perform();
        api.assertThatResponse()
                .extractedJsonValue("customers[?(@.id == '" +
                        TestData.getString(TestData.TestDataKey.CUSTOMER_ID) + "')].name")
                .isEqualTo(expectedName + uuid);
        return this;
    }
}
