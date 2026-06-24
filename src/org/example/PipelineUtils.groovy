package org.example

/**
 * PipelineUtils.groovy
 *
 * Utility class with helper methods shared across pipeline steps.
 * These are plain Groovy classes (not steps), loaded via @Library.
 *
 * Usage in vars/*.groovy or Jenkinsfile:
 *   import org.example.PipelineUtils
 *   def utils = new PipelineUtils(this)
 *   utils.printBanner("Starting Build")
 */
class PipelineUtils implements Serializable {

    private def script  // reference to the pipeline script (this)

    PipelineUtils(def script) {
        this.script = script
    }

    /**
     * Prints a formatted banner to the Jenkins console.
     */
    void printBanner(String message) {
        def line = '=' * 60
        script.echo "\n${line}\n  ${message}\n${line}"
    }

    /**
     * Returns true if the current branch matches a pattern.
     */
    boolean isBranch(String pattern) {
        def branch = script.env.BRANCH_NAME ?: ''
        return branch ==~ pattern
    }

    /**
     * Gets the short Git commit hash.
     */
    String getGitShortHash() {
        return script.sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
    }

    /**
     * Generates a Docker image tag using branch name + commit hash.
     */
    String generateImageTag() {
        def branch = (script.env.BRANCH_NAME ?: 'local').replaceAll('/', '-')
        def hash   = getGitShortHash()
        return "${branch}-${hash}"
    }

    /**
     * Checks if a file exists in the workspace.
     */
    boolean fileExists(String filePath) {
        return script.fileExists(filePath)
    }
}
