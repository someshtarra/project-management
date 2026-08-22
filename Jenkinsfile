pipeline {
    agent any

    tools {
        maven 'maven_3.1'
    }

    environment {
        GITHUB_USER_NAME = 'someshtarra'
        GITHUB_REPO_NAME = 'GITOPS'
        IMAGE_NAME       = 'someshtarra/project'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Cloning code from GitHub repo'
                git branch: 'main', url: 'https://github.com/someshtarra/project-management.git'
            }
        }

        stage('Build and Unit Test') {
            steps {
                echo 'Building Maven project and executing unit tests with JaCoCo coverage'
                sh 'mvn clean test'
            }
        }

        stage('SonarQube Scan') {
            steps {
                echo 'Scanning Maven project with SonarQube'
                withCredentials([
                    string(
                        credentialsId: 'sonarqube-token',
                        variable: 'SONAR_TOKEN'
                    )
                ]) {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                        -Dsonar.host.url=http://ec2-34-204-169-100.compute-1.amazonaws.com:9000 \
                        -Dsonar.token=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate result'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Artifact') {
            steps {
                echo 'Building executable Maven artifact'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image'
                sh '''
                    docker build \
                    -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                    -f Dockerfile .
                '''
            }
        }

        stage('Scan Docker Image Using Trivy') {
            steps {
                echo 'Scanning Docker image using Trivy'
                sh '''
                    trivy image ${IMAGE_NAME}:${BUILD_NUMBER} || true
                '''
            }
        }

        stage('Docker Image Push') {
            steps {
                echo 'Logging into Docker Hub'
                withCredentials([
                    string(
                        credentialsId: 'dockerhub',
                        variable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login -u someshtarra --password-stdin
                        echo "Pushing image to Docker Hub"
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    '''
                }
            }
        }

        stage('Deployment Checkout') {
            steps {
                echo 'Cloning GITOPS deployment repository'
                dir('gitops') {
                    git branch: 'main', url: 'https://github.com/someshtarra/GITOPS.git'
                }
            }
        }

        stage('Update GitOps YAML') {
            steps {
                echo 'Updating deployment image tag in GITOPS repository'
                dir('gitops') {
                    withCredentials([
                        string(
                            credentialsId: 'githubtoken',
                            variable: 'GITHUB_TOKEN'
                        )
                    ]) {
                        sh '''
                            git config user.name "someshtarra"
                            git config user.email "someshtarra@gmail.com"

                            sed -i "s|image:.*|image: ${IMAGE_NAME}:${BUILD_NUMBER}|g" deployment.yaml || true

                            git add .
                            git commit -m "Update image tag to ${BUILD_NUMBER}" || true
                            git push https://${GITHUB_TOKEN}@github.com/${GITHUB_USER_NAME}/${GITHUB_REPO_NAME}.git HEAD:main
                        '''
                    }
                }
            }
        }
    }
}
