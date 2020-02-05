package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a whole RailNetwork.
 */
public class DeleteRailNetworkCommand implements Command {

    private final String networkId;

    public DeleteRailNetworkCommand(final String networkId) {
        this.networkId = networkId;
    }

    public String getNetworkId() {
        return networkId;
    }
}
