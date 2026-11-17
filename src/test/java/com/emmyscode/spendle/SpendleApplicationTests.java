package com.emmyscode.spendle;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled for cloud builds where database is not available during build phase")
class SpendleApplicationTests {

    @Test
    void contextLoads() {
    }

}
