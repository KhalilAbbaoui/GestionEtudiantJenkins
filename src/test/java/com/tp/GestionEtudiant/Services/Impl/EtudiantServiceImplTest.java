package com.tp.GestionEtudiant.Services.Impl;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc // Configure MockMvc for HTTP tests
@SpringBootTest
class EtudiantServiceImplTest {

    @Autowired
    private EtudiantRepository etudiantRepository; // Inject real repository

    @Autowired
    private EtudiantServiceImpl etudiantService; // Inject real service

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");

        // Save an Etudiant in the database before each test
        etudiantRepository.save(etudiant);
    }

    @Test
    void getAllEtudiants_returnsListOfEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();

        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
    }

    @Test
    void getEtudiantById_returnsEtudiant_whenExists() {
        Etudiant result = etudiantService.getEtudiantById(etudiant.getId());

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
    }

    @Test
    void getEtudiantById_returnsNull_whenNotExists() {
        Etudiant result = etudiantService.getEtudiantById(999L);

        assertNull(result);
    }

    @Test
    void updateEtudiant_returnsUpdatedEtudiant_whenExists() {
        Etudiant updatedEtudiant = new Etudiant();
        updatedEtudiant.setId(etudiant.getId());
        updatedEtudiant.setNom("Smith");

        Etudiant result = etudiantService.update(etudiant.getId(), updatedEtudiant);

        assertNotNull(result);
        assertEquals("Smith", result.getNom());
    }

    @Test
    void deleteEtudiant_deletesEtudiant_whenExists() {
        etudiantService.delete(etudiant.getId());

        assertFalse(etudiantRepository.existsById(etudiant.getId()));
    }
}
