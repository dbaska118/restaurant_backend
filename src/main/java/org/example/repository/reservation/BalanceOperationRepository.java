package org.example.repository.reservation;

import org.example.model.reservation.BalanceOperation;
import org.example.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceOperationRepository extends JpaRepository<BalanceOperation, Long> {

    List<BalanceOperation> findAllByUser(User user);
}
