node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'Maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=Diego-Arreola_CleanTeam_64f0be85-f4a6-48b8-9f5c-47b05689c104 -Dsonar.projectName='CleanTeam'"
    }
  }
}
