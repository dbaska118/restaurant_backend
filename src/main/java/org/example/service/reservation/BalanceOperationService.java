package org.example.service.reservation;

import org.example.dto.reservation.AddBalanceRequest;
import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.model.reservation.BalanceOperation;
import org.example.model.reservation.BalanceOperationType;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.reservation.BalanceOperationRepository;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public BalanceOperation addBalance(AddBalanceRequest addBalanceRequest) {
        User user = userRepository.findWithLockByEmail(addBalanceRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        if(user instanceof Client) {
            LocalDateTime now = LocalDateTime.now();
            double balanceBefore = ((Client) user).getBalance();
            BalanceOperation balanceOperation = new BalanceOperation(
                    user,
                    now,
                    addBalanceRequest.getAmount(),
                    balanceBefore,
                    balanceBefore + addBalanceRequest.getAmount(),
                    BalanceOperationType.ADD_FUNDS
            );
            ((Client) user).setBalance(balanceBefore + addBalanceRequest.getAmount());
            userRepository.save(user);
            return balanceOperationRepository.save(balanceOperation);
        }
        throw new NotClientException();
    }
}
