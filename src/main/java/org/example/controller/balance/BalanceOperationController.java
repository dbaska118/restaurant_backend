package org.example.controller.balance;

import org.example.dto.balance.AddBalanceRequest;
import org.example.dto.balance.BalanceOperationDTO;
import org.example.dto.balance.BalanceOperationMapper;
import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.model.balance.BalanceOperation;
import org.example.service.balance.BalanceOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/balanceOperation")
public class BalanceOperationController {

    private final BalanceOperationMapper balanceOperationMapper;
    private final BalanceOperationService balanceOperationService;

    @Autowired
    public BalanceOperationController(BalanceOperationMapper balanceOperationMapper, BalanceOperationService balanceOperationService) {
        this.balanceOperationMapper = balanceOperationMapper;
        this.balanceOperationService = balanceOperationService;
    }

    @PostMapping("/addFunds")
    public ResponseEntity<BalanceOperationDTO> addFunds(@RequestBody AddBalanceRequest request, Principal principal) {
        String email = principal.getName();
        if(!email.equals(request.getEmail())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            BalanceOperation balanceOperation = balanceOperationService.addBalance(request);
            return new ResponseEntity<>(balanceOperationMapper.fromBalanceOperation(balanceOperation), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NotClientException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/employee/addFunds")
    public ResponseEntity<BalanceOperationDTO> addFundsEmployee(@RequestBody AddBalanceRequest request) {
        try {
            BalanceOperation balanceOperation = balanceOperationService.addBalance(request);
            return new ResponseEntity<>(balanceOperationMapper.fromBalanceOperation(balanceOperation), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NotClientException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<BalanceOperationDTO>> getAllBalanceOperations(@PathVariable String email, Principal principal) {
        String emailToken = principal.getName();
        if(!email.equals(emailToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            List<BalanceOperation> list = balanceOperationService.geAllOperationsByEmail(email);
            List<BalanceOperationDTO> result = new ArrayList<>();
            for (BalanceOperation balanceOperation : list) {
                result.add(balanceOperationMapper.fromBalanceOperation(balanceOperation));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NotClientException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
