package org.shlimtech.typesixbusinesslogic.service.impl.repository;

import org.shlimtech.typesixbusinesslogic.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
