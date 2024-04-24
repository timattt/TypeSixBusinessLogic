package org.shlimtech.typesixdatabasecommon.service.core;

import java.util.function.Consumer;

public interface RegistrationService {
    void beginRegistrationFlow(String email);

    void checkValidCode(String email, String code);

    void endRegistrationFlow(String email, String code, String password);

    void setCodeSender(Consumer<String> codeConsumer);
}
