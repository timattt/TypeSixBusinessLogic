package org.shlimtech.typesixdatabasecommon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shlimtech.typesixdatabasecommon.domain.model.User;
import org.shlimtech.typesixdatabasecommon.domain.model.UserStatus;
import org.shlimtech.typesixdatabasecommon.service.core.AuthenticationException;
import org.shlimtech.typesixdatabasecommon.service.core.AuthenticationService;
import org.shlimtech.typesixdatabasecommon.service.core.RandomStringsGeneratorService;
import org.shlimtech.typesixdatabasecommon.service.impl.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RandomStringsGeneratorService randomStringsGeneratorService;

    @Override
    @Transactional
    public void customizeToken(String email, BiConsumer<String, String> claimsConsumer) {
        User user = loadUser(email);
        claimsConsumer.accept("email", user.getEmail());
        claimsConsumer.accept("id", Integer.toString(user.getId()));
    }

    @Override
    @Transactional
    public User loadUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new AuthenticationException("No such user");
        }

        return user;
    }

    @Override
    @Transactional
    public boolean ensureActiveUserExists(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return false;
        }

        userRepository.save(User.builder().email(email).status(UserStatus.active).password(randomStringsGeneratorService.generatePassword()).build());

        return true;
    }

    @Override
    @Transactional
    public void complementUserByOauth2ProviderData(String email, Map<String, Object> attributes) {
        User user = loadUser(email);

        analyzeGithub(user, attributes);
        analyzeVK(user, attributes);
        analyzeYandex(user, attributes);
    }

    private <T> void process(String key, Consumer<T> cons, Map<String, Object> attributes) {
        if (attributes.containsKey(key)) {
            cons.accept((T) attributes.get(key));
        }
    }

    private void analyzeYandex(User user, Map<String, Object> attributes) {
        process("login", user::setLogin, attributes);
        process("first_name", user::setFirstName, attributes);
        process("last_name", user::setLastName, attributes);
        process("birthday", user::setBirthday, attributes);
    }

    private void analyzeGithub(User user, Map<String, Object> attributes) {
        process("login", user::setLogin, attributes);
        process("name", user::setFirstName, attributes);
        process("bio", user::setBiography, attributes);
        process("url", user::setGithubLink, attributes);
    }

    private void analyzeVK(User user, Map<String, Object> attributes) {

    }
}
