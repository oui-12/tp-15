package com.example.banqueservice.controller;

import com.example.banqueservice.dto.TransactionRequest;
import com.example.banqueservice.model.Compte;
import com.example.banqueservice.model.Transaction;
import com.example.banqueservice.model.TypeTransaction;
import com.example.banqueservice.repository.CompteRepository;
import com.example.banqueservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TransactionControllerGraphQL {
    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    @MutationMapping
    public Transaction addTransaction(@Argument("transaction") TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        
        // Mise à jour du solde du compte
        if (transactionRequest.getType() == TypeTransaction.DEPOT) {
            compte.setSolde(compte.getSolde() + transactionRequest.getMontant());
        } else if (transactionRequest.getType() == TypeTransaction.RETRAIT) {
            if (compte.getSolde() < transactionRequest.getMontant()) {
                throw new RuntimeException("Solde insuffisant");
            }
            compte.setSolde(compte.getSolde() - transactionRequest.getMontant());
        }
        
        // Création de la transaction
        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(transactionRequest.getDate() != null ? transactionRequest.getDate() : new Date());
        transaction.setType(transactionRequest.getType());
        transaction.setCompte(compte);
        
        // Sauvegarde du compte et de la transaction
        compteRepository.save(compte);
        return transactionRepository.save(transaction);
    }

    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(TypeTransaction.DEPOT);
        double sumRetraits = transactionRepository.sumByType(TypeTransaction.RETRAIT);
        
        return Map.of(
            "count", count,
            "sumDepots", sumDepots,
            "sumRetraits", sumRetraits
        );
    }
}
