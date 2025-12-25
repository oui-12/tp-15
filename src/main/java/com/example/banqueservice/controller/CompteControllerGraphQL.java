package com.example.banqueservice.controller;
import com.example.banqueservice.dto.CompteInput;
import com.example.banqueservice.dto.CompteRequest;
import com.example.banqueservice.model.Compte;
import com.example.banqueservice.repository.CompteRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {
    private final CompteRepository compteRepository;
    
    @QueryMapping
    public List<Compte> allComptes(){
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument Long id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Compte %s not found", id)));
    }

    @MutationMapping
    public Compte saveCompte(@Argument("compte") CompteInput compteInput) {
        Compte compte = new Compte();
        compte.setSolde(compteInput.getSolde());
        // Conversion de String en Date si nécessaire
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // compte.setDateCreation(sdf.parse(compteInput.getDateCreation()));
        compte.setDateCreation(compteInput.getDateCreation());
        compte.setType(compteInput.getType());
        return compteRepository.save(compte);
    }

    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = compteRepository.count(); 
        double sum = compteRepository.sumSoldes();
        double average = count > 0 ? sum / count : 0;
        
        return Map.of(
            "count", count,
            "sum", sum,
            "average", average
        );
    }
}
