def call() {
  node {
    git branch: 'main', url: "https://github.com/rganjare/${COMPONENT}"
    env.APP_TYPE = "GoLang"
    common.lintChecks()
    env.ARGS="-Dsonar.java.binaries=."
    common.sonarCheck()
    common.testCases()

    if (env.TAG_NAME != null) {
      common.artifacts()
    }
  }
}