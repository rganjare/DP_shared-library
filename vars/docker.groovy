def call() {
  node {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    env.APP_TYPE = "NodeJS"
    
    stage("Docker Build"){
      sh 'docker build .'
    }
  }
}