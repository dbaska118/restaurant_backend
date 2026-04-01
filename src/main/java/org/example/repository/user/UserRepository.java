package org.example.repository.user;

import org.example.model.user.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public List<User> findAllByEnabledTrueAndRoleInOrderByIdAsc(List<String> roles);


}
