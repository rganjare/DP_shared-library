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
      
      stage("Check The Release"){
          steps{
            script{
              env.UPLOAD_STATUS = sh(returnStdout: true, script: 'curl -L -s http://172.31.8.251:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true')

              print UPLOAD_STATUS 
            }
          }
        }

      stage("Prepare Artifacts"){
         when {
          expression { env.TAG_NAME != null }
          expression { env.UPLOAD_STATUS == "" }
         } 
          steps{
            sh '''
            npm install
            zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
              '''
          }
        }

      stage("Upload Artifacts"){
          when {
          expression { env.TAG_NAME != null }
          expression { env.UPLOAD_STATUS == "" }
         } 
          steps{
            sh '''
            curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.8.251:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
            '''
          }
        }


    } 
  }
}