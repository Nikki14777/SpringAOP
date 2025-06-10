pipeline{
	agent any
	tools {
    	    maven 'maven 3.9.7'  // This must match the name from Global Tool Configuration
    }
	 environment {
        IMAGE_NAME = 'SpringAOP'
        IMAGE_TAG = 'v1.0'
        DOCKERFILE_PATH = 'Dockerfile'
    }
	stages{
		stage('Checkout'){
			steps{
				git 'https://github.com/Nikki14777/SpringAOP.git'
			}
		}
		stage('Build & Test') {
      		      steps {
                	sh 'mvn clean install'
            }
        }
		 stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
		stage('Dependency Check'){
			steps{
				dependencyCheck additionalArguments: '--format XML', odcInstallation: 'dependency-check 12.1.3'
			}
		}
		stage('Test Reports') {
        	    steps {
                	junit '**/target/surefire-reports/*.xml'
            }
      	  }
		stage('Build Docker Image') {
            steps {
                sh """
                    docker build -f ${DOCKERFILE_PATH} -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Verify Image') {
            steps {
                sh 'docker images | grep ${IMAGE_NAME}'
            }
        }
	}

}
