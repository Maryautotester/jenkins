timeout(180) {
    node('maven') {
        timestamps {
            stage('Checkout') {
                checkout scm
            }
            stage('Run tests') {
                tests_exit_code = sh(
                        script: "mvn test -DplatformName=$PLATFORM_NAME " +
                                "-DdeviceName=$DEVICE_NAME " +
                                "-DplatformVersion=$PLATFORM_VERSION " +
                                "-DappiumServerUrl=$APPIUM_URL",
                )

                if (tests_exit_code != 0) {
                    currentBuild.result = 'UNSTABLE'
                }
            }
        }
    }
}