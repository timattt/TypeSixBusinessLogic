package io.mipt.typesix.businesslogic.service.impl;

import io.mipt.typesix.businesslogic.domain.model.User;
import io.mipt.typesix.businesslogic.domain.model.UserStatus;
import io.mipt.typesix.businesslogic.service.impl.repository.UserRepository;
import io.mipt.typesix.businesslogic.service.core.RandomStringsGeneratorService;
import io.mipt.typesix.businesslogic.service.core.RegistrationException;
import io.mipt.typesix.businesslogic.service.core.RegistrationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RandomStringsGeneratorService randomStringsGeneratorService;
    private final UserRepository userRepository;
    private BiConsumer<String, String> codeSender;

    @Override
    @Transactional
    public void beginRegistrationFlow(String email) {
        Assert.notNull(email, "email must be not null");

        if (codeSender == null) {
            throw new RegistrationException("code sender must be not null");
        }
        User user;
        if ((user = userRepository.findByEmail(email)) != null && user.getStatus() != UserStatus.pending) {
            throw new RegistrationException("Active user with this email already exists");
        }
        if (user == null) {
            user = User.builder().email(email).status(UserStatus.pending).build();
        }

        String code = randomStringsGeneratorService.generateCode();

        codeSender.accept(code, email);

        user.setCode(code);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void checkValidCode(String email, String code) {
        Assert.notNull(email, "email must be not null");
        Assert.notNull(code, "code must be not null");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RegistrationException("No user with such email");
        }
        if (user.getCode() == null) {
            throw new RegistrationException("User has no active code");
        }
        if (user.getStatus() != UserStatus.pending) {
            throw new RegistrationException("User has incorrect status");
        }
        if (!user.getCode().equals(code)) {
            throw new RegistrationException("Invalid code");
        }
    }

    @Override
    @Transactional
    public void endRegistrationFlow(String email, String code, String password) {
        Assert.notNull(email, "email must be not null");
        Assert.notNull(code, "code must be not null");
        Assert.notNull(password, "password must be not null");

        checkValidCode(email, code);
        User user = userRepository.findByEmail(email);

        user.setStatus(UserStatus.active);
        user.setCode(null);
        user.setPassword(password);

        userRepository.save(user);
    }

    @Override
    public void setCodeSender(BiConsumer<String, String> codeConsumer) {
        Assert.notNull(codeConsumer, "codeConsumer must be not null");
        this.codeSender = codeConsumer;
    }
}
