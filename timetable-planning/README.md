# Timetable Planning

* Input: Rail Network
* Output: Timetable

## Domain Model

### Aggregates

* **Station**: a train station (StationName, GeoLocation)
* **Track**: a connection between two Stations (Station A, Station B, Length)
* **Rail Network**: an undirected graph of Stations connected via Tracks (Stations, Tracks, TimePeriod)
* **Connection**: a scheduled connection between two Stations (DepartureStation, DepartureTime, ArrivalStation, ArrivalTime)
* **Train Line**: a named collection of Conncetions (TrainLineName, PassengerCapacity, Conncetions)
* **Timetable**: a collection of Train Lines (TrainLines, TimePeriod)

### Domain Events

External:

* Timetable released

### Invariants

Connection:

* DepartureTime is before (<) ArrivalTime
* Departure- and ArrivalTime only have hours and minutes (no seconds)
* Departure- and ArrivalTime is between 4:00 and 23:59 (inclusive), so from 0:00 until 3:59 the network is free for maintenance and freight traffic and we won't get into trouble with daylight saving time changes
* a Train constantly drives 150 km/h
* a Connection can only connect existing Stations of the RailNetwork on existing Tracks

Train Line:

* a TrainLine contains at least one Connection
* a TrainLine's name has to be unique within the Timetable
* a TrainLine's name should begin with an uppercase letter or a number
* a TrainLine's PassengerCapacity has to be between 10-800 (inclusive)
* a TrainLine is only scheduled for a single day (no Arrivals/Departures after midnight)
* a Train has to stop on all intermediate Stations of it's trip (no transit)
* the period between Arrival- and DepartureTime for a Train on a Station is 5 minutes

Timetable:

* a Timetable contains at least one TrainLine
* all Stations of the RailNetwork have to be reachable
* there must be no stand-alone disconnected TrainLine
* the TimePeriods of released Timetables are continuous without gaps and don't overlap
* Trains driving in the same direction on the same Track must have at least 10 minutes distance of time
