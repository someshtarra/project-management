pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        GITHUB_REPO_NAME = 'GITOPS'
        GITHUB_USER_NAME = 'someshtarra'
        IMAGE_NAME       = 'someshtarra/project'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Cloning code from GitHub repo'
                git branch: 'main',
                    url: 'https://github.com/someshtarra/project-management.git'
            }
        }

        stage('Build and Unit Test') {
            steps {
                echo 'Compiling Java code, running unit tests, and generating JaCoCo coverage report'
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Scan') {
            steps {
                echo 'Scanning project with SonarQube'
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('SonarQube Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate result...'
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate abortPipeline: true
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image ${IMAGE_NAME}:${BUILD_NUMBER}"
                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} -f Dockerfile ."
            }
        }

        stage('Scan Docker Image Using Trivy') {
            steps {
                echo "Scanning Docker image ${IMAGE_NAME}:${BUILD_NUMBER} using Trivy"
                sh "trivy image --severity HIGH,CRITICAL --ignore-unfixed --exit-code 1 ${IMAGE_NAME}:${BUILD_NUMBER}"
            }
        }

        stage('Docker Image Push') {
            steps {
                echo 'Logging into Docker Hub and pushing image'
                withCredentials([
                    string(
                        credentialsId: 'dockerhub',
                        variable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login -u someshtarra --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    '''
                }
            }
        }

        stage('Deployment Checkout') {
            steps {
                echo 'Cloning deployment files from GitOps repo'
                dir('gitops') {
                    git branch: 'main',
                        url: 'https://github.com/someshtarra/GITOPS.git'
                }
            }
        }

        stage('Update deploy.yaml') {
            steps {
                echo 'Updating deployment file'
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

                            sed -i "s|image:.*|image: ${IMAGE_NAME}:${BUILD_NUMBER}|g" Deployment/deploy.yaml || true

                            git add .
                            git commit -m "Updated build number ${BUILD_NUMBER}" || true
                            git push https://${GITHUB_TOKEN}@github.com/${GITHUB_USER_NAME}/${GITHUB_REPO_NAME}.git HEAD:main
                        '''
                    }
                }
            }
        }

    }
}
