@Library('prepareEnv') _

pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        
        // If the last commit was from Jenkins, skip the delete it. This command uses SCM Skip plugin
        stage('Check skip') {
            steps {
                scmSkip(deleteBuild: true, skipPattern: '.*\\[ci skip\\].*')
            }
        }

        // Call prepareEnv to get commit parameters
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

                // Copy all files to a uniqe folder. Exculde unnecessey files.
                rsync -a --exclude 'build' --exclude '.git' --exclude 'artifacts' ./ build/

                // Add inforamtional file anout the commit
                echo "commit: ${COMMIT_HASH}"       >  build/build-info.txt
                echo "committer: ${COMMITER}"      >> build/build-info.txt
                echo "started: ${START_TIME}"       >> build/build-info.txt
                '''
            }
        }
        
        stage('Pack'){
            steps{
                sh '''
                ARTIFACT_NAME="git-repo-${BUILD_NUMBER}.tar.gz"
                
                // Pack and zip the build
                tar -czf "${ARTIFACT_NAME}" -C build .

                echo "--- contents of ${ARTIFACT_NAME} ---"
                tar -tzf "${ARTIFACT_NAME}"
                '''
            }
        }    
        
        stage('Upload'){
            steps{

                // Use a Personal Accsess Token to push to git
                withCredentials([usernamePassword(credentialsId: 'github-creds', 
                                usernameVariable: 'GIT_USER',
                                passwordVariable: 'GIT_TOKEN')]) {
                    sh '''
                    mkdir  -p artifacts
                    mv git-repo-*.tar.gz artifacts/
                    
                    // Upload the artifacts
                    git config user.name "jenkins"
                    git config user.email "jenkins@local"

                    git add artifacts/
                    git commit -m "Add artifacts from build ${BUILD_NUMBER} [ci skip]"
                    git push https://${GIT_USER}:${GIT_TOKEN}@github.com/mayadubinsky/TLV-Stock-Exchange-Task.git HEAD:main
                    '''
                }
            }
        }
    }
}
