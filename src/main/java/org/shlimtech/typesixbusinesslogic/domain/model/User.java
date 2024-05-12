package org.shlimtech.typesixbusinesslogic.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "type6user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "login")
    private String login;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "biography")
    private String biography;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "phone")
    private String phone;

    @Column(name = "vk_link")
    private String vkLink;

    @Column(name = "github_link")
    private String githubLink;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    @Column(name = "password")
    private String password;
}
