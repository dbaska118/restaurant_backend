package org.example.repository.balance;

import org.example.model.balance.BalanceOperation;
import org.example.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceOperationRepository extends JpaRepository<BalanceOperation, Long> {

    List<BalanceOperation> findAllByUser(User user);
}
