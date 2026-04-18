package com.emmanueljeanpierreauguste.riubackend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Basic smoke test for the application main method.
 */
class RiubackendApplicationTests {

    @Test
    void mainMethodShouldNotThrow() {
        assertDoesNotThrow(() -> RiubackendApplication.class.getDeclaredConstructor().newInstance());
    }
}
