def call(String project, String hubUser) {
    sh "sudo DOCKER_BUILDKIT=1 docker build -t ${hubUser}/${project} ."
    sh "sudo docker tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}"
    sh "sudo docker tag ${hubUser}/${project} ${hubUser}/${project}:latest"
    withCredentials([usernamePassword(
            credentialsId: "docker_cred",
            usernameVariable: "USER",
            passwordVariable: "PASS"
    )]) {
        sh "docker login -u '$USER' -p '$PASS'"
    }
    sh "sudo docker image push ${hubUser}/${project}:${ImageTag}"
    sh "sudo docker image push ${hubUser}/${project}:latest"
}
