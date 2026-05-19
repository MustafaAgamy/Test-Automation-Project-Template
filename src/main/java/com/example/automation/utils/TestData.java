package com.example.automation.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe per-test data store for values shared between steps within a single test.
 *
 * <p>Backed by a {@link ThreadLocal} map so parallel test threads never share state.
 * Call {@link #clear()} in {@code @AfterMethod} to prevent leaking values across tests.
 */
public class TestData {

    private static final ThreadLocal<Map<TestDataKey, Object>> context =
            ThreadLocal.withInitial(HashMap::new);

    /**
     * Stores {@code value} under {@code key} for the current thread.
     */
    public static void set(TestDataKey key, Object value) {
        context.get().put(key, value);
    }

    /**
     * Returns the value stored under {@code key}, cast to {@code type}.
     * Returns {@code null} if the key has not been set.
     */
    public static <T> T get(TestDataKey key, Class<T> type) {
        return type.cast(context.get().get(key));
    }

    /**
     * Returns the value stored under {@code key} as a {@link String}.
     *
     * @throws IllegalArgumentException if the key has not been set
     */
    public static String getString(TestDataKey key) {
        Object value = context.get().get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing test data for key: " + key);
        }
        return value.toString();
    }

    /**
     * Removes all data for the current thread. Call this in {@code @AfterMethod}
     * to prevent values from leaking into subsequent tests on the same thread.
     */
    public static void clear() {
        context.remove();
    }

    /** Keys available for storing and retrieving test data. */
    public enum TestDataKey {
        UUID,
        PRODUCT_ID
    }
}
