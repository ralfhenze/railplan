Feature: Ensure Rail Network Invariants

  Scenario Outline: Station names must be unique
    Given a Rail Network with a Station "<station-given>"
    When a Network Planner tries to add a new Station "<station-to-be-added>"
    Then the new Station should be <station-added>
    Examples:
      | station-given | station-to-be-added | station-added |
      | Berlin Hbf    | Hamburg Hbf         | added         |
      | Berlin Hbf    | Berlin Hbf          | rejected      |

  Scenario Outline: Minimum distance between Stations is 10 km
    Given a Rail Network with a Station "<station-given>"
    When a Network Planner tries to add a new Station "<station-to-be-added>", which is ~<distance> km away
    Then the new Station should be <station-added>
    Examples:
      | station-given | station-to-be-added | distance | station-added |
      | Berlin Hbf    | Erfurt Hbf          | 235      | added         |
      | Berlin Hbf    | Berlin Ostbahnhof   | 5        | rejected      |

  Scenario Outline: Tracks must not be longer than 300 km
    Given a Rail Network with two Stations "<station-given-1>" and "<station-given-2>" (distance: ~<distance> km)
    When a Network Planner tries to connect those two stations with a new Railway Track
    Then the new Track should be <track-added>
    Examples:
      | station-given-1 | station-given-2       | distance | track-added |
      | Hamburg Hbf     | Berlin Hbf            | 252      | added       |
      | Hamburg Hbf     | Frankfurt am Main Hbf | 394      | rejected    |
