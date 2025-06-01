package com.example.pasir_kochanski_cezary.services;

import com.example.pasir_kochanski_cezary.dto.BalanceDTO;
import com.example.pasir_kochanski_cezary.model.Transaction;
import com.example.pasir_kochanski_cezary.dto.TransactionDTO;
import com.example.pasir_kochanski_cezary.model.TransactionType;
import com.example.pasir_kochanski_cezary.model.User;
import com.example.pasir_kochanski_cezary.repository.TransactionRepository;
import com.example.pasir_kochanski_cezary.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService
{
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllTransactions()
    {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id)
    {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znalezniono transakcji o ID " + id));
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono zalogowanego użytkownika"));
    }

    public BalanceDTO getUserBalance(User user, Float days) {
        List<Transaction> userTransactions = transactionRepository.findByUser((user));

        if(days!=null){
            LocalDateTime fromDate=LocalDateTime.now().minusSeconds(Math.round(days*86400));
            userTransactions=userTransactions.stream().filter(t->t.getTimestamp().isAfter(fromDate)).toList();
        }

        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();


        return new BalanceDTO(income, expense, income - expense);
    }


    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO)
    {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setTimestamp(transactionDTO.getTimestamp());

        return transactionRepository.save(transaction);
    }

    public Transaction createTransaction(TransactionDTO transactionDetails) {
        Transaction transaction = new Transaction(
                transactionDetails.getAmount(),
                transactionDetails.getType(),
                transactionDetails.getTags(),
                transactionDetails.getNotes(),
                getCurrentUser(),
                LocalDateTime.now()
        );

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }

        transactionRepository.delete(transaction);
    }

}
