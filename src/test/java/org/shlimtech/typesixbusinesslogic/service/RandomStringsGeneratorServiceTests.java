package org.shlimtech.typesixbusinesslogic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shlimtech.typesixbusinesslogic.service.core.RandomStringsGeneratorService;
import org.shlimtech.typesixbusinesslogic.service.impl.RandomStringsGeneratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.shlimtech.typesixbusinesslogic.service.core.RandomStringsGeneratorService.RANDOM_PASSWORD_LENGTH;
import static org.shlimtech.typesixbusinesslogic.service.core.RandomStringsGeneratorService.REGISTRATION_CODE_LENGTH;

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
