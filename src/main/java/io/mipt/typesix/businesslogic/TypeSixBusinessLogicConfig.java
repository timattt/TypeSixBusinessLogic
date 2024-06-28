package io.mipt.typesix.businesslogic;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@Configuration
@ComponentScan
@EnableJpaRepositories
@PropertySource("classpath:db/db.properties")
public class TypeSixBusinessLogicConfig {
}