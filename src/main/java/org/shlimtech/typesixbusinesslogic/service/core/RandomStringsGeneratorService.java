package org.shlimtech.typesixbusinesslogic.service.core;

public interface RandomStringsGeneratorService {
    int REGISTRATION_CODE_LENGTH = 10;
    int RANDOM_PASSWORD_LENGTH = 10;

    /**
     * Generates random alphabetic string with length REGISTRATION_CODE_LENGTH.
     *
     * @return Random alphabetic string.
     */
    String generateCode();

    /**
     * Generates random alphabetic string with length RANDOM_PASSWORD_LENGTH.
     *
     * @return Random alphabetic string.
     */
    String generatePassword();
}
