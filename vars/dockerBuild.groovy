def call(String project, String hubUser, String ImageTag) {
    echo "Building image for ${hubUser}/${project}:${ImageTag}"
    withEnv(["DOCKER_BUILDKIT=1"]) {
        sh "docker build -t ${hubUser}/${project} ."
        sh "docker tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}"
        sh "docker tag ${hubUser}/${project} ${hubUser}/${project}:latest"

        withCredentials([usernamePassword(
                credentialsId: "docker_cred",
                usernameVariable: "USER",
                passwordVariable: "PASS"
        )]) {
            echo "Logging into Docker Hub"
            sh "docker login -u '$USER' -p '$PASS'"
        }
        echo "Pushing image ${hubUser}/${project}:${ImageTag}"
        sh "docker image push ${hubUser}/${project}:${ImageTag}"
        sh "docker image push ${hubUser}/${project}:latest"
    }
}
