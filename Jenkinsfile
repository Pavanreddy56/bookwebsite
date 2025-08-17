pipeline {
    agent any

    tools {
        maven 'Maven3'    // Maven configured in Jenkins
        jdk 'JDK17'       // JDK configured in Jenkins
    }

    environment {
        IMAGE_NAME = "pavanreddych/book-website"   // DockerHub repo
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
        SONARQUBE_SERVER = "SonarQube"  // Jenkins SonarQube server name
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: 'main', url: 'https://github.com/Pavanreddy56/bookwebsite.git'
            }
        }

        stage('Build with Maven') {
            steps {
                dir('backend') {
                    bat 'mvn clean package -DskipTests'
                    bat 'dir target'
                }
                dir('frontend') {
                    bat 'mvn clean package -DskipTests'
                    bat 'dir target'
                }
            }
        }

        stage('SonarQube Analysis') {
            environment {
                PATH = "${tool 'Maven3'}/bin;${env.PATH}"
            }
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    dir('backend') {
                        bat """
                           mvn sonar:sonar ^
                              -Dsonar.projectKey=book-backend ^
                              -Dsonar.host.url=%SONAR_HOST_URL% ^
                              -Dsonar.login=%SONAR_AUTH_TOKEN%
                        """
                    }
                    dir('frontend') {
                        bat """
                           mvn sonar:sonar ^
                              -Dsonar.projectKey=book-frontend ^
                              -Dsonar.host.url=%SONAR_HOST_URL% ^
                              -Dsonar.login=%SONAR_AUTH_TOKEN%
                        """
                    }
                }
            }
        }

        stage('Prepare Docker Context') {
            steps {
                dir('backend') {
                    bat 'copy target\\*.jar ..\\'   // copy backend jar to root for Docker build
                }
                bat 'dir'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                  usernameVariable: 'DOCKERHUB_USER',
                                                  passwordVariable: 'DOCKERHUB_PASS')]) {
                    bat "echo %DOCKERHUB_PASS% | docker login -u %DOCKERHUB_USER% --password-stdin"
                    bat "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    bat "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
                    bat "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploying App to Kubernetes') {
            steps {
                withEnv(["KUBECONFIG=C:/Users/cheed/.kube/config"]) {
                    bat 'kubectl apply -f k8s/mysql-configmap.yaml || exit 0'
                    bat 'kubectl apply -f k8s/mysql-deployment.yaml'
                    bat 'kubectl apply -f k8s/backend-deployment.yaml'
                    bat 'kubectl apply -f k8s/frontend-deployment.yaml'
                    bat 'kubectl apply -f k8s/ingress.yaml'
                }
            }
        }

    }

    post {
        success {
            echo "✅ Deployment successful: ${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo "❌ Build, analysis, or deployment failed"
        }
    }
}

