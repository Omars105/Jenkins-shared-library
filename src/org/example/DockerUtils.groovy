package org.example

/**
 * DockerUtils.groovy
 *
 * Utility class with Docker-specific helper methods.
 *
 * Usage in vars/*.groovy:
 *   import org.example.DockerUtils
 *   def docker = new DockerUtils(this)
 *   docker.login('my-docker-credentials')
 */
class DockerUtils implements Serializable {

    private def script

    DockerUtils(def script) {
        this.script = script
    }

    /**
     * Logs in to a Docker registry using stored Jenkins credentials.
     * @param credentialsId - Jenkins credentials ID for Docker registry
     * @param registry      - Registry URL (leave empty for Docker Hub)
     */
    void login(String credentialsId, String registry = '') {
        script.withCredentials([
            script.usernamePassword(
                credentialsVariable: 'DOCKER_PASS',
                usernameVariable: 'DOCKER_USER',
                credentialsId: credentialsId
            )
        ]) {
            def registryArg = registry ? "--password-stdin ${registry}" : '--password-stdin'
            script.sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER ${registryArg}"
        }
        script.echo "🔑 Docker login successful"
    }

    /**
     * Removes a local Docker image to free up disk space.
     */
    void removeImage(String imageName) {
        script.sh "docker rmi ${imageName} || true"
        script.echo "🗑️ Removed local image: ${imageName}"
    }

    /**
     * Returns the full image name: registry/name:tag
     */
    String fullImageName(String name, String tag, String registry = '') {
        return registry ? "${registry}/${name}:${tag}" : "${name}:${tag}"
    }
}
