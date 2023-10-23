timeout(30) {
    node('maven') {
        timestamps {
            stage('Checkout') {
                checkout scm
            }
            stage('Run tests') {
                tests_exit_code = sh(
                        script: "mvn test -DbaseUrl=$BASE_URL",
                        returnStatus: true
                )
                sh "echo tests_exit_code=$tests_exit_code"
                if (tests_exit_code != 0) {
                    currentBuild.result = 'UNSTABLE'
                }

            }
            stage('Publish artifacts') {
                sh "ls -la ."
                sh "echo BASE_URL=$BASE_URL > ./allure-results/environment.properties"
                allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: './allure-results']]
                ])

            }
        }
    }
}