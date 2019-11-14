# Rail Network Planning

* Input: -
* Output: Rail Network Plan for a specific Time Period

## Domain Model

### Aggregates

* **Station**: a train station (StationName, GeoLocation)
* **Track**: a connection between two Stations (Station A, Station B, Length)
* **Rail Network Proposal**: an unvalidated, undirected, weighted graph  of connected Stations (Stations, Tracks)
* **Rail Network Plan**: a validated, unchangeable, undirected, weighted graph of connected Stations (Stations, Tracks, TimePeriod)

### Domain Events

Internal:

* Station created
* Station renamed
* Stations connected / Track created
* Track removed
* Rail Network Proposal drawn
* Rail Network Proposal validated
* Time Period defined

External:

* Rail Network Plan released

### Invariants

Station:

* a Station is located within Germany's bounding rectangle
* a Station is located on land
* a Station's GeoLocation always has Latitude and Longitude
* a Station's Name begins with an uppercase letter
* a Station's Name contains [a-zA-Z\ \.\-\(\)]
* a Station's Name is unique
* the minimum distance between two Stations is 10 km

Track:

* a Track connects two different Stations
* two Stations can only be connected by a single Track
* a Track has no direction
* the maximum length of a Track is 200 km

Rail Network Plan:

* the Rail Network Plan contains at least two Stations and one Track
* the Rail Network Plan contains no stand-alone / unconnected Stations
* the Rail Network Plan is a single graph without unconnected islands / sub-graphs
* the TimePeriods of released Rail Network Plans are continuous without gaps and don't overlap
* released Rail Network Plans can't be changed any more
