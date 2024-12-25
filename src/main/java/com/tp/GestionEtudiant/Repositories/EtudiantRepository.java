package com.tp.GestionEtudiant.Repositories;

import com.tp.GestionEtudiant.Entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant,Long> {
    List<Etudiant> findByNomAndPrenom(String Nom, String Prenom);
}
