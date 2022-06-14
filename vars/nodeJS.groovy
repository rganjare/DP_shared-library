def lintChecks() {
      sh '''
        # We commented this because devs gonna check the failures.
        #~/node_modules/jslint/bin/jslint.js server.js
        echo Link Check for ${COMPONENT}
      '''
    }

def call() {
pipeline {
   agent any
    
   environment{
    SONAR=credentials('SONAR')
    NEXUS=credentials('NEXUS')
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
          expression { env.TAG_NAME != null }
         } 
          steps{
            sh '''
            npm install
            zip -r ${COMPONANT}-${TAG_NAME}.zip node_modules server.js
              '''
          }
        }

      stage("Upload Artifacts"){
          when {
          expression { env.TAG_NAME != null }
         } 
          steps{
            sh '''
            curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONANT}-${TAG_NAME}.zip http://3.236.19.102:8081/repository/${COMPONANT}/${COMPONANT}-${TAG_NAME}.zip
            '''
          }
        }


    } 
  }
}