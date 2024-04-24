package org.shlimtech.typesixdatabasecommon.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shlimtech.typesixdatabasecommon.domain.model.User;
import org.shlimtech.typesixdatabasecommon.domain.model.UserStatus;
import org.shlimtech.typesixdatabasecommon.service.core.RandomStringsGeneratorService;
import org.shlimtech.typesixdatabasecommon.service.core.RegistrationException;
import org.shlimtech.typesixdatabasecommon.service.core.RegistrationService;
import org.shlimtech.typesixdatabasecommon.service.impl.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RandomStringsGeneratorService randomStringsGeneratorService;
    private final UserRepository userRepository;
    private Consumer<String> codeSender;

    @Override
    @Transactional
    public void beginRegistrationFlow(String email) {
        Assert.notNull(codeSender, "code sender must be not null");
        if (userRepository.findByEmail(email) != null) {
            throw new RegistrationException("User with this email already exists");
        }

        String code = randomStringsGeneratorService.generateCode();

        codeSender.accept(code);

        userRepository.save(User.builder().email(email).code(code).status(UserStatus.pending).build());
    }

    @Override
    @Transactional
    public void checkValidCode(String email, String code) {
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
        checkValidCode(email, code);
        User user = userRepository.findByEmail(email);

        user.setStatus(UserStatus.active);
        user.setCode(null);
        user.setPassword(password);
    }

    @Override
    public void setCodeSender(Consumer<String> codeConsumer) {
        this.codeSender = codeConsumer;
    }
}
