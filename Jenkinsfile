pipeline{
	agent any
	tools {
    	    maven 'maven 3.9.7'  // This must match the name from Global Tool Configuration
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
	}

}
