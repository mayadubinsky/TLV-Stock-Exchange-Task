// This function returns parameters about the SCM trigger
def call() {

  // get the start time and the commit hash
  def startTime = new Date(currentBuild.startTimeInMillis).format('dd-MM-yyyy HH:mm:ss z', TimeZone.getTimeZone('Asia/Jerusalem'))
  def commitHash = env.GIT_COMMIT

  // get the committer name and save the name
  def triggeredBy = sh(
              script: "git log -1 --pretty=format:'%cn'",
              returnStdout: true
          ).trim()

 // save the values as environment variables for the next stagess          
  env.START_TIME = startTime
  env.COMMIT_HASH = commitHash
  env.COMMITTER = triggeredBy
  }
