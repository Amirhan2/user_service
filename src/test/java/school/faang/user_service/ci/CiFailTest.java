package school.faang.user_service.ci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class CiFailTest {

    @Test
    @DisplayName("failed test for ci check")
    void ciTest_checkFailedTest() {
        fail();
    }
}
