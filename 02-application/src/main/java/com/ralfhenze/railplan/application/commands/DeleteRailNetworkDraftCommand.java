package com.ralfhenze.railplan.application.commands;

/**
 * A command DTO to delete a whole RailNetworkDraft.
 */
public class DeleteRailNetworkDraftCommand implements Command {

    private final String draftId;

    public DeleteRailNetworkDraftCommand(final String draftId) {
        this.draftId = draftId;
    }

    public String getDraftId() {
        return draftId;
    }
}
