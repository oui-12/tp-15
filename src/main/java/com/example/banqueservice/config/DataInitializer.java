package com.example.banqueservice.config;

import com.example.banqueservice.model.Compte;
import com.example.banqueservice.model.TypeCompte;
import com.example.banqueservice.repository.CompteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Date;

@Configuration
@Profile("!test")
public class DataInitializer {

    private final CompteRepository compteRepository;

    public DataInitializer(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @PostConstruct
    public void init() {
        if (compteRepository.count() == 0) {
            Compte compte1 = new Compte();
            compte1.setSolde(1000.0);
            compte1.setDateCreation(new Date());
            compte1.setType(TypeCompte.COURANT);

            Compte compte2 = new Compte();
            compte2.setSolde(5000.0);
            compte2.setDateCreation(new Date());
            compte2.setType(TypeCompte.EPARGNE);

            Compte compte3 = new Compte();
            compte3.setSolde(2500.0);
            compte3.setDateCreation(new Date());
            compte3.setType(TypeCompte.COURANT);

            compteRepository.saveAll(Arrays.asList(compte1, compte2, compte3));
            System.out.println("✅ Données initiales chargées avec succès !");
        }
    }
}