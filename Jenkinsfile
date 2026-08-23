pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        GITHUB_REPO_URL = 'https://github.com/someshtarra/project-management.git'
        GITHUB_REPO_NAME = 'project-management'
        GITHUB_USER_NAME = 'someshtarra'
        GITHUB_BRANCH = 'main'
        USER = 'somesh'
        USER_MAIL = 'someshtarra@gmail.com'
        GITOPS_REPO_URL = 'https://github.com/someshtarra/GITOPS.git'
        GITOPS_REPO_NAME = 'GITOPS'
        GITOPS_BRANCH = 'main'
        DOCKER_IMAGE = 'someshtarra/projectimage'
        DOCKER_USER = 'someshtarra'
    }

    stages {
        stage('Checkout Stage'){
            steps{
                echo 'cloning the code from github'
                git branch: env.GITHUB_BRANCH,
                  url: env.GITHUB_REPO_URL
            }
        }

        stage('Maven test'){
            steps{
                echo 'Using maven validate, compiler and unit test the code'
                sh 'mvn clean test'
            }
        }

        stage('SonarQube'){
            steps{
                echo 'Code Scan Using SonarQube'
                withSonarQubeEnv('SonarQube'){
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                }
            }
        }

        stage('Quality Gate'){
            steps{
                echo 'Checking the Quality Gate'
                timeout(time: 5 ,unit: 'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Maven package'){
            steps{
                echo 'Using Maven package to create artifact'
                sh 'mvn clean package'
            }
        }

        stage('Docker build'){
            steps{
                echo 'To create Docker Image'
                sh 'docker build -t $DOCKER_IMAGE:${BUILD_NUMBER} -f Dockerfile .'
            }
        }

        stage('Trivy scan'){
            steps{
                echo 'Image Scan Using Trivy'
                sh 'trivy image --severity CRITICAL --exit-code 1 $DOCKER_IMAGE:${BUILD_NUMBER}'
            }
        }

        stage('Docker Hub push'){
            steps{
                echo 'Pushing Image to the Docker Hub'
                withCredentials([
                    string(
                        credentialsId: 'dockerhub', variable: 'DOCKER_HUB'
                    )
                ]){
                    sh 'echo "$DOCKER_HUB" | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push $DOCKER_IMAGE:${BUILD_NUMBER}'
                }
            }
        }

        stage('Deployment Checkout'){
            steps{
                echo 'Cloning deployment files from GitOps repo'
                git branch: env.GITOPS_BRANCH,
                  url: env.GITOPS_REPO_URL 
            }
        }

        stage('Update GitOps repo'){
            steps{
                echo 'Updating deployment file'
                withCredentials([
                    string(
                        credentialsId: 'githubtoken', variable: 'GITHUB_TOKEN'
                    )
                ]){
                sh '''
                git config --global user.name "$USER"
                git config --global user.email "$USER_MAIL"

                sed -i "s/projectimage:.*/projectimage:${BUILD_NUMBER}/g" Deployment/deploy.yaml

                git add Deployment/deploy.yaml
                git commit -m "upading Build number ${BUILD_NUMBER}"
                
                git push https://${GITHUB_TOKEN}@github.com/${GITHUB_USER_NAME}/${GITOPS_REPO_NAME}.git HEAD:$GITOPS_BRANCH
                '''
                }
            }
        }

    }


}
