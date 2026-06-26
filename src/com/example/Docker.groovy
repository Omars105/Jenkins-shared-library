#!/usr/bin/env groovy 
package com.example 

class Docker implements Serializable {
    
    def script 

    Docker(script) {
        this.script = script
    }

    def dockerbuild(String imageName) {
        script.echo 'Building the docker image...'
        script.sh "docker build -t $imageName ." 
    }



def dockerlogin() {
    script.echo 'Logging in to dockerhub...'
    script.withCredentials([script.usernamePassword(credentialsId: 'omar-dockerhub-repo', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
        script.sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
    }
}


def dockerpush(String imageName) {
    script.echo 'Pushing the docker image...'
    script.sh "docker push $imageName  "   
}


}