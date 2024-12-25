package com.tp.GestionEtudiant.Controllers;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import com.tp.GestionEtudiant.Services.EtudiantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc; // Utilisation de MockMvc pour simuler les requêtes HTTP

    @Autowired
    private EtudiantService etudiantService; // Injecte le service réel

    @Autowired
    private EtudiantRepository etudiantRepository; // Injecte le repository réel

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Initialisation des données avant chaque test
        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
        // Sauvegarder un étudiant dans la base de données avant chaque test
        etudiantRepository.save(etudiant);
    }

    @Test
    void testGetAllEtudiants_shouldReturnListOfEtudiants_whenSuccessful() throws Exception {
        mockMvc.perform(get("/etudiants"))
                .andExpect(status().isOk()) // Vérifie que la réponse est 200 OK
                .andExpect(jsonPath("$[0].nom").value("Doe")); // Vérifie que le nom du premier étudiant est "Doe"
    }

    @Test
    void testGetEtudiantById_shouldReturnEtudiant_whenEtudiantExists() throws Exception {
        mockMvc.perform(get("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isOk()) // Vérifie que la réponse est 200 OK
                .andExpect(jsonPath("$.nom").value("Doe")); // Vérifie que le nom de l'étudiant est "Doe"
    }

    @Test
    void testGetEtudiantById_shouldReturnNotFound_whenEtudiantDoesNotExist() throws Exception {
        mockMvc.perform(get("/etudiants/{id}", 999L)) // L'ID 999 n'existe pas
                .andExpect(status().isNotFound()); // Vérifie que la réponse est 404 Not Found
    }

    @Test
    void testAddEtudiant_shouldReturnCreated_whenEtudiantIsValid() throws Exception {
        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Doe\", \"prenom\":\"John\", \"adresse\":\"123 Main St\", \"telephone\":\"1234567890\"}"))
                .andExpect(status().isCreated()) // Vérifie que la réponse est 201 Created
                .andExpect(jsonPath("$.nom").value("Doe")); // Vérifie que le nom de l'étudiant est "Doe"
    }

    @Test
    void testAddEtudiant_shouldReturnBadRequest_whenValidationFails() throws Exception {
        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"\", \"prenom\":\"John\", \"adresse\":\"\", \"telephone\":\"1234567890\"}"))
                .andExpect(status().isBadRequest()); // Vérifie que la réponse est 400 Bad Request
    }
}
