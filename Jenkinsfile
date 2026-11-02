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

                rsync -a --exclude 'build' --exclude '.git' --exclude 'artifacts' ./ build/
                
                echo "commit: ${COMMIT_HASH}"       >  build/build-info.txt
                echo "committer: ${COMMITTER}"      >> build/build-info.txt
                echo "started: ${START_TIME}"       >> build/build-info.txt
                '''
            }
        }
        stage('Pack'){
            steps{
                sh '''
                ARTIFACT_NAME="git-repo-${BUILD_NUMBER}.tar.gz"

                pwd
                ls -la
                ls -la build

                tar -czf "${ARTIFACT_NAME}" -C build .

                echo "--- contents of ${ARTIFACT_NAME} ---"
                tar -tzf "${ARTIFACT_NAME}"
                '''
            }
        }    
        stage('Upload'){
            steps{
                withCredentials([usernamePassword(credentialsId: 'github-creds', 
                                usernameVariable: 'mayadubinsky',
                                passwordVariable: 'GIT_TOKEN')]) {
                    sh '''
                    mkdir  -p artifacts
                    mv git-repo-*.tar.gz artifacts/

                    git config user.name "jenkins"
                    git config user.email "jenkins@local"

                    git add artifacts/
                    git commit -m "Add artifacts from build ${BUILD_NUMBER}"
                    git push https://{GIT_USER}:{GIT_TOKEN}@github.com/mayadubinsky/TLV-Stock-Exchange-Task.git HEAD:main
                    '''
                }
            }
        }
    }
}
