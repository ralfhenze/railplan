package com.ralfhenze.rms.railnetworkplanning.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RailNetworkGraph {

    final Map<StationId, TrainStation> stations = new HashMap<>();
    final Set<DoubleTrackRailway> connections = new HashSet<>();
}
