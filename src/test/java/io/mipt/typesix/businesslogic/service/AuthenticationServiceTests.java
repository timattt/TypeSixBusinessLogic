package io.mipt.typesix.businesslogic.service;

import io.mipt.typesix.businesslogic.domain.model.User;
import io.mipt.typesix.businesslogic.domain.model.UserStatus;
import io.mipt.typesix.businesslogic.service.impl.AuthenticationServiceImpl;
import io.mipt.typesix.businesslogic.service.impl.repository.UserRepository;
import io.mipt.typesix.businesslogic.service.core.AuthenticationException;
import io.mipt.typesix.businesslogic.service.core.AuthenticationService;
import io.mipt.typesix.businesslogic.service.core.RandomStringsGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AuthenticationService.class, AuthenticationServiceImpl.class})
public class AuthenticationServiceTests {

    private static final String RANDOM_PASSWORD = "cde";
    private static final String TEST_CODE = "abd";
    private static final String TEST_EMAIL = "a@gmail.com";
    private static final String TEST_LOGIN = "login";
    private static final int TEST_ID = 11;

    @MockBean
    private RandomStringsGeneratorService randomStringsGeneratorService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setupMocks() {
        when(randomStringsGeneratorService.generatePassword()).thenReturn(RANDOM_PASSWORD);
    }

    @Test
    public void customizeTokenCorrectTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(User.builder().email(TEST_EMAIL).id(TEST_ID).status(UserStatus.active).build());

        Map<String, String> prediction = new HashMap<>(Map.of("email", TEST_EMAIL, "id", String.valueOf(TEST_ID)));

        authenticationService.customizeToken(TEST_EMAIL, prediction::remove);

        Assertions.assertTrue(prediction.isEmpty());
    }

    @Test
    public void loadUserCorrectTest() {
        User mockUser = User.builder().email(TEST_EMAIL).id(TEST_ID).status(UserStatus.active).build();
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(mockUser);

        Assertions.assertEquals(authenticationService.loadUser(TEST_EMAIL), mockUser);
    }

    @Test
    public void loadUserNoSuchUserTest() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.loadUser(TEST_EMAIL));
    }

    @Test
    public void ensureActiveUserExistsAlreadyExistsTest() {
        User mockUser = User.builder().email(TEST_EMAIL).id(TEST_ID).status(UserStatus.active).build();
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(mockUser);

        Assertions.assertFalse(authenticationService.ensureActiveUserExists(TEST_EMAIL));
    }

    @Test
    public void ensureActiveUserExistsNotExistsTest() {
        User mockUser = User.builder().email(TEST_EMAIL).status(UserStatus.active).password(RANDOM_PASSWORD).build();
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(null);

        Assertions.assertTrue(authenticationService.ensureActiveUserExists(TEST_EMAIL));

        verify(userRepository).save(eq(mockUser));
    }

    @Test
    public void ensureActiveUserExistsPendingUserExistsTest() {
        User before = User.builder().email(TEST_EMAIL).status(UserStatus.pending).code(TEST_CODE).build();
        User after = User.builder().email(TEST_EMAIL).status(UserStatus.active).build();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(before);

        Assertions.assertFalse(authenticationService.ensureActiveUserExists(TEST_EMAIL));

        verify(userRepository).save(eq(after));
    }

    @Test
    public void complementUserByOauth2ProviderDataCorrectTest() {
        User before = User.builder().email(TEST_EMAIL).status(UserStatus.active).build();
        User after = User.builder().email(TEST_EMAIL).status(UserStatus.active).login(TEST_LOGIN).build();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(before);

        Map<String, Object> input = Map.of("login", TEST_LOGIN);

        authenticationService.complementUserByOauth2ProviderData(TEST_EMAIL, input);

        verify(userRepository).save(eq(after));
    }
}
