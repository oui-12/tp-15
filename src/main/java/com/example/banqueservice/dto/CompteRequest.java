package com.example.banqueservice.dto;

import com.example.banqueservice.model.TypeCompte;
import lombok.Data;

import java.util.Date;

@Data
public class CompteRequest {
    private double solde;
    private Date dateCreation;
    private TypeCompte type;
}