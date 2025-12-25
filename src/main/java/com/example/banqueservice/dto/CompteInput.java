package com.example.banqueservice.dto;

import com.example.banqueservice.model.TypeCompte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class CompteInput {
    private Long id;
    private double solde;
    private Date dateCreation;
    private TypeCompte type;
}
