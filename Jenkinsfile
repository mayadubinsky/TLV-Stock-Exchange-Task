@Library('prepareEnv') _

pipeline {
    agent any
    stages {
        stage('Get info') {
            steps {
                prepareEnv()
                echo "${env.START_TIME}"
            }
        }
    }
}
