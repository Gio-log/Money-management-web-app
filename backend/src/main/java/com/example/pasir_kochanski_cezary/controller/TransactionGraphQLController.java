package com.example.pasir_kochanski_cezary.controller;

import com.example.pasir_kochanski_cezary.dto.BalanceDTO;
import com.example.pasir_kochanski_cezary.dto.TransactionDTO;
import com.example.pasir_kochanski_cezary.model.Transaction;
import com.example.pasir_kochanski_cezary.model.User;
import com.example.pasir_kochanski_cezary.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public Transaction addTransaction(@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(@Valid @Argument Long id,@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @MutationMapping
    public Boolean deleteTransaction(@Valid @Argument Long id) {
        transactionService.deleteTransaction(id);
        return true;
    }
    @QueryMapping
    public BalanceDTO userBalance(@Argument Float days){
        User user = transactionService.getCurrentUser();
        return transactionService.getUserBalance(user,days);
    }
}
