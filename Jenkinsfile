pipeline{
	agent any
	tools {
    	    maven 'maven 3.9.7'
		
    }
	 environment {
        IMAGE_NAME = 'springaop'
        IMAGE_TAG = 'v1.0'
        DOCKERFILE_PATH = 'Dockerfile'
	SONAR_AUTH_TOKEN = credentials('sonarqube-token')
	SONAR_SCANNER_OPTS = "-Xmx512m"
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
	       stage('SonarQube Analysis') {
           	 steps {
                	withSonarQubeEnv('SonarQube') {
                 	  	 sh 'mvn clean verify sonar:sonar
				sonar-scanner \
	                        -Dsonar.projectKey=springaop \
	                        -Dsonar.sources=src \
	                        -Dsonar.host.url=http://localhost:9000 \
	                        -Dsonar.login=$SONAR_AUTH_TOKEN'
                }
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
