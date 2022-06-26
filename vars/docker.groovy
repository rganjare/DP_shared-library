def call() {
  node {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    
    stage("Docker Build"){
      sh '''
          pwd
          $USER 
          docker build . 
        '''
    }
  }
}