package org.example.dto.balance;
import org.example.model.balance.BalanceOperation;
import org.springframework.stereotype.Component;

@Component
public class BalanceOperationMapper {

    public BalanceOperationDTO fromBalanceOperation(BalanceOperation balanceOperation) {
        return new BalanceOperationDTO(
                balanceOperation.getId(),
                balanceOperation.getUser().getEmail(),
                balanceOperation.getOperationDate(),
                balanceOperation.getAmount(),
                balanceOperation.getBalanceBefore(),
                balanceOperation.getBalanceAfter(),
                balanceOperation.getOperationType()
        );
    }
}
