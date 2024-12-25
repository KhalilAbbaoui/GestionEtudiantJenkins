package com.tp.GestionEtudiant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.tp.GestionEtudiant.Entities")

public class GestionEtudiantApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEtudiantApplication.class, args);
	}

}
