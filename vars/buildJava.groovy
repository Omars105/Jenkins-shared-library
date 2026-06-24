#!/usr/bin/env groovy

/**
 * buildJava.groovy
 *
 * Reusable step to build Java applications using Maven or Gradle.
 *
 * Usage in Jenkinsfile:
 *   buildJava()
 *   buildJava(tool: 'Gradle', goals: 'clean build')
 */
def call() {
  echo "Build java"
  sh 'mvn package'
}

