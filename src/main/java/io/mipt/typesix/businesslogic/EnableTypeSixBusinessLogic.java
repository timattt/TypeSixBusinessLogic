package io.mipt.typesix.businesslogic;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackages = {"io.mipt"})
@EnableJpaRepositories(basePackages = "io.mipt")
@EntityScan(basePackages = "io.mipt")
public @interface EnableTypeSixBusinessLogic {
}
