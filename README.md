# vaadin-quarkus

> The objective of this project is purely for learning, I am testing combining technology like Vaadin Flow with Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode.


The project is a standard Maven project. To run it from the command line, tab mvnw (Windows), or ./mvnw (Mac & Linux),
then open http://localhost:8080 in your browser.

> The 'defaultGoal' is configured in the pom file.

```shell script
# Mac & Linux
./mvnw
```

```shell script
# Windows
  mvnw
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
# Mac & Linux
./mvnw package
```

```shell script
# Windows
  mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
# Mac & Linux
./mvnw package -Dquarkus.package.type=uber-jar
```

```shell script
# Windows
mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
# Mac & Linux
./mvnw package -Dnative
```

```shell script
# Windows
mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
# Mac & Linux
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

```shell script
# Windows
mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/vaadin-quarkus-1.0.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Deploying using Docker

To build the Dockerized version of the project, run

```shell script
mvn package -Pproduction
```
```shell script
docker build . -t vaadin-quarkus-app:latest
```

Once the Docker image is correctly built, you can test it locally using

```shell script
docker run -p 8080:8080 vaadin-quarkus-app:latest
```

## Related Guides

- Vaadin Flow ([guide](https://vaadin.com/docs/latest/integrations/quarkus)): Vaadin Flow is a unique framework that
  lets you build web apps without writing HTML or JavaScript

## Provided Code

### Vaadin Flow example

This is an example application to get started with Vaadin Flow. It generates a simple view interacting with an injected
service

[Related guide section...](https://vaadin.com/docs/latest/integrations/quarkus)

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorial at [vaadin.com/docs/latest/tutorial/overview](https://vaadin.com/docs/latest/tutorial/overview).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/docs/latest/components](https://vaadin.com/docs/latest/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Build any UI without custom CSS by discovering Vaadin's set of [CSS utility classes](https://vaadin.com/docs/styling/lumo/utility-classes).
- Find a collection of solutions to common use cases at [cookbook.vaadin.com](https://cookbook.vaadin.com/).
- Find add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin).

