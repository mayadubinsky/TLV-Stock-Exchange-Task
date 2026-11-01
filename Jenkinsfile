@Library('prepareEnv') _

pipeline {
    agent any
    stages {
        stage('Get info') {
            steps {
                prepareEnv()
                echo $START_TIME
            }
        }
    }
}
