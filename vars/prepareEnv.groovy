// This functions return parameters about the SCM trigger
def call() {

  // get the start time and the run hash from git
  def startTime = new Date(currentBuild.startTimeInMillis).format('dd-MM-yyyy HH:mm:ss')
  def commitCode = env.GIT_COMMIT

  // get the committer name and save the name
  def triggeredBy = sh(
              script: "git log -1 --pretty=format:'%an'",
              returnStdout: true
          ).trim()

 // save the parameters as variables          
  env.START_TIME = startTime
  env.COMMITCODE = commitCode
  env.TRIGGERED_BY = triggeredBy
  
return "The trigger was made by ${triggeredBy}. The start time of the run was ${startTime} and the commit code is ${commitCode}"
}
