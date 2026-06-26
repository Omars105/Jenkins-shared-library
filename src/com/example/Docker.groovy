#!/usr/bin/env groovy 
package com.example 

class Docker implements Serializable {
    
    def script 

    Docker(script) {
        this.script = script
    }

    def buildDocker(String imageName) {
        script.echo 'Building the docker image...'
        script.withCredentials([script.usernamePassword(credentialsId: 'omar-dockerhub-repo', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                    script.sh "docker build -t $imageName ." 
                    script.sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                    script.sh "docker push $imageName  "   
                }
    }
}