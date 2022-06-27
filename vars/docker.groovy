def call() {
  node {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    
    stage("Docker Build"){
      sh "sudo docker build -t 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER-COMPONENT}:latest ."
      // Creating image for Docker hub
      sh "sudo docker build -t rganjaredocker/${DOCKER-COMPONENT}:${TAG_NAME} ."
    }

    if (env.TAG_NAME != null) {
      stage('Docker Build') {
        sh """
           docker tag 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER-COMPONENT}:latest 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER-COMPONENT}:${TAG_NAME}
           (Get-ECRLoginCommand).Password | docker login --username AWS --password-stdin 549008638695.dkr.ecr.us-east-1.amazonaws.com
           docker push 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER-COMPONENT}:${TAG_NAME}
          
          ### Pushing image into docker hub
         #docker tag 549008638695.dkr.ecr.us-east-1.amazonaws.com/${DOCKER-COMPONENT}:latest rganjaredocker/${DOCKER-COMPONENT}:${TAG_NAME}
          docker login --username rganjaredocker --password Rahul#143
          docker push rganjaredocker/${DOCKER-COMPONENT}:${TAG_NAME}
        """
      }
    }

  }
}