```java
/*****************************************************************
 *                       DISCLAIMER                              *
 *  This software is under heavy development!                    *
 *  It's not ready for production use. There are still a lot of  *
 *  things to do and bugs to fix. Feel free to play around with  *
 *  it, but don't expect everything is working fine.             *
 *****************************************************************/
```

# RailPlan

RailPlan is a web application that let's you create simple Railway Networks within Germany. The Railway Network is
basically an undirected graph, composed of Train Stations as nodes and Railway Tracks as edges.

[![GitHub](https://github.com/ralfhenze/railplan/workflows/Build%20%26%20Test/badge.svg)](https://github.com/ralfhenze/railplan/actions?query=workflow%3A%22Build+%26+Test%22)
[![CodeFactor](https://www.codefactor.io/repository/github/ralfhenze/railplan/badge)](https://www.codefactor.io/repository/github/ralfhenze/railplan)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/d584008f1f9f4bb098fcff7253ca5565)](https://www.codacy.com/manual/ralfhenze/railplan?utm_source=github.com&utm_medium=referral&utm_content=ralfhenze/railplan&utm_campaign=Badge_Coverage)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d584008f1f9f4bb098fcff7253ca5565)](https://www.codacy.com/manual/ralfhenze/railplan?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ralfhenze/railplan&amp;utm_campaign=Badge_Grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/4b9d0e226c22dcfb33b5/maintainability)](https://codeclimate.com/github/ralfhenze/railplan/maintainability)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ralfhenze_railplan&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=ralfhenze_railplan)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ralfhenze_railplan&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=ralfhenze_railplan)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ralfhenze_railplan&metric=bugs)](https://sonarcloud.io/dashboard?id=ralfhenze_railplan)

## Run

```console           
$ docker-compose up -d
$ mvn spring-boot:run
```
The UI should now be accessible at <http://localhost:8080/>

### Tests

Naming scheme / test class suffixes:

*   UT = Unit Tests
*   IT = Integration Tests
*   ET = End-to-End Tests

Run tests:

```console
$ docker-compose up -d                 # Start testing environment
$ mvn verify                           # Run all tests
$ mvn test                             # Run only unit and acceptance tests
$ docker-compose down                  # Destroy Docker containers
```

## Goals

Why am I doing this:

*   to have a public reference web project in Java and Spring MVC 
*   to show my understanding of Domain-Driven Design (how to enforce domain invariants and not letting the domain model
    get into an invalid state)
*   to have a web project with almost full test coverage
*   to improve my Clean Code and Software Design skills

## Implementation Notes

Architecture:

*   Onion Architecture
*   Domain-Driven Design
*   Validation
*   CQRS

Domain Core:

*   Eclipse Collections

UI:

*   the UI is completely rendered on the server
*   no JavaScript is used to keep things simple for now
*   I orginally used [Thymeleaf](https://www.thymeleaf.org/) as a template engine, but
    [the templates](https://github.com/ralfhenze/railplan/tree/c3033e918bbd78033c602d05efe04ecf84969876/04-userinterface/src/main/resources/templates)
    where a PITA to maintain, so I switched to [j2html](https://j2html.com/), which I like a lot for it's type-safety
*   missing type-safety is also the reason why I stopped using Spring MVC's Model class
*   my current approach to HTML rendering is somewhat inspired by React, with composable and immutable View objects
    resembling React's functional components

Tests:

*   JUnit4
*   I use [AssertJ](https://joel-costigliola.github.io/assertj/) for assertions instead of JUnit's default ones or
    Hamcrest, because IMHO they are much easier to read and use
*   Mockito for mocking
*   Cucumber for BDD
*   JSoup for HTML parsing
*   Selenium for end-to-end tests

Quality Assurance:

*   I use [GitHub Actions](https://github.com/ralfhenze/railplan/actions) for continuous integration, to automatically
    run the tests on each push
*   I try to adhere to the [Clean Code principles](https://clean-code-developer.com/) as good as possible

Coding Conventions:

*   I use Java's new local type inference (```var```) wherever possible, to make the code easier to read
*   I use the ```final``` keyword wherever I can, to enforce immutability on local variables, class fields and method
    parameters. I know
    [this is](https://softwareengineering.stackexchange.com/questions/98691/excessive-use-final-keyword-in-java)
    [debatable](https://stackoverflow.com/questions/137868/using-the-final-modifier-whenever-applicable-in-java)
    but in this case I favor compile-time strictness over readability

## Domain Model

### Ubiquitous Language Glossary

| Term                      | Acceptable Synonym | Description                                                                                                                                                                                                                 |
|---------------------------|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Train Station**         | _Station_          | Building where trains regularly stop to load or unload passengers, [more info](https://en.wikipedia.org/wiki/Train_station)                                                                                                 |
| **Railway Track**         | _Track_            | Straight [double track railway](https://en.wikipedia.org/wiki/Double-track_railway) connection between two train stations, [more info](https://en.wikipedia.org/wiki/Track_%28rail_transport%29)                            |
| **Rail Network**          |                    | TODO                                                                                                                                                                                                                        |
| **Rail Network Draft**    | _Draft_            | TODO                                                                                                                                                                                                                        |
| **Released Rail Network** | _Network_          | TODO                                                                                                                                                                                                                        |
| **Validity Period**       | _Period_           | TODO                                                                                                                                                                                                                        |
| **Geo Location**          | _Location_         | [Geographic coordinates](https://en.wikipedia.org/wiki/Geographic_coordinate_system) within Germany, defined by [Latitude](https://en.wikipedia.org/wiki/Latitude) and [Longitude](https://en.wikipedia.org/wiki/Longitude) |
| **Network Planner**       | _Planner_          | Person who plans and designs the Rail Network                                                                                                                                                                               |

### Simplifying Assumptions

As this is a one-man project and my goal is not to model the real-world with all its complexities, I will make some simplifying assumptions:

*   the Rail Network is just within Germany
*   a Track between two Stations is a straight line, no curves, no altitude differences
*   a Track between two Stations is actually a double track railway, one for each direction, no passing loops

### Invariants

Train Station:

*   a Station is located within Germany's bounding rectangle
*   a Station's Name begins with an uppercase letter
*   a Station's Name is allowed to contain \[a-zA-ZäöüßÄÖÜ .-()\]
*   a Station's Name is unique within the Rail Network
*   the minimum distance between two Stations is 10 km

Railway Track:

*   a Track connects two different Stations
*   two Stations can only be connected by a single Track
*   the maximum length of a Track is 300 km

Released Rail Network:

*   the Rail Network contains at least two Stations and one Track
*   the Rail Network is a single graph without unconnected Stations or sub-graphs
*   the Validity Periods of Released Rail Networks are continuous without gaps and don't overlap
*   Released Rail Networks can't be changed any more

### Commands

*   Add Rail Network Draft
*   Delete Rail Network Draft
*   Add Train Station
*   Update Train Station
*   Delete Train Station
*   Add Railway Track
*   Delete Railway Track
*   Release Rail Network Draft
