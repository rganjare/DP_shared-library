def sonarCheck() {
      sh '''
          sonar-scanner -Dsonar.host.url=http://172.31.15.120:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR}
          sleep 5
          #sonar-quality-gate.sh ${SONAR_USR} 172.31.15.120 ${COMPONENT}
          echo Sonar Checks for ${COMPONENT}
      '''
    }