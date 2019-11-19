Feature: Ensure Rail Network Draft Invariants

  Scenario Outline: Tracks must not be longer than 300 km
    Given a Rail Network Draft with two Stations "<station-given-1>" and "<station-given-2>" (distance: ~<distance> km)
    When a Network Planner tries to connect those two stations with a new Railway Track
    Then the new Track should be <track-added>
    Examples:
      | station-given-1 | station-given-2       | distance | track-added |
      | Hamburg Hbf     | Berlin Hbf            | 252      | added       |
      | Hamburg Hbf     | Frankfurt am Main Hbf | 394      | rejected    |
