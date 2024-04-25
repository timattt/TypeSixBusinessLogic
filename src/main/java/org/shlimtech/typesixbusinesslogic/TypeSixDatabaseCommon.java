package org.shlimtech.typesixbusinesslogic;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackages = {"org.shlimtech.typesixbusinesslogic"})
@EnableJpaRepositories(basePackages = "org.shlimtech.typesixbusinesslogic")
@EntityScan(basePackages = "org.shlimtech.typesixdatabasecommon")
public @interface TypeSixDatabaseCommon {
}
