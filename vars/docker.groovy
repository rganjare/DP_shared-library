def call() {
  node {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    
   if (env.TAG_NAME == null) {
    stage("Docker Build"){
        sh """
          sudo docker build -t rganjaredocker/${dockerCOMPONENT}:latest .
          docker login --username rganjaredocker --password Rahul#143
          docker push rganjaredocker/${dockerCOMPONENT}:latest
        """
    }
   else {
      stage('Docker Build') {
        sh """
          sudo docker build -t rganjaredocker/${dockerCOMPONENT}:${TAG_NAME} .
          docker login --username rganjaredocker --password Rahul#143
          docker push rganjaredocker/${dockerCOMPONENT}:${TAG_NAME}
        """
      }
    }

  }
}