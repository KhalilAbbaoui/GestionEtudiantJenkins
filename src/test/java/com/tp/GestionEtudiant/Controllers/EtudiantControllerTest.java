package com.tp.GestionEtudiant.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Repositories.EtudiantRepository;
import com.tp.GestionEtudiant.Services.EtudiantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Lancer l'application avec un port aléatoire pour les tests d'intégration.
@AutoConfigureMockMvc // Configuration de MockMvc pour effectuer des requêtes HTTP sans démarrer un serveur HTTP complet.
@Transactional // Assure que les tests sont effectués dans une transaction et que toutes les modifications de la base de données sont annulées après chaque test.
public class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permet d'effectuer des requêtes HTTP simulées pour tester les contrôleurs.

    @Autowired
    private EtudiantService etudiantService; // Service utilisé pour gérer la logique métier, bien qu'il ne soit pas utilisé ici directement.

    @Autowired
    private EtudiantRepository etudiantRepository; // Repository pour interagir avec la base de données.

    @Autowired
    private ObjectMapper objectMapper; // Permet de convertir des objets Java en JSON et vice versa.

    private Etudiant etudiant; // Variable pour stocker un étudiant avant les tests.

    @BeforeEach
    void setUp() {
        // Efface tous les étudiants avant chaque test pour garantir un environnement propre.
        etudiantRepository.deleteAll();

        // Crée un étudiant à tester
        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
        etudiant.setAdresse("123 Main St");
        etudiant.setTelephone(1234567890L);

        // Sauvegarde l'étudiant dans la base de données pour que les tests suivants puissent l'utiliser
        etudiant = etudiantRepository.saveAndFlush(etudiant);
    }

    @AfterEach
    void tearDown() {
        // Efface tous les étudiants après chaque test
        etudiantRepository.deleteAll();
    }

    @Test
    void testGetAllEtudiants_shouldReturnListOfEtudiants_whenSuccessful() throws Exception {
        // Effectue une requête GET pour récupérer la liste des étudiants
        mockMvc.perform(get("/etudiants"))
                .andExpect(status().isOk()) // Vérifie que la réponse a un statut HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que le type de contenu est JSON
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1)))) // Vérifie qu'il y a au moins un étudiant dans la réponse
                .andExpect(jsonPath("$[*].nom", hasItem("Doe"))) // Vérifie que le nom "Doe" est présent dans la réponse
                .andExpect(jsonPath("$[*].prenom", hasItem("John"))) // Vérifie que le prénom "John" est présent
                .andExpect(jsonPath("$[*].telephone", hasItem(1234567890))); // Vérifie que le numéro de téléphone est correct
    }

    @Test
    void testGetEtudiantById_shouldReturnEtudiant_whenEtudiantExists() throws Exception {
        // Effectue une requête GET pour récupérer l'étudiant par ID
        mockMvc.perform(get("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que le contenu est au format JSON
                .andExpect(jsonPath("$.nom").value("Doe")) // Vérifie que le nom est bien "Doe"
                .andExpect(jsonPath("$.prenom").value("John")) // Vérifie que le prénom est bien "John"
                .andExpect(jsonPath("$.adresse").value("123 Main St")) // Vérifie que l'adresse est correcte
                .andExpect(jsonPath("$.telephone").value(1234567890)); // Vérifie que le téléphone est correct
    }

    @Test
    void testGetEtudiantById_shouldReturnNotFound_whenEtudiantDoesNotExist() throws Exception {
        // Effectue une requête GET pour récupérer un étudiant qui n'existe pas dans la base de données
        mockMvc.perform(get("/etudiants/{id}", 999L)) // ID non existant
                .andExpect(status().isNotFound()); // Vérifie que la réponse retourne un statut 404 (Non trouvé)
    }

    @Test
    void testAddEtudiant_shouldReturnCreated_whenEtudiantIsValid() throws Exception {
        // Crée un nouvel étudiant pour tester l'ajout
        Etudiant newEtudiant = new Etudiant();
        newEtudiant.setNom("Smith");
        newEtudiant.setPrenom("Jane");
        newEtudiant.setAdresse("456 Oak St");
        newEtudiant.setTelephone(3987654321L);

        // Convertit l'objet en JSON
        String jsonContent = objectMapper.writeValueAsString(newEtudiant);

        // Effectue une requête POST pour ajouter un nouvel étudiant
        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated()) // Vérifie que l'étudiant a été créé (statut HTTP 201)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que la réponse est en JSON
                .andExpect(jsonPath("$.nom").value("Smith")) // Vérifie le nom de l'étudiant ajouté
                .andExpect(jsonPath("$.prenom").value("Jane")) // Vérifie le prénom de l'étudiant ajouté
                .andExpect(jsonPath("$.adresse").value("456 Oak St")) // Vérifie l'adresse
                .andExpect(jsonPath("$.telephone").value(3987654321L)); // Vérifie le téléphone
    }

    @Test
    void testAddEtudiant_shouldReturnCreated_whenFieldsAreEmpty() throws Exception {
        // Crée un nouvel étudiant avec des champs vides
        Etudiant newEtudiant = new Etudiant();
        newEtudiant.setNom("");
        newEtudiant.setPrenom("John");
        newEtudiant.setAdresse("");
        newEtudiant.setTelephone(1234567890L);

        // Convertit l'objet en JSON
        String jsonContent = objectMapper.writeValueAsString(newEtudiant);

        // Effectue une requête POST pour ajouter un étudiant avec des champs vides
        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated()) // Vérifie que la réponse est bien un statut 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie le type de contenu
                .andExpect(jsonPath("$.prenom").value("John")) // Vérifie que le prénom est correct
                .andExpect(jsonPath("$.telephone").value(1234567890L)); // Vérifie que le téléphone est correct
    }

    @Test
    void testUpdateEtudiant_shouldReturnUpdated_whenEtudiantExists() throws Exception {
        // Modifie l'étudiant existant
        etudiant.setNom("UpdatedDoe");

        // Effectue une requête PUT pour mettre à jour l'étudiant
        mockMvc.perform(put("/etudiants/{id}", etudiant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que la réponse est en JSON
                .andExpect(jsonPath("$.nom").value("UpdatedDoe")); // Vérifie que le nom a été mis à jour
    }

    @Test
    void testDeleteEtudiant_shouldReturnNoContent_whenEtudiantExists() throws Exception {
        // Effectue une requête DELETE pour supprimer l'étudiant
        mockMvc.perform(delete("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isNoContent()); // Vérifie que la réponse a un statut HTTP 204 No Content

        // Vérifie que l'étudiant a bien été supprimé
        mockMvc.perform(get("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isNotFound()); // Vérifie que l'étudiant n'est plus trouvé, statut 404
    }
}