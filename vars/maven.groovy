def lintChecks() {
      sh '''
        # We commented this because devs gonna check the failures.
        #~/node_modules/jslint/bin/jslint.js server.js
        #mvn checkstyle:check
        echo Link Check for ${COMPONENT}
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
                  sh 'mvn clean compile'
                  env.ARGS="-Dsonar.java.binaries=target/"
                  common.sonarCheck()
               }
           }  
       }
   }
}
}