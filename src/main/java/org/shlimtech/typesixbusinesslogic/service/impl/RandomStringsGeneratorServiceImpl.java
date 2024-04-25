package org.shlimtech.typesixbusinesslogic.service.impl;

import org.shlimtech.typesixbusinesslogic.service.core.RandomStringsGeneratorService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomStringsGeneratorServiceImpl implements RandomStringsGeneratorService {
    @Override
    public String generateCode() {
        return generateRandomString(REGISTRATION_CODE_LENGTH);
    }

    @Override
    public String generatePassword() {
        return generateRandomString(RANDOM_PASSWORD_LENGTH);
    }

    private String generateRandomString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
