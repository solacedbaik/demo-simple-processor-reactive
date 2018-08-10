# converter

Sample Spring Cloud Stream (SCS) processor that converts temperatures between Fahrenheit and Celsius. Can be used in Spring Cloud Data Flow (SCDF) and has been tested with a local and PCF deployer, although it could be used with any supported deployer such as Mesos, Kubernetes, etc. 

This example uses a Maven plugin to generate an SCS app starter processor which supports RabbitMQ or Kafka as a transport.  For more details on building your own custom source, processor or sink, check out the [SCS app starter docs](https://docs.spring.io/spring-cloud-stream-app-starters/docs/current/reference/htmlsingle/#_creating_new_stream_application_starters_and_generating_artifacts).  For more details on the app generator plugin, check out the [GitHub project](https://github.com/spring-cloud/spring-cloud-app-starters-maven-plugins).

## Running the application

The following instructions assume that you are running docker containers for SCDF and its dependencies.  It is also assumed that you will adjust the command (if necessary) to work on your target platform (Windows, Linux, Mac, etc.)

Go to the application root:

On Windows:
* `set DATAFLOW_VERSION=latest`

On Linux:
* `export DATAFLOW_VERSION=latest`

You can adjust the version as needed.  Then run:

* `docker-compose up -d`

This brings up SCDF, Kafka, Zookeeper, RabbitMQ, MySQL, and Redis.  Check if SCDF is properly running:

* `docker-compose ps`

The State column should show `Up` for all services except app-import.  Local ports mapped are 9393 for SCDF, 9092 for Kafka, 5672 for RabbitMQ, and 15672 for Rabbit Admin.

* `mvnw clean install`

You can test the app as a standalone process by running the following:

```
 java -jar target\celsius-converter-processor-0.0.1-SNAPSHOT.jar
```

Go to the Rabbit Admin console and check if a queue similar to the following is bound:

* `queue.celsius.in.anonymous.[unique_identifier]`

Copy the jar and pom file to the SCDF Docker container (`dataflow-server`).

Enter the SCDF container and prepare it for app registration:

```
docker exec -it dataflow-server /bin/sh
/ # cd
~ # apk update
~ # apk add curl vim tmux net-tools
~ # apk add maven --update-cache --repository http://dl-4.alpinelinux.org/alpine/edge/community/ --allow-untrusted && rm -rf /var/cache/apk/*
~ # mvn install:install-file -Dfile=celsius-converter-processor-0.0.1-SNAPSHOT.jar -DpomFile=celsius-converter-processor-0.0.1-SNAPSHOT.pom


docker cp spring-cloud-dataflow-shell-1.6.0.BUILD-20180623.053121-42.jar dataflow-server:/root

λ docker exec -it dataflow-server /bin/sh

/ # cd
~ # java -jar spring-cloud-dataflow-shell-1.6.0.BUILD-20180623.053121-42.jar


dataflow:>app register --name celsius-converter-processor --type processor --uri maven://demo.celsius.converter:celsius-converter-processor:jar:0.0.1-SNAPSHOT

dataflow:>stream deploy --name tempconverter --properties "deployer.*.local.inheritLogging=true"

dataflow:>http post --target http://localhost:9000 --data 123
> POST (text/plain) http://localhost:9000 123
> 202 ACCEPTED


```

Enter some text and verify it comes through the console output (test consumer).

You can also go the RabbitMQ UI: `http:localhost:15672`

Once you are done testing: `docker-compose down`
