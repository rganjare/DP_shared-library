def call() {
  node {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    
    stage("Docker Build"){
     // sh "sudo docker build -t 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER_COMPONENT}:latest ."
      // Creating image for Docker hub
      sh "sudo docker build -t rganjaredocker/${dockerCOMPONENT}:${TAG_NAME} ."
    }

    // if (env.TAG_NAME != null) {
    //   stage('Docker Build') {
    //     sh """
    //      ### Pushing image into docker hub
    //      #docker tag 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER_COMPONENT}:latest rganjaredocker/${DOCKER_COMPONENT}:${TAG_NAME}
    //       docker login --username rganjaredocker --password Rahul#143
    //       docker push rganjaredocker/${DOCKER_COMPONENT}:${TAG_NAME}
    //     """
    //   }
    // }

  }
}