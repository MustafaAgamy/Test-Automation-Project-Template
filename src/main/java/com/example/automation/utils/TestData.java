package com.example.automation.utils;

import java.util.HashMap;
import java.util.Map;

public class TestData {

    private static final ThreadLocal<Map<TestDataKey, Object>> context =
            ThreadLocal.withInitial(HashMap::new);

    public static void set(TestDataKey key, Object value) {
        context.get().put(key, value);
    }

    public static <T> T get(TestDataKey key, Class<T> type) {
        return type.cast(context.get().get(key));
    }

    public static String getString(TestDataKey key) {
        Object value = context.get().get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing test data for key: " + key);
        }
        return value.toString();
    }

    public static void clear() {
        context.remove();
    }

    public enum TestDataKey {
        UUID,
        PRODUCT_ID
    }
}
