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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiantRepository.deleteAll();

        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
        etudiant.setAdresse("123 Main St");
        etudiant.setTelephone(1234567890L);

        etudiant = etudiantRepository.saveAndFlush(etudiant);
    }

    @AfterEach
    void tearDown() {
        etudiantRepository.deleteAll();
    }

    @Test
    void testGetAllEtudiants_shouldReturnListOfEtudiants_whenSuccessful() throws Exception {
        mockMvc.perform(get("/etudiants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].nom", hasItem("Doe")))
                .andExpect(jsonPath("$[*].prenom", hasItem("John")))
                .andExpect(jsonPath("$[*].telephone", hasItem(1234567890)));
    }

    @Test
    void testGetEtudiantById_shouldReturnEtudiant_whenEtudiantExists() throws Exception {
        mockMvc.perform(get("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Doe"))
                .andExpect(jsonPath("$.prenom").value("John"))
                .andExpect(jsonPath("$.adresse").value("123 Main St"))
                .andExpect(jsonPath("$.telephone").value(1234567890));
    }

    @Test
    void testGetEtudiantById_shouldReturnNotFound_whenEtudiantDoesNotExist() throws Exception {
        mockMvc.perform(get("/etudiants/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddEtudiant_shouldReturnCreated_whenEtudiantIsValid() throws Exception {
        Etudiant newEtudiant = new Etudiant();
        newEtudiant.setNom("Smith");
        newEtudiant.setPrenom("Jane");
        newEtudiant.setAdresse("456 Oak St");
        newEtudiant.setTelephone(3987654321L);  // Using the expected phone number

        String jsonContent = objectMapper.writeValueAsString(newEtudiant);

        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Smith"))
                .andExpect(jsonPath("$.prenom").value("Jane"))
                .andExpect(jsonPath("$.adresse").value("456 Oak St"))
                .andExpect(jsonPath("$.telephone").value(3987654321L));
    }

    @Test
    void testAddEtudiant_shouldReturnCreated_whenFieldsAreEmpty() throws Exception {
        Etudiant newEtudiant = new Etudiant();
        newEtudiant.setNom("");
        newEtudiant.setPrenom("John");
        newEtudiant.setAdresse("");
        newEtudiant.setTelephone(1234567890L);

        String jsonContent = objectMapper.writeValueAsString(newEtudiant);

        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.prenom").value("John"))
                .andExpect(jsonPath("$.telephone").value(1234567890L));
    }

    @Test
    void testUpdateEtudiant_shouldReturnUpdated_whenEtudiantExists() throws Exception {
        etudiant.setNom("UpdatedDoe");

        mockMvc.perform(put("/etudiants/{id}", etudiant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("UpdatedDoe"));
    }

    @Test
    void testDeleteEtudiant_shouldReturnNoContent_whenEtudiantExists() throws Exception {
        mockMvc.perform(delete("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/etudiants/{id}", etudiant.getId()))
                .andExpect(status().isNotFound());
    }
}