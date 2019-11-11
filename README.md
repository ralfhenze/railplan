# Railway Management System

A domain-driven implementation of a fictional railway network. It contains subsystems / microservices for rail network and timetable planning, as well as ticket sales.

## Railway Domain Links

https://en.wikipedia.org/wiki/Rail_transport
https://en.wikipedia.org/wiki/Glossary_of_rail_transport_terms


## Simplifying Assumptions

As this is a one-man project and my goal is not to model the real-world with all it's complexities, I will make some simplifying assumptions about the railway domain:

* no freight traffic, just passenger traffic
* no distinction between long- and short-distance trains
* no distinction between first and second class
* no seats, no coaches, just a single number of passenger capacity per train
* therefore no seat reservations


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
* no region tickets, no "Use any connection from A to B on day X" tickets, just tickets from A to B tied to a certain train connection
* no complex pricing models and special offers, just a constant ticket price per kilometer
* no several-day journeys, just between 4:00 and 23:59 on a single day
* no "I am sick and won't travel": everybody who has bought a ticket will go exactly the given route
* no booking cancellation


Even with these assumptions the domain is complex enough to study Microservices and Domain-Driven Design (which is my actual goal for this). Someday I will eventually remove and implement some of the assumptions to see, how the changes affect the overall system.