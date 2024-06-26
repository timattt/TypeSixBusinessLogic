package io.mipt.typesix.businesslogic.service.core;

import io.mipt.typesix.businesslogic.service.impl.RandomStringsGeneratorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.mipt.typesix.businesslogic.service.core.RandomStringsGeneratorService.RANDOM_PASSWORD_LENGTH;
import static io.mipt.typesix.businesslogic.service.core.RandomStringsGeneratorService.REGISTRATION_CODE_LENGTH;

@SpringBootTest(classes = {RandomStringsGeneratorService.class, RandomStringsGeneratorServiceImpl.class})
public class RandomStringsGeneratorServiceTests {

    private static final int TOTAL_TRIES = 1000;

    @Autowired
    private RandomStringsGeneratorService randomStringsGeneratorService;

    @Test
    public void generatePasswordTest() {
        for (int i = 0; i < TOTAL_TRIES; i++) {
            String result = randomStringsGeneratorService.generateCode();
            Assertions.assertEquals(REGISTRATION_CODE_LENGTH, result.length());
            Assertions.assertTrue(result.matches("[a-zA-Z]+"));
        }
    }

    @Test
    public void generateCodeTest() {
        for (int i = 0; i < TOTAL_TRIES; i++) {
            String result = randomStringsGeneratorService.generatePassword();
            Assertions.assertEquals(RANDOM_PASSWORD_LENGTH, result.length());
            Assertions.assertTrue(result.matches("[a-zA-Z]+"));
        }
    }

}
