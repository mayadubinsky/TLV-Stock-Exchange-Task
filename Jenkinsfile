@Library('prepareEnv') _

pipeline {
    agent any

    triggers {
        githubPush()
        echo '$(commitHash)'
    }
    
    stages {
        stage('Get info') {
            steps {
                prepareEnv()
            }
        }
    }
}
