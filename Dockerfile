# Étape 1 : Image Maven pour la compilation
FROM maven:3.8.5-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier uniquement les fichiers nécessaires
COPY pom.xml .
COPY src ./src

# Construire l'application sans exécuter les tests
RUN mvn clean package -DskipTests

# Étape 2 : Image finale basée sur OpenJDK
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer un autre port pour éviter le conflit avec Jenkins
EXPOSE 8081

# Commande pour exécuter l'application
CMD ["java", "-jar", "app.jar"]
