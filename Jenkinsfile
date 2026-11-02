@Library('prepareEnv') _

pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        
        // Skip (and delete) the run if the commit message contains [ci skip]
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
                // Copy all files to a unique folder. Exclude unnecessary files.
                // Add informational file about the commit

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
        
        stage('Pack') {
            steps {
                
                // Pack and zip the build
                sh '''
                ARTIFACT_NAME="git-repo-${BUILD_NUMBER}.tar.gz"
                
                tar -czf "${ARTIFACT_NAME}" -C build .

                echo "--- contents of ${ARTIFACT_NAME} ---"
                tar -tzf "${ARTIFACT_NAME}"
                '''
            }
        }    
        
        stage('Upload'){
            steps{

                // Use a Personal Access Token to push to git
                withCredentials([usernamePassword(credentialsId: 'github-creds', 
                                usernameVariable: 'GIT_USER',
                                passwordVariable: 'GIT_TOKEN')]) {
                    
                    // Upload the artifacts
                    sh '''
                    mkdir  -p artifacts
                    mv git-repo-*.tar.gz artifacts/
                    
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
