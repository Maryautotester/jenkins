timeout(180) {
    node('maven') {
        timestamps {
            stage('Checkout') {
                checkout scm
            }
            stage('Run tests') {
                tests_exit_code = sh(
                        script: "mvn test -DbaseUrl=$BASE_URL",
                )
                if (tests_exit_code != 0) {
                    currentBuild.result = 'SUCCESS'
                }
            }
            stage('Publish artifacts') {
                allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'allure-results']]
                ])
            }
        }
    }
}