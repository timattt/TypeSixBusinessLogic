package org.shlimtech.typesixdatabasecommon.tests;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shlimtech.typesixdatabasecommon.TypeSixDatabaseCommon;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {UserService.class})
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=sa",
        "spring.profiles.active=debug",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://google.com"
})
@RequiredArgsConstructor
@TypeSixDatabaseCommon
@EnableAutoConfiguration
public class BaseTest {

    @Autowired
    protected ModelMapper modelMapper;

}