
def sonarCheck() {
  stage("Sonar Code Analysis"){
      sh '''
        #sonar-scanner -Dsonar.host.url=http://172.31.15.120:9000 -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} ${ARGS}
        #sleep 10
        #sonar-quality-gate.sh ${SONAR_USR} 172.31.15.120 ${COMPONENT}
        echo Sonar Checks for ${COMPONENT}
      '''
    }
  }  

def lintChecks() {
    stage("lintChecks"){
      if(env.APP_TYPE == "NodeJS"){
        sh '''
          # We commented this because devs gonna check the failures.
          #~/node_modules/jslint/bin/jslint.js server.js
          echo Link Check for ${COMPONENT}
        '''
      } 
      else if (env.APP_TYPE == "Maven"){
          sh '''
            # We commented this because devs gonna check the failures.
            #mvn checkstyle:check
            echo Link Check for ${COMPONENT}
          '''
      }
      else if (env.APP_TYPE == "Python"){
          sh '''
            # We commented this because devs gonna check the failures.
            #pylint *.py
            echo Link Check for ${COMPONENT}
          '''
      }
      else if (env.APP_TYPE == "GoLang"){
          sh '''
            # We commented this because devs gonna check the failures.
            #~/node_modules/jslint/bin/jslint.js server.js
            echo Link Check for ${COMPONENT}
          '''
      }    
    }
}

def testCases() {

  stage("Test Cases") {
    def stages = [:]

    stages["Unit Tests"] = {
      sh 'echo Unit Tests'
    }
    stages["Integration Tests"] = {
      sh 'echo Integration Tests'
    }

    stages["Functional Tests"] = {
      sh 'echo Functional Tests'
    }

    parallel(stages)
  }

}

def artifacts() {

  stage("Check The Release") {
    env.UPLOAD_STATUS = sh(returnStdout: true, script: 'curl -L -s http://172.31.8.251:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true')

    print UPLOAD_STATUS 
  }

  stage("Prepare Artifacts") {
      if (env.APP_TYPE == "NodeJS") {
        sh '''
          npm install 
          zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js 
        '''
      } else if (env.APP_TYPE == "Maven") {
        sh '''
          echo Prepare Artifacts !!
         mvn clean package 
         mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar 
         zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar 
        '''
      } else if (env.APP_TYPE == "Python") {
        sh '''
          echo Prepare Artifacts !!
        //  zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt 
        '''
      } else if (env.APP_TYPE == "GoLang") {
        sh '''
          echo Prepare Artifacts !!
          // go mod init ${COMPONENT}
          // go get 
          // go build
          // zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}
        '''
      }
      // else if (env.APP_TYPE == "nginx" ){
      //   sh '''
      //     cd static 
      //     zip -r ../${COMPONENT}-${TAG_NAME}.zip * 
      //   '''
      // }
    }

  stage("Upload Artifacts") {
      withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
        sh '''
        curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.8.251:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      '''
      }
    }
}  