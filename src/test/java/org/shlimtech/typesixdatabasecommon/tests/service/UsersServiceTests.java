package org.shlimtech.typesixdatabasecommon.tests.service;

import org.junit.jupiter.api.Test;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.shlimtech.typesixdatabasecommon.tests.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class UsersServiceTests extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void simpleCreationTest() {
        userService.createOrComplementUser(UserDTO.builder().email("ggg@gmail.com").firstName("hhh").build());
        Assert.isTrue(userService.loadUser("ggg@gmail.com") != null, "must contains user");
    }

    @Test
    public void complementTest() {
        UserDTO dto = userService.createOrComplementUser(UserDTO.builder().email("hhh@gmail.com").firstName("hhh").build());
        Assert.isTrue(userService.createOrComplementUser(UserDTO.builder().email("hhh@gmail.com").firstName("hhh").build()).getId() == dto.getId(), "complementation test error");
    }

}
