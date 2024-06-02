package io.mipt.typesix.businesslogic.service;

import io.mipt.typesix.businesslogic.domain.model.User;
import io.mipt.typesix.businesslogic.domain.model.UserStatus;
import io.mipt.typesix.businesslogic.service.core.RandomStringsGeneratorService;
import io.mipt.typesix.businesslogic.service.impl.RegistrationServiceImpl;
import io.mipt.typesix.businesslogic.service.impl.repository.UserRepository;
import io.mipt.typesix.businesslogic.service.core.RegistrationException;
import io.mipt.typesix.businesslogic.service.core.RegistrationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
        verify(userRepository).save(ArgumentMatchers.eq(User.builder().email(TEST_EMAIL).code(RANDOM_CODE).status(UserStatus.pending).build()));
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
