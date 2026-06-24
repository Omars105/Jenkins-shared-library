# Jenkins Shared Library

A reusable Jenkins Shared Library for standardizing CI/CD pipelines across projects.

## 📁 Project Structure

```
jenkins-shared-lib/
├── vars/                          # Global pipeline steps (called directly in Jenkinsfile)
│   ├── myPipeline.groovy          # Full reusable pipeline (main entry point)
│   ├── buildJava.groovy           # Build Java apps with Maven or Gradle
│   ├── dockerBuild.groovy         # Build & push Docker images
│   └── notify.groovy              # Send Slack/email notifications
│
├── src/org/example/               # Groovy utility classes (imported via @Library)
│   ├── PipelineUtils.groovy       # General pipeline utilities
│   └── DockerUtils.groovy         # Docker-specific utilities
│
├── resources/                     # Static files loaded with libraryResource()
│   └── pipeline-config.yaml       # Default pipeline configuration
│
├── Jenkinsfile.example            # Example of how to USE this library
└── README.md
```

## 🚀 Quick Start

### Step 1: Register in Jenkins

1. Go to **Manage Jenkins → Configure System → Global Pipeline Libraries**
2. Add a new library:
   - **Name**: `jenkins-shared-lib`
   - **Default Version**: `main`
   - **Source**: Git → your repo URL

### Step 2: Use in your Jenkinsfile

**Option A – Simple (full pipeline wrapper):**
```groovy
@Library('jenkins-shared-lib') _

myPipeline(
    appName:        'my-service',
    dockerRegistry: 'myrepo',
    buildTool:      'Maven'
)
```

**Option B – Custom (use individual steps):**
```groovy
@Library('jenkins-shared-lib') _
import org.example.PipelineUtils

def utils = new PipelineUtils(this)

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                script {
                    utils.printBanner("Building my-service")
                    buildJava(tool: 'Maven')
                }
            }
        }
        stage('Docker') {
            steps {
                script {
                    dockerBuild(imageName: 'my-service', imageTag: env.BUILD_NUMBER, registry: 'myrepo')
                }
            }
        }
    }
    post {
        success { notify(status: 'SUCCESS') }
        failure { notify(status: 'FAILURE') }
    }
}
```

## 📦 Available Steps (`vars/`)

| Step | Description | Required Params |
|------|-------------|-----------------|
| `myPipeline()` | Full declarative pipeline wrapper | `appName` |
| `buildJava()` | Build Java with Maven or Gradle | — |
| `dockerBuild()` | Build & push Docker image | `imageName` |
| `notify()` | Send Slack/email notification | — |

## 🔧 Utility Classes (`src/`)

| Class | Methods |
|-------|---------|
| `PipelineUtils` | `printBanner()`, `isBranch()`, `getGitShortHash()`, `generateImageTag()` |
| `DockerUtils` | `login()`, `removeImage()`, `fullImageName()` |

## 📋 Prerequisites

- Jenkins 2.x+
- Plugins: Git, Pipeline, Docker Pipeline, Slack Notification (optional)
