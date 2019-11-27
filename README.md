# Railway Management System

A domain-driven implementation of a fictional railway network. It contains subsystems / microservices for rail network and timetable planning, as well as ticket sales.

## Run

There is not much to run yet, except for a few tests:

```
$ docker-compose up -d
$ mvn test
```

## Railway Domain Links

* https://en.wikipedia.org/wiki/Rail_transport
* https://en.wikipedia.org/wiki/Glossary_of_rail_transport_terms

## Simplifying Assumptions

As this is a one-man project and my goal is not to model the real-world with all it's complexities, I will make some simplifying assumptions about the railway domain:

* just passenger traffic, no freight traffic
* just trains, no distinction between long- and short-distance trains
* just one type of passenger, no distinction between first and second class
* just a single number of passenger capacity per train, no coaches, no seats, no seat reservations
* the rail network is just within Germany (1 time zone)
* a connection between two stations is built of two tracks, one for each direction, no passing loops, no more than two parallel tracks
* a connection between two stations is a straight line, no curves, no altitude differences
* trains travel at constant speed of 150 km/h, no acceleration, no deceleration, no distinct high-speed-tracks and -trains
* trains stop at each station on their way, no transit
* no trains between 0:00 and 3:59
* no distinct platforms on stations
* a station can hold any number of trains at the same time
* 5 minutes is enough for a transfer in any case
* therefore every train will always wait 5 minutes on each station
* people always want to travel the shortest connection
* people can and want to transfer as much as needed
* just tickets from A to B tied to a certain train connection, no region tickets, no "Use any connection from A to B on day X" tickets
* just a constant ticket price per kilometer, no complex pricing models and special offers
* just single day journeys between 4:00 and 23:59, no journeys over several days
* everybody who has bought a ticket will go exactly the given route, no "I am sick and won't travel", no booking cancellation

Even with these assumptions the domain is complex enough to study Microservices and Domain-Driven Design (which is my actual goal for this). Someday I will eventually remove and implement some of the assumptions to see, how the changes affect the overall system.

## Domain Model

### Ubiquitous Language Glossary

Term                      | Acceptable Synonym | Description
--------------------------|--------------------|-----------------------------------------------------
**Train Station**         | _Station_          | Building where trains regularly stop to load or unload passengers, [more info](https://en.wikipedia.org/wiki/Train_station)
**Railway Track**         | _Track_            | Straight [double track railway](https://en.wikipedia.org/wiki/Double-track_railway) connection between two train stations, [more info](https://en.wikipedia.org/wiki/Track_%28rail_transport%29)
**Rail Network**          |                    | TODO
**Rail Network Draft**    | _Draft_            | TODO
**Released Rail Network** | _Network_          | TODO
**Validity Period**       | _Period_           | TODO
**Geo Location**          | _Location_         | [Geographic coordinates](https://en.wikipedia.org/wiki/Geographic_coordinate_system) within Germany, defined by [Latitude](https://en.wikipedia.org/wiki/Latitude) and [Longitude](https://en.wikipedia.org/wiki/Longitude)
**Network Planner**       | _Planner_          | Person who plans and designs the Rail Network

### Commands

* Add Rail Network Draft
* Add Train Station
* Add Railway Track
* Rename Train Station
* Relocate Train Station
* Remove Train Station
* Remove Railway Track
* Release Rail Network

### Invariants

Train Station:

* a Station is located within Germany's bounding rectangle
* a Station is located on land
* a Station's Name begins with an uppercase letter
* a Station's Name is allowed to contain \[a-zA-ZäöüßÄÖÜ .-()\]
* a Station's Name is unique within the Rail Network
* the minimum distance between two Stations is 10 km

Railway Track:

* a Track connects two different Stations
* two Stations can only be connected by a single Track
* a Track has no direction
* the maximum length of a Track is 200 km

Released Rail Network:

* the Rail Network contains at least two Stations and one Track
* the Rail Network is a single graph without unconnected Stations or sub-graphs
* the Validity Periods of Released Rail Networks are continuous without gaps and don't overlap
* Released Rail Networks can't be changed any more
