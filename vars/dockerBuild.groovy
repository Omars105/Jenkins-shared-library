#!/usr/bin/env groovy

/**
 * dockerBuild.groovy
 *
 * Reusable step to build and push Docker images.
 *
 * Usage in Jenkinsfile:
 *   dockerBuild(imageName: 'myapp', imageTag: '1.0.0', registry: 'myrepo')
 */
def call() {
     echo 'Building the docker image...'
    withCredentials([usernamePassword(credentialsId: 'omar-dockerhub-repo', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh 'docker build -t omar1015/omar-test:jma-2.0 .'
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push omar1015/omar-test:jma-2.0  '   
                }
}
