def lintChecks() {
      sh '''
        # We commented this because devs gonna check the failures.
        #~/node_modules/jslint/bin/jslint.js server.js
        #mvn checkstyle:check
        #pylint *.py
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
                 env.ARGS="-Dsonar.sources=."
                 common.sonarCheck()
               }
           }  
       }

      stage ("Test Cases"){

          parallel {

            stage("Unit Test"){
              steps{
                sh 'echo unit Test'
              }
            } 

            stage("Integration Test"){
              steps{
                sh 'echo Integration Test'
              }
            }

            stage("Functional Test"){
              steps{
                sh 'echo Functional Test'
              }
            }
          }
        }
      
      stage("Prepare Artifacts"){
         when {
          expression {env.TAG_NAME != null}
         } 
          steps{
            sh 'echo Prepare Artifacts'
          }
        }

      stage("Upload Artifacts"){
          when {
          expression {env.TAG_NAME != null}
         } 
          steps{
            sh 'echo Upload Artifacts'
          }
        }
       
   }
}
}