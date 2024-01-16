package org.shlimtech.typesixdatabasecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String email;
    private String login;
    private String firstName;
    private String lastName;
    private String biography;
    private String birthday;
    private String phone;
    private String vkLink;
    private String githubLink;
}
