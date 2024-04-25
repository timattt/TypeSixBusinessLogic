package org.shlimtech.typesixbusinesslogic.service.core;

import java.util.function.BiConsumer;

public interface RegistrationService {
    /**
     * First step of registration flow.
     * Method setCodeSender must be invoked first.
     * Email must not be presented in database.
     * <br/>
     * Generates code via RandomStringsGeneratorService.
     * Sends this code to codeSender consumer.
     * Finally, creates new user into database with this code, given email and pending status code.
     *
     * @param email Email to create new user. Not null.
     * @throws RegistrationException If code sender is null, or if ACTIVE user with this email already exists.
     */
    void beginRegistrationFlow(String email) throws RegistrationException;

    /**
     * Checks code to be valid for this email.
     *
     * @param email Email to identify user in the database. Not null.
     * @param code  Code to compare with the one which is in the database. Not null.
     * @throws RegistrationException If user with this email is not presented,
     *                               or if user does not have code,
     *                               or if user state is not pending,
     *                               or if user code is not equal to given code.
     */
    void checkValidCode(String email, String code) throws RegistrationException;

    /**
     * Ends registration flow.
     * Sets user state to be activated.
     * Sets new password to user.
     * Sets user code to be null.
     *
     * @param email    Email to identify user. Not null.
     * @param code     Code received by user to confirm email. Not null.
     * @param password Password to set for this user. Not null.
     * @throws RegistrationException If checkValidCode method fails.
     */
    void endRegistrationFlow(String email, String code, String password) throws RegistrationException;

    /**
     * Sets code consumer for this service.
     * Code consumer will be called when registration flow is started.
     *
     * @param codeConsumer Code consumer lambda. (code, email) -> action. Not null.
     */
    void setCodeSender(BiConsumer<String, String> codeConsumer);
}
