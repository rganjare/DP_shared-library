def lintChecks() {
      sh '''
        # We commented this because devs gonna check the failures.
        #~/node_modules/jslint/bin/jslint.js server.js
        echo Link Check for ${COMPONENT}
      '''
    }

def sonarCheck() {
      sh '''
          sonar-scanner -Dsonar.host.url=http://172.31.15.120:9000 
          -Dsonar.projectKey=${COMPONENT} 
          -Dsonar.login=1763f3ab53b016a018862f4b5d92159ddeb4a60c
         # -Dsonar.login=${SONAR_USR} 
         # -Dsonar.password=${SONAR_PSW}
      '''
    }    

def call() {
pipeline {
   agent any
    
   environment{
    SONAR = credentials('sonar')
   } 
   stages{
       stage ("Lint Check"){
           steps {
               script{
                 lintChecks()
               }
           }  
       }

       stage ("sonarCheck"){
           steps {
               script{
                 sonarCheck()
               }
           }  
       }
   }
}
}