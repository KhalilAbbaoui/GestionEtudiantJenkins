package com.tp.GestionEtudiant.Controllers;

import com.tp.GestionEtudiant.Entities.Etudiant;
import com.tp.GestionEtudiant.Services.EtudiantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtudiantController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EtudiantService etudiantService;

    @InjectMocks
    private EtudiantController etudiantController;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
    }

    @Test
    void testGetAllEtudiants() throws Exception {
        when(etudiantService.getAllEtudiants()).thenReturn(List.of(etudiant));

        mockMvc.perform(get("/etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Doe"));

        verify(etudiantService, times(1)).getAllEtudiants();
    }

    @Test
    void testGetEtudiantById() throws Exception {
        when(etudiantService.getEtudiantById(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/etudiants/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Doe"));

        verify(etudiantService, times(1)).getEtudiantById(1L);
    }

    @Test
    void testAddEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant(1L, "Doe", "John", "123 Main St", 1234567890L);
        doNothing().when(etudiantService).add(any(Etudiant.class));
        mockMvc.perform(post("/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Doe\", \"prenom\":\"John\", \"adresse\":\"123 Main St\", \"telephone\":\"1234567890\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Doe"));
        verify(etudiantService, times(1)).add(any(Etudiant.class));
    }

}
