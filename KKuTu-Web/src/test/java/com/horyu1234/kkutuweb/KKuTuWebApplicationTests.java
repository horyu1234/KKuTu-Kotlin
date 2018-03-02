package com.horyu1234.kkutuweb;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KKuTuWebApplicationTests {
    @BeforeClass
    public static void setUp() {
        System.setProperty("app.home", "src/test/resource");
    }

    @Test
    public void contextLoads() {
    }
}
