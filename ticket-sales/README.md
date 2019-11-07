# Ticket Sales

* Input: Rail Network, Timetable
* Output: Ticket Sold Event

## Domain Model

### Objects

* **Journey / Journey Offer**
* **Travel Time**
* **Transfer**
* **Ticket**
* **Price**
* **Connection**
* **Station**
* **TrainLine**
* **Passenger / Traveller**
* **Customer / Ticket Buyer**

### Domain Events

External:

* Ticket sold

### Invariants

Journey:

* a Journey contains at least one Connection
* a Journey's Connections are interconnected without leaps to other Stations
* a Journey happens within a single day (not until after midnight / the next day)
* a Journey always represents the shortest possible connection between Start- and DestinationStation

Ticket:

* a Ticket's price is proportional to the distance: 0.20 EUR / km
* one can only book Tickets for Journeys in the future (from tomorrow on)
* one can only book Tickets for Journeys for which enough PassengerCapacity is available
