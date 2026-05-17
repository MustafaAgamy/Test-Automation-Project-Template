package com.example.automation.apis.orders;

import com.example.automation.apis.BaseApiHelper;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;

public class OrdersApiHelper extends BaseApiHelper {

    public OrdersApiHelper(SHAFT.API api) {
        super(api);
    }

    @Step("Create Order")
    public OrdersApiHelper createOrder(String reference, String customerName,
                                       String productName, String quantity, String unitPrice) {
        String uuid = TestData.getString(TestData.TestDataKey.UUID);
        String body = SHAFT.CLI.file()
                .readFile("src/test/resources/testDataFiles/APIs/CreateOrder.json")
                .replace("{{reference}}", reference + uuid)
                .replace("{{customerName}}", customerName)
                .replace("{{productName}}", productName)
                .replace("{{quantity}}", quantity)
                .replace("{{unitPrice}}", unitPrice);

        post("api/v1/orders")
                .setRequestBody(body)
                .perform();

        TestData.set(TestData.TestDataKey.ORDER_ID, Integer.parseInt(api.getResponseJSONValue("order.id")));
        return this;
    }

    @Step("Validate order status is {expectedStatus}")
    public OrdersApiHelper validateOrderStatus(String expectedStatus) {
        get("api/v1/orders/" + TestData.getString(TestData.TestDataKey.ORDER_ID))
                .perform();
        api.assertThatResponse()
                .extractedJsonValue("order.status")
                .isEqualTo(expectedStatus);
        return this;
    }
}
