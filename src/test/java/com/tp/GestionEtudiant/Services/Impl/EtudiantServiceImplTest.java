package com.tp.GestionEtudiant.Services.Impl;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EtudiantServiceImplTest {
    @Mock
    private EtudiantRepository etudiantRepository;
    @InjectMocks
    private EtudiantServiceImpl etudiantService;
    private Etudiant etudiant;

    @BeforeEach
    void setUp()
    {
        etudiant=new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
    }
    @Test
    void testGetAllEtudiants()
    {
        when(etudiantRepository.findAll()).thenReturn(List.of(etudiant));

        List<Etudiant> etudiants = etudiantService.getAllEtudiants();

        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testGetEtudiantById() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.getEtudiantById(1L);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        verify(etudiantRepository, times(1)).findById(1L);
    }
    @Test
    void testUpdateEtudiant() {
        Etudiant updatedEtudiant = new Etudiant();
        updatedEtudiant.setId(1L);
        updatedEtudiant.setNom("Smith");
        updatedEtudiant.setPrenom("John");

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(updatedEtudiant);

        Etudiant result = etudiantService.update(1L, updatedEtudiant);

        assertNotNull(result);
        assertEquals("Smith", result.getNom());
        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
    }
    @Test
    void testDeleteEtudiant() {
        when(etudiantRepository.existsById(1L)).thenReturn(true);

        etudiantService.delete(1L);

        verify(etudiantRepository, times(1)).deleteById(1L);
    }
}

