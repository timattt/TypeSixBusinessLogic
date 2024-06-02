package io.mipt.typesix.businesslogic.service.core;

import io.mipt.typesix.businesslogic.domain.model.User;

import java.util.Map;
import java.util.function.BiConsumer;

public interface AuthenticationService {
    /**
     * Customizes token for given user.
     * Can add key-value items like email, id...
     *
     * @param email          Email of user for which token is customized. Not null.
     * @param claimsConsumer Consumer used to customize token. Send KV items here. Not null.
     * @throws AuthenticationException If loadUser method fails.
     */
    void customizeToken(String email, BiConsumer<String, String> claimsConsumer) throws AuthenticationException;

    /**
     * Loads user from database.
     *
     * @param email Email to identify user. Not null.
     * @return User with given email from database
     * @throws AuthenticationException if user with such email is not in the database
     */
    User loadUser(String email) throws AuthenticationException;

    /**
     * Checks user with such email in the database. If it is not presented, then it will be created.
     * If user is presented and is currently pending, it will make it active and remove code.
     *
     * @param email Email to identify user. Not null.
     * @return True if user was created in the database, False if user already was in the database.
     */
    boolean ensureActiveUserExists(String email);

    /**
     * Select required attributes from given map. For specified providers. Such as Yandex, VK, GitHub.
     *
     * @param email      Email that identifies user. Not null.
     * @param attributes Attributes for this user given by the provider.
     * @throws AuthenticationException if no such user presented in database.
     */
    void complementUserByOauth2ProviderData(String email, Map<String, Object> attributes) throws AuthenticationException;
}
