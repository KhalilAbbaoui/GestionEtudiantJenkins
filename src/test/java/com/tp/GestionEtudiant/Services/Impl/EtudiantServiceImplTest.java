package com.tp.GestionEtudiant.Services.Impl;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Créer l'objet Etudiant avec le nom "John Doe"
        etudiant = new Etudiant("John", "Doe");

        // Mocker les méthodes du repository uniquement si nécessaire
        lenient().when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);
        lenient().when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant)); // Simuler le findById pour ID 1
        lenient().when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant)); // Simuler le findAll()
        lenient().when(etudiantRepository.existsById(1L)).thenReturn(true); // Simuler le existsById pour ID 1
        lenient().doNothing().when(etudiantRepository).deleteById(1L); // Simuler la suppression d'un étudiant par ID
        lenient().when(etudiantRepository.findByNomAndPrenom("John", "Doe")).thenReturn(Arrays.asList(etudiant)); // Simuler la recherche par nom et prénom
    }

    @Test
    void getAllEtudiants_returnsListOfEtudiants() {
        // Tester si la méthode retourne correctement la liste des étudiants
        var result = etudiantService.getAllEtudiants();
        assertFalse(result.isEmpty()); // Vérifier que la liste n'est pas vide
        assertEquals(1, result.size()); // Vérifier que la liste contient un étudiant
        assertEquals("John", result.get(0).getNom()); // Vérifier que le nom est "John"
    }

    @Test
    void getEtudiantById_returnsEtudiant_whenExists() {
        // Tester si l'étudiant correct est retourné lorsqu'il existe
        Etudiant result = etudiantService.getEtudiantById(1L);
        assertEquals("John", result.getNom()); // Vérifier que le prénom est "John"
        assertEquals("Doe", result.getPrenom()); // Vérifier que le nom est "Doe"
    }

    @Test
    void getEtudiantById_returnsNull_whenNotExists() {
        // Tester si null est retourné lorsque l'étudiant n'existe pas
        lenient().when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());
        Etudiant result = etudiantService.getEtudiantById(999L);
        assertNull(result); // Vérifier que le résultat est null
    }

    @Test
    void recupererParNomEtPrenom_returnsEtudiants_whenExists() {
        // Tester si la méthode retourne l'étudiant correct en fonction du nom et prénom
        var result = etudiantService.recupererParNomEtPrenom("John", "Doe");
        assertFalse(result.isEmpty()); // Vérifier que la liste n'est pas vide
        assertEquals("John", result.get(0).getNom()); // Vérifier que le nom est "John"
        assertEquals("Doe", result.get(0).getPrenom()); // Vérifier que le prénom est "Doe"
    }

    @Test
    void saveEtudiant_savesEtudiant() {
        // Create an Etudiant object
        Etudiant etudiant = new Etudiant("John", "Doe");

        // Mock the repository to return the same Etudiant when save is called
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Call the add method from the service
        Etudiant savedEtudiant = etudiantService.add(etudiant);

        // Assert that the returned Etudiant is not null
        assertNotNull(savedEtudiant);

        // Assert that the name is "John"
        assertEquals("John", savedEtudiant.getNom());

        // Optionally, check the last name as well (this is just an extra validation)
        assertEquals("Doe", savedEtudiant.getPrenom());
    }

    @Test
    void updateEtudiant_updatesEtudiant_whenExists() {
        // Create an updated Etudiant object with new values
        Etudiant updatedEtudiant = new Etudiant("John", "Doe Updated");
        updatedEtudiant.setAdresse("New Address");
        updatedEtudiant.setTelephone(1234567890L);

        // Mock the repository to return an Etudiant when findById is called
        lenient().when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        // Call the update method from the service
        Etudiant result = etudiantService.update(1L, updatedEtudiant);

        // Assert the result is not null
        assertNotNull(result);

        // Validate the updated values
        assertEquals("John", result.getNom()); // Check if the name is "John"
        assertEquals("Doe Updated", result.getPrenom()); // Check if the last name is updated
        assertEquals("New Address", result.getAdresse()); // Check if the address is updated
        assertEquals(1234567890L, result.getTelephone()); // Check if the phone number is updated
    }

    @Test
    void updateEtudiant_returnsNull_whenNotExists() {
        // Tester si la mise à jour retourne null lorsque l'étudiant n'existe pas
        Etudiant updatedEtudiant = new Etudiant("John", "Doe Updated");
        lenient().when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());

        Etudiant result = etudiantService.update(999L, updatedEtudiant);

        assertNull(result); // Vérifier que le résultat est null
    }

    @Test
    void deleteEtudiant_deletesEtudiant_whenExists() {
        // Tester la suppression de l'étudiant
        etudiantService.delete(1L);
        verify(etudiantRepository, times(1)).deleteById(1L); // Vérifier que deleteById a bien été appelé une fois
    }

    @Test
    void deleteEtudiant_doesNotDelete_whenNotExists() {
        // Tester si rien ne se passe lorsque l'étudiant n'existe pas
        lenient().when(etudiantRepository.existsById(999L)).thenReturn(false);
        etudiantService.delete(999L);
        verify(etudiantRepository, times(0)).deleteById(999L); // Vérifier que deleteById n'a pas été appelé
    }
}
