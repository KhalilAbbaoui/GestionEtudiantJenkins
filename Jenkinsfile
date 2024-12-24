pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'khalilabbaoui/gestionetudiant' // Nom de l'image Docker sur Docker Hub
        MYSQL_IMAGE = 'mysql:5.7' // Image officielle de MySQL version 5.7
        MYSQL_CONTAINER = 'mysql_test' // Nom du conteneur MySQL
        APP_PORT = '8081' // Port exposé pour accéder à l'application
        MYSQL_ROOT_PASSWORD = 'rootpassword' // Mot de passe administrateur pour MySQL
        MYSQL_DATABASE = 'gestionetudiant' // Nom de la base de données utilisée par l'application
    }

    stages {
        // Étape pour récupérer le code source depuis le SCM
        stage('Checkout') {
            steps {
            // Cette commande utilise la configuration par défaut de Jenkins pour cloner le code source
           // à partir du SCM (Source Code Management) configuré dans le job Jenkins.
                checkout scm
            }
        }

        // Étape pour compiler le projet et exécuter les tests Maven
        stage('Build & Test') {
            steps {
                script {
                    echo 'Building the project with Maven and running tests...' // Log pour indiquer la progression
                    sh 'mvn clean install -DskipTests=false' // Nettoyer, compiler et exécuter les tests
                }
            }
        }

        // Étape pour démarrer le conteneur MySQL
        stage('Start MySQL') {
            steps {
                script {
                    echo 'Starting MySQL container...' // Log pour informer sur le démarrage de MySQL
                    sh """
                    docker run --name ${MYSQL_CONTAINER} -d \
                        -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                        -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                        -p 3306:3306 \ // Expose MySQL sur le port 3306
                        ${MYSQL_IMAGE} // Utilisation de l'image MySQL officielle
                    """
                }
            }
        }

        // Étape pour construire l'image Docker de l'application
        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for the application...' // Log pour la construction de l'image
                    sh "docker build -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        // Étape pour pousser l'image Docker sur Docker Hub
        stage('Docker Push') {
            steps {
                script {
                    echo 'Pushing the Docker image to Docker Hub...' // Log pour indiquer le push
                    sh "echo ${env.DOCKER_HUB_PASSWORD} | docker login -u ${env.DOCKER_HUB_USERNAME} --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        // Étape pour démarrer le conteneur de l'application
        stage('Run Application Container') {
            steps {
                script {
                    echo 'Starting the application container...' // Log pour informer sur le démarrage de l'application
                    sh """
                    docker stop gestionetudiant || true && docker rm gestionetudiant || true // Arrêter et supprimer les conteneurs existants
                    docker run -d -p ${APP_PORT}:8081 \
                        --name gestionetudiant \
                        ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
    }

    post {
        // Message de succès
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        // Message d'échec
        failure {
            echo 'Une erreur est survenue pendant l’exécution du pipeline.'
        }
    }
}
