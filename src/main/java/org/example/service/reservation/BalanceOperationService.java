package org.example.service.reservation;

import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.model.reservation.BalanceOperation;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.reservation.BalanceOperationRepository;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BalanceOperationService {

    private final BalanceOperationRepository balanceOperationRepository;
    private final UserRepository userRepository;

    @Autowired
    public BalanceOperationService(BalanceOperationRepository balanceOperationRepository, UserRepository userRepository) {
        this.balanceOperationRepository = balanceOperationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<BalanceOperation> geAllOperationsByEmail(String email) {
       User user = userRepository.findWithLockByEmail(email).orElseThrow(UserNotFoundException::new);
       if(user instanceof Client) {
           return balanceOperationRepository.findAllByUser(user);
       }
       throw new NotClientException();
    }
}
