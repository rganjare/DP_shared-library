def call() {
  node {
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    env.APP_TYPE = "NodeJS"
    common.lintChecks()
    env.ARGS="-Dsonar.sources=."
    common.sonarCheck()
    common.testCases()

    if (env.TAG_NAME != null) {
      common.artifacts()
    }
  }
}