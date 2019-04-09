# Pytheas, The intergalactic exoplanet explorer.
This simple springboot application loads a sample graph in csv format during startup and after persisting
the planets and their connections, it processes and keeps a shortest path table in memory.
In-memory map is updated in real-time if there is any route updated/created/deleted.

## Requirements
For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `bylogics.io.pytheas.PytheasApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Getting started
This simple app finds the shortest path from planet earth to any other planet.

Data Structure, DB tables and application logic, all are customized for the specific problem to provide the optimum result.

Assumptions and Criteria

1. Application is optimized for directed, weighted and sparse graph.
2. Adjacency list is used as the data structure.
3. graph is reloaded for any modification of nodes or edges in database.
4. application provides real-time updated routes with no delay, so memory was the second consideration.

The Application follows the [CQRS Pattern](https://martinfowler.com/bliki/CQRS.html) in a very simple way by separating the persistence view (Planets and Routes data store) and the query view. 
Query view is constantly refreshed and synced with data store view. This way the query view can be scaled and optimized for varied scenario (having graph for each node as start).
All the queries for route discovery are served using the query view.
Planet discovery will be the command view to manage the planets and routes.

### TODO
1. Implement HATEOAS so that api is self discoverable.
2. Support for the content negotiation to support various clients (Web, Mobile, Application Client).
3. Provide flexibility for multiple graphs for different planets as source.
4. web sockets ?? to update the route on client side??

## REst API Documentation
once the application is running,
go to http://localhost:5000/pytheas/swagger-ui.html for api usage.

 
