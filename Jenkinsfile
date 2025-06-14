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
	            sh """
	                mvn clean verify sonar:sonar \
	                -Dsonar.projectKey=springaop \
	                -Dsonar.host.url=http://localhost:9000 \
	                -Dsonar.login=${SONAR_AUTH_TOKEN}
          	  """
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
		stage('Push to Docker Hub') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'DockerHub',
                                          usernameVariable: 'DOCKER_USER',
                                          passwordVariable: 'DOCKER_PASS')]) {
            sh """
                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                docker tag ${IMAGE_NAME}:${IMAGE_TAG} $DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}
                docker push $DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}
            """
        }
    }
}
		stage('Docker Image Security Scan') {
    steps {
        sh '''
            docker volume create trivy-db || true
            docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy-db:/root/.cache/ aquasec/trivy image --exit-code 1 --severity CRITICAL,HIGH springaop:v1.0
        '''
    }
}
	}

}
