def buildImage() {
    echo "Build Docker Image with Dockerfile..."
    withCredentials([usernamePassword(credentialsId: 'Docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t oluwaseuna/demo-app:react-nodejs-app .'
        //sh "echo $PASS | docker login -u $USER --password-stdin"
        //sh 'docker push oluwaseuna/demo-app:react-nodejs-app'
    }
} 

def pushImage() {
    echo "Pushing Docker Image to Docker Hub Repo..."
    withCredentials([usernamePassword(credentialsId: 'Docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        //sh 'docker build -t oluwaseuna/demo-app:react-nodejs-app .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push oluwaseuna/demo-app:react-nodejs-app'
    }
} 
def deployImage() {
    echo "Deploy Docker Image to EC2..."
    def dockerCmd = 'docker run -d -p 80:80 --name react-nodejs-app oluwaseuna/demo-app:react-nodejs-app' 
    sshagent(['ec2-pem-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@52.56.46.176 ${dockerCmd}"

    }
    
} 
return this
