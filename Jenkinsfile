pipeline{
	agent any
	stages{
		stage('Checkout'){
			steps{
				git 'https://github.com/Nikki14777/SpringAOP.git'
			}
		}
		stage('Dependency Check'){
			steps{
				dependencyCheck additionalArguments: '--format XML', odcInstallation: 'dependency-check 12.1.3'
			}
		}
	}

}
