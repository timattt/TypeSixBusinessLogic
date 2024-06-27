module io.mipt.typesix.businesslogic {
    requires static lombok;
    requires spring.context;
    requires spring.core;
    requires spring.boot.starter.data.jpa;
    requires spring.data.jpa;
    requires spring.boot.autoconfigure;
    requires spring.tx;
    requires jakarta.persistence;
    requires spring.aop;
    requires org.hibernate.orm.core;

    opens io.mipt.typesix.businesslogic.service.impl;
    opens io.mipt.typesix.businesslogic;
    opens io.mipt.typesix.businesslogic.domain.model;
    opens io.mipt.typesix.businesslogic.service.core;
    opens io.mipt.typesix.businesslogic.service.core.repository;

    exports io.mipt.typesix.businesslogic.service.core;
    exports io.mipt.typesix.businesslogic.service.core.repository;
    exports io.mipt.typesix.businesslogic.domain.model;
}