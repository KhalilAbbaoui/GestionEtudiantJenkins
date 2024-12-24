package com.tp.GestionEtudiant.Repositories;

import com.tp.GestionEtudiant.Entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant,Long> {
    List<Etudiant> findByNomAndPrenom(String Nom, String Prenom);
}
