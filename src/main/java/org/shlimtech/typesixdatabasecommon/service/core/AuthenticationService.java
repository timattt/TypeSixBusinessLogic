package org.shlimtech.typesixdatabasecommon.service.core;

import org.shlimtech.typesixdatabasecommon.domain.model.User;

import java.util.Map;
import java.util.function.BiConsumer;

public interface AuthenticationService {
    void customizeToken(String email, BiConsumer<String, String> claimsConsumer);

    User loadUser(String email);

    boolean ensureActiveUserExists(String email);

    void complementUserByOauth2ProviderData(String email, Map<String, Object> attributes);
}
