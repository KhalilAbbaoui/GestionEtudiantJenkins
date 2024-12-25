package com.tp.GestionEtudiant.Services.Impl;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import com.tp.GestionEtudiant.Services.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Override
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }
    @Override
    public Etudiant getEtudiantById(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }
    @Override
    public List<Etudiant> recupererParNomEtPrenom(String nom, String prenom) {
        return etudiantRepository.findByNomAndPrenom(nom, prenom);
    }
    @Override
    public Etudiant add(Etudiant etudiant) {
        etudiantRepository.save(etudiant);
        return etudiant;
    }
    public Etudiant update(Long id, Etudiant etudiant) {
        Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
        if (existingEtudiant.isPresent()) {
            Etudiant updatedEtudiant = existingEtudiant.get();
            updatedEtudiant.setNom(etudiant.getNom());
            updatedEtudiant.setPrenom(etudiant.getPrenom());
            updatedEtudiant.setAdresse(etudiant.getAdresse());
            updatedEtudiant.setTelephone(etudiant.getTelephone());
            return etudiantRepository.save(updatedEtudiant);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        if (etudiantRepository.existsById(id)) {
            etudiantRepository.deleteById(id);
        }
    }
}