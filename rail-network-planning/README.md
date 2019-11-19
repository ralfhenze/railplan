# Rail Network Planning

* Input: -
* Output: Released Rail Network for a specific Validity Period

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

### Published Events

* Rail Network released

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
