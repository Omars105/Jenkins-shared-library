#!/usr/bin/env groovy

/**
 * dockerBuild.groovy
 *
 * Reusable step to build and push Docker images.
 *
 * Usage in Jenkinsfile:
 *   dockerBuild(imageName: 'myapp', imageTag: '1.0.0', registry: 'myrepo')
 */
def call(String imageName ) {
     echo 'Building the docker image...'
    withCredentials([usernamePassword(credentialsId: 'omar-dockerhub-repo', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh "docker build -t $imageName ." 
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push $imageName  "   
                }
}
