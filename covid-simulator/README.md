# Getting Started

## Prerequisites
* Install OpenJDK 11 (OpenJDK, AdoptJDK) on your system for development, e.g.: https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot
* Install Git on your system for development (e.g. Sourcetree as Git client)
* Install [Docker] and register on their site to pull available Docker images (e.g. https://www.docker.com/products/docker-desktop) or install the required servers locally
* Install an IDE for Java on your system for development (e.g. IntelliJ)
* Open the project with your chosen IDE (e.g. IntelliJ)
* Setup your IDE to use AdoptOpenJDK11 (see above)

## Setting up the development environment
* Enable Annotation Processing on your IDE (e.g. on IntelliJ: Preferences -> Annotation Processors -> check the box Enable Annotation Processing)
* Run Apache Kafka (e.g. through a Docker container https://hub.docker.com/r/landoop/kafka-lenses-dev landoop/kafka-lenses-dev)
```
docker run -d -e ADV_HOST=127.0.0.1 -e EULA="[CHECK_YOUR_EMAIL_FOR_PERSONAL_ID]" -e SAMPLEDATA=0 -p 3030:3030 -p 9092:9092 --name kafka lensesio/box
```
* Make sure that the application is pointing to Kafka on the resources/application.properties file (e.g. localhost:9092)

## Running and testing the application locally
* Start the application through the IDE (play button) or through the gradle task ./gradlew bootRun using the cli
* By default, the application will run under port 8080

## Building and Deployment
* Make sure that the version and target image repository properties are set properly on the covid-simulator/build.gradle file
* Make sure that docker credentials are configured properly, use the following command if the image should be pushed to Google Container Registry (GCR):
```
gcloud auth configure-docker
```
* Run the following command on the home directory o`f the simulator project to build and push the Docker image on Mac/Linux:
```
./gradlew clean jib
```
* Run the following command on the home directory of the simulator project to build and push the Docker image on Windows:
```
gradlew.bat clean jib
```
* Get the credentials from the Google Kubernetes Engine:
```
gcloud container clusters get-credentials --project mpds-297011 --region europe-west3-c  mpds-cluster
```
* Use the Helm charts under covid-simulator/k8s/helm to do a deployment on the Kubernetes Cluster, e.g.:
```
helm install covid-simulator .
```
* Use the Helm charts under covid-simulator/k8s/helm to upgrade the deployment of the simulator on the Kubernetes Cluster, e.g.:
```
helm install upgrade -f override.yaml covid-simulator .
```
* Use the Helm charts under covid-simulator/k8s/helm to uninstall the deployment of the simulator on the Kubernetes Cluster, e.g.:
```
helm uninstall covid-simulator
```

### Links`
* Kafka Performance Tuning — Ways for Kafka Optimization: https://medium.com/@rinu.gour123/kafka-performance-tuning-ways-for-kafka-optimization-fdee5b19505b
* Using Helm and Kubernetes: https://www.baeldung.com/kubernetes-helm
* Jib - Containerize your Gradle Java project: https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin#quickstart
* Dockerizing Java Apps using Jib: https://www.baeldung.com/jib-dockerizing
* Kafka for development: https://lenses.io/box/
* Reactive Programming with Project Reactor: https://projectreactor.io/
* Reactor Kafka Reference Guide: https://projectreactor.io/docs/kafka/release/reference/
* Spring Boot Dependency Injection: https://www.baeldung.com/spring-dependency-injection
* Introduction to Project Lombok: https://www.baeldung.com/intro-to-project-lombok

