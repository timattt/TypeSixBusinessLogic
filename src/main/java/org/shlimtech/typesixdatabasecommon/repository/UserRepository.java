package org.shlimtech.typesixdatabasecommon.repository;

import org.shlimtech.typesixdatabasecommon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
