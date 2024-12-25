package com.tp.GestionEtudiant.Services;

import com.tp.GestionEtudiant.Entities.Etudiant;

import java.util.List;

public interface EtudiantService {
    List<Etudiant> getAllEtudiants();
    Etudiant getEtudiantById(Long id);
    List<Etudiant> recupererParNomEtPrenom(String nom, String prenom);
    Etudiant add(Etudiant etudiant);
    Etudiant update(Long id, Etudiant etudiant);
    void delete(Long id);
}
