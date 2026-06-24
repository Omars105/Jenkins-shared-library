#!/usr/bin/env groovy

/**
 * notify.groovy
 *
 * Reusable step to send Slack/email notifications about build status.
 *
 * Usage in Jenkinsfile:
 *   notify(message: "Build succeeded!", status: 'SUCCESS')
 *   notify(message: "Build failed!", status: 'FAILURE', channel: '#ci-alerts')
 */
def call(Map config = [:]) {
    def message = config.get('message', "Build ${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
    def status  = config.get('status', currentBuild.currentResult)
    def channel = config.get('channel', '#jenkins')

    def color = [SUCCESS: 'good', FAILURE: 'danger', UNSTABLE: 'warning'].get(status, '#cccccc')
    def icon  = [SUCCESS: '✅', FAILURE: '❌', UNSTABLE: '⚠️'].get(status, 'ℹ️')

    echo "${icon} Sending notification [${status}]: ${message}"

    // Slack notification (requires Slack plugin)
    // slackSend(channel: channel, color: color, message: "${icon} ${message}")

    // Email notification (requires Email Extension plugin)
    // emailext(
    //     subject: "[Jenkins] ${status}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
    //     body: message,
    //     to: config.get('email', '')
    // )
}
