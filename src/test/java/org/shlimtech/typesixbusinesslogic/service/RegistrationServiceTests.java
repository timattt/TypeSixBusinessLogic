package org.shlimtech.typesixbusinesslogic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shlimtech.typesixbusinesslogic.domain.model.User;
import org.shlimtech.typesixbusinesslogic.domain.model.UserStatus;
import org.shlimtech.typesixbusinesslogic.service.core.RandomStringsGeneratorService;
import org.shlimtech.typesixbusinesslogic.service.core.RegistrationException;
import org.shlimtech.typesixbusinesslogic.service.core.RegistrationService;
import org.shlimtech.typesixbusinesslogic.service.impl.RegistrationServiceImpl;
import org.shlimtech.typesixbusinesslogic.service.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.ReflectionUtils;

import java.util.function.BiConsumer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RegistrationService.class, RegistrationServiceImpl.class})
public class RegistrationServiceTests {

    private static final String RANDOM_CODE = "abc";
    private static final String DIFFERENT_CODE = "abc1";
    private static final String RANDOM_PASSWORD = "cde";
    private static final String TEST_EMAIL = "a@gmail.com";
    private static final String TEST_PASSWORD = "password";

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserRepository userRepository;

    private boolean codeReceived = false;

    @MockBean
    private RandomStringsGeneratorService randomStringsGeneratorService;

    private final BiConsumer<String, String> testConsumer = (code, email) -> {
        Assertions.assertEquals(code, RANDOM_CODE);
        Assertions.assertEquals(email, TEST_EMAIL);

        codeReceived = true;
    };

    @BeforeEach
    public void setupMocks() {
        when(randomStringsGeneratorService.generateCode()).thenReturn(RANDOM_CODE);
        when(randomStringsGeneratorService.generatePassword()).thenReturn(RANDOM_PASSWORD);

        codeReceived = false;
    }

    @Test
    public void beginRegistrationFlowNullCodeSenderTest() throws IllegalAccessException {
        ReflectionUtils.findField(RegistrationServiceImpl.class, "codeSender").setAccessible(true);
        ReflectionUtils.findField(RegistrationServiceImpl.class, "codeSender").set(registrationService, null);
        Assertions.assertThrows(RegistrationException.class, () -> registrationService.beginRegistrationFlow(TEST_EMAIL));
    }

    @Test
    public void beginRegistrationFlowCorrectTest() {
        registrationService.setCodeSender(testConsumer);
        registrationService.beginRegistrationFlow(TEST_EMAIL);

        Assertions.assertTrue(codeReceived);
        verify(userRepository).save(eq(User.builder().email(TEST_EMAIL).code(RANDOM_CODE).status(UserStatus.pending).build()));
    }

    @Test
    public void beginRegistrationFlowActiveUserAlreadyPresentedTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).status(UserStatus.active).build());

        registrationService.setCodeSender(testConsumer);
        Assertions.assertThrows(RegistrationException.class, () -> registrationService.beginRegistrationFlow(TEST_EMAIL));
    }

    @Test
    public void beginRegistrationFlowPendingUserAlreadyPresentedTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).status(UserStatus.pending).build());

        registrationService.setCodeSender(testConsumer);
        registrationService.beginRegistrationFlow(TEST_EMAIL);

        Assertions.assertTrue(codeReceived);
        verify(userRepository).save(eq(User.builder().email(TEST_EMAIL).code(RANDOM_CODE).status(UserStatus.pending).build()));
    }

    @Test
    public void checkValidCodeCorrectTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).code(RANDOM_CODE).status(UserStatus.pending).build());

        registrationService.checkValidCode(TEST_EMAIL, RANDOM_CODE);
    }

    @Test
    public void checkValidCodeNoSuchUserTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        Assertions.assertThrows(RegistrationException.class, () -> registrationService.checkValidCode(TEST_EMAIL, RANDOM_CODE));
    }

    @Test
    public void checkValidCodeNoCodeTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).status(UserStatus.pending).build());

        Assertions.assertThrows(RegistrationException.class, () -> registrationService.checkValidCode(TEST_EMAIL, RANDOM_CODE));
    }

    @Test
    public void checkValidCodeStatusNotPendingTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).code(RANDOM_CODE).status(UserStatus.active).build());

        Assertions.assertThrows(RegistrationException.class, () -> registrationService.checkValidCode(TEST_EMAIL, RANDOM_CODE));
    }

    @Test
    public void checkValidCodeInvalidCodeTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).code(DIFFERENT_CODE).status(UserStatus.pending).build());

        Assertions.assertThrows(RegistrationException.class, () -> registrationService.checkValidCode(TEST_EMAIL, RANDOM_CODE));
    }

    @Test
    public void endRegistrationFlowCorrectTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).code(RANDOM_CODE).status(UserStatus.pending).build());

        registrationService.endRegistrationFlow(TEST_EMAIL, RANDOM_CODE, TEST_PASSWORD);

        verify(userRepository).save(eq(User.builder().email(TEST_EMAIL).code(null).status(UserStatus.active).password(TEST_PASSWORD).build()));
    }

    /*
    @Test
    public void endRegistrationFlowTest() {

    }
*/
}
