// This functions return parameters about the SCM trigger
def call() {

  // get the start time and the run hash from git
  def startTime = new Date(currentBuild.startTimeInMillis).format('dd-MM-yyyy HH:mm:ss')
  def commitHash = env.GIT_COMMIT

  // get the committer name and save the name
  def triggeredBy = sh(
              script: "git log -1 --pretty=format:'%an'",
              returnStdout: true
          ).trim()

 // save the parameters as variables          
  env.START_TIME = startTime
  env.COMMIT_HASH = commitHash
  env.COMMITER = triggeredBy
  }
