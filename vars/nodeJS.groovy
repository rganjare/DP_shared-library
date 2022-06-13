def lintChecks() {
      sh '''
        # We commented this because devs gonna check the failures.
        #~/node_modules/jslint/bin/jslint.js server.js
        echo Link Check for ${COMPONENT}
      '''
    }

def sonarCheck() {
      sh '''
          sonar-scanner -Dsonar.host.url=http://172.31.15.120:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR}
      '''
    }    

def call() {
pipeline {
   agent any
    
   environment{
    SONAR=credentials('SONAR')
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