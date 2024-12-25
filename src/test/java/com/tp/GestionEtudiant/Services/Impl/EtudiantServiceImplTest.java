package com.tp.GestionEtudiant.Services.Impl;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
    }

    @Test
    void getAllEtudiants_returnsListOfEtudiants() {
        when(etudiantRepository.findAll()).thenReturn(List.of(etudiant));

        List<Etudiant> etudiants = etudiantService.getAllEtudiants();

        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void getEtudiantById_returnsEtudiant_whenExists() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.getEtudiantById(1L);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void getEtudiantById_returnsNull_whenNotExists() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        Etudiant result = etudiantService.getEtudiantById(1L);

        assertNull(result);
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void updateEtudiant_returnsUpdatedEtudiant_whenExists() {
        Etudiant updatedEtudiant = new Etudiant();
        updatedEtudiant.setId(1L);
        updatedEtudiant.setNom("Smith");

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(updatedEtudiant);

        Etudiant result = etudiantService.update(1L, updatedEtudiant);

        assertNotNull(result);
        assertEquals("Smith", result.getNom());
        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
    }

    @Test
    void deleteEtudiant_deletesEtudiant_whenExists() {
        when(etudiantRepository.existsById(1L)).thenReturn(true);

        etudiantService.delete(1L);

        verify(etudiantRepository, times(1)).deleteById(1L);
    }
}
