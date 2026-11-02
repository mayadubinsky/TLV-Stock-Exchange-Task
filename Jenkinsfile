@Library('prepareEnv') _

pipeline {
    agent any

    triggers {
        githubPush()
    }
    
    stages {
        stage('Get info') {
            steps {
                prepareEnv()
                echo '${env.commitHash}'

            }
        }
    }
}
