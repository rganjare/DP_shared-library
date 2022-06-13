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

   stages{
       stage ("Lint Check"){
           steps {
               script{
                 lintChecks()
               }
           }  
       }
   }
}
}