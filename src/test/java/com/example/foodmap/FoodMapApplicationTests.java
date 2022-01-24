package com.example.foodmap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
class FoodMapApplicationTests {

    @Test
    void contextLoads() {
    }

}
