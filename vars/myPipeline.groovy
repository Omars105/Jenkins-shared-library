#!/usr/bin/env groovy

/**
 * myPipeline.groovy
 *
 * A complete reusable pipeline step that wraps a full declarative pipeline.
 * This is the main entry point for teams using this shared library.
 *
 * Usage in Jenkinsfile:
 *   @Library('jenkins-shared-lib') _
 *   myPipeline(
 *       appName: 'my-service',
 *       dockerRegistry: 'myrepo',
 *       buildTool: 'Maven'
 *   )
 */
def call(Map config = [:]) {
    def appName        = config.get('appName')       ?: error("'appName' is required")
    def dockerRegistry = config.get('dockerRegistry', '')
    def buildTool      = config.get('buildTool', 'Maven')
    def branch         = config.get('branch', 'main')
    def imageTag       = config.get('imageTag', env.BUILD_NUMBER ?: 'latest')

    pipeline {
        agent any

        options {
            timestamps()
            timeout(time: 30, unit: 'MINUTES')
            disableConcurrentBuilds()
        }

        environment {
            APP_NAME    = "${appName}"
            IMAGE_TAG   = "${imageTag}"
            REGISTRY    = "${dockerRegistry}"
        }

        stages {
            stage('Checkout') {
                steps {
                    script {
                        echo "📥 Checking out branch: ${branch}"
                        checkout scm
                    }
                }
            }

            stage('Build') {
                steps {
                    script {
                        buildJava(tool: buildTool)
                    }
                }
            }

            stage('Test') {
                steps {
                    script {
                        echo "🧪 Running tests..."
                        if (buildTool == 'Maven') {
                            sh "mvn test"
                        } else {
                            sh "./gradlew test"
                        }
                    }
                }
                post {
                    always {
                        junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                    }
                }
            }

            stage('Docker Build & Push') {
                when {
                    branch branch
                }
                steps {
                    script {
                        dockerBuild(
                            imageName: appName,
                            imageTag: imageTag,
                            registry: dockerRegistry
                        )
                    }
                }
            }
        }

        post {
            success {
                script {
                    notify(status: 'SUCCESS', message: "✅ ${appName} build #${env.BUILD_NUMBER} succeeded!")
                }
            }
            failure {
                script {
                    notify(status: 'FAILURE', message: "❌ ${appName} build #${env.BUILD_NUMBER} failed!")
                }
            }
            always {
                cleanWs()
            }
        }
    }
}
