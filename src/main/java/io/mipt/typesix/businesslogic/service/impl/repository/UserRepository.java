package io.mipt.typesix.businesslogic.service.impl.repository;

import io.mipt.typesix.businesslogic.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
