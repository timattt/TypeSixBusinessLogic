package org.shlimtech.typesixdatabasecommon.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "type6user")
public class User {

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

}
