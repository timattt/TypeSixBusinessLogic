package org.shlimtech.typesixbusinesslogic.service.core;

import org.shlimtech.typesixbusinesslogic.domain.model.User;

import java.util.Map;
import java.util.function.BiConsumer;

public interface AuthenticationService {
    /**
     * Customizes token for given user.
     * Can add key-value items like email, id...
     *
     * @param email          Email of user for which token is customized. Not null.
     * @param claimsConsumer Consumer used to customize token. Send KV items here. Not null.
     */
    void customizeToken(String email, BiConsumer<String, String> claimsConsumer);

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
