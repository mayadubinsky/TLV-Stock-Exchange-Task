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
            }
        }
        stage('Build') {
            steps {
                sh '''
                rm -rf build
                mkdir build

                rsync -a --exclude 'build' --exclude '.git' ./ build/
                
                echo "commit: ${COMMIT_HASH}"       >  build/build-info.txt
                echo "committer: ${COMMITTER}"      >> build/build-info.txt
                echo "started: ${START_TIME}"       >> build/build-info.txt
                cat build/build-info.txt
                '''
            }
        }
        stage('pack'){
            steps{
                sh '''
                ARTIFACT_NAME="git-repo-${BUILD_NUMBER}.tar.gz"

                tar -czf "${ARTIFACT_NAME} -C build"

                echo "--- contents of ${ARTIFACT_NAME} ---"
                tar -tzf "${ARTIFACT_NAME}"
                '''
            }
        }    
    }
}
