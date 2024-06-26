package io.mipt.typesix.businesslogic.service.impl;

import io.mipt.typesix.businesslogic.domain.model.User;
import io.mipt.typesix.businesslogic.domain.model.UserStatus;
import io.mipt.typesix.businesslogic.service.core.AuthenticationException;
import io.mipt.typesix.businesslogic.service.core.AuthenticationService;
import io.mipt.typesix.businesslogic.service.core.RandomStringsGeneratorService;
import io.mipt.typesix.businesslogic.service.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
        Assert.notNull(email, "email must not be null");
        Assert.notNull(claimsConsumer, "claimsConsumer must not be null");

        User user = loadUser(email);
        claimsConsumer.accept("email", user.getEmail());
        claimsConsumer.accept("id", Integer.toString(user.getId()));
    }

    @Override
    @Transactional
    public User loadUser(String email) {
        Assert.notNull(email, "email must not be null");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new AuthenticationException("No such user");
        }

        return user;
    }

    @Override
    @Transactional
    public boolean ensureActiveUserExists(String email) {
        Assert.notNull(email, "email must not be null");

        User user = userRepository.findByEmail(email);

        if (user != null) {
            if (user.getStatus() != UserStatus.active) {
                user.setStatus(UserStatus.active);
                user.setCode(null);
                userRepository.save(user);
            }
            return false;
        }

        userRepository.save(User.builder().email(email).status(UserStatus.active).password(randomStringsGeneratorService.generatePassword()).build());

        return true;
    }

    @Override
    @Transactional
    public void complementUserByOauth2ProviderData(String email, Map<String, Object> attributes) {
        Assert.notNull(email, "email must not be null");
        Assert.notNull(attributes, "attributes must not be null");

        User user = loadUser(email);

        analyzeGithub(user, attributes);
        analyzeVK(user, attributes);
        analyzeYandex(user, attributes);

        userRepository.save(user);
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
