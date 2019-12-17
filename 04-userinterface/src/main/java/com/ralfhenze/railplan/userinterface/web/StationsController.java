package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.application.queries.Queries;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StationsController {

    private final Queries queries;
    private final RailNetworkDraftRepository draftRepository;
    private final AddTrainStationCommand addTrainStationCommand;
    private final DeleteTrainStationCommand deleteTrainStationCommand;
    private final UpdateTrainStationCommand updateTrainStationCommand;

    @Autowired
    public StationsController(
        final Queries queries,
        final RailNetworkDraftRepository draftRepository,
        final AddTrainStationCommand addTrainStationCommand,
        final DeleteTrainStationCommand deleteTrainStationCommand,
        final UpdateTrainStationCommand updateTrainStationCommand
    ) {
        this.queries = queries;
        this.draftRepository = draftRepository;
        this.addTrainStationCommand = addTrainStationCommand;
        this.deleteTrainStationCommand = deleteTrainStationCommand;
        this.updateTrainStationCommand = updateTrainStationCommand;
    }

    /**
     * Provides a page with a list of Stations.
     */
    @GetMapping({"/drafts/{currentDraftId}", "/drafts/{currentDraftId}/stations"})
    public String showDraftPage(@PathVariable String currentDraftId, Model model) {
        return new DraftsView(currentDraftId, draftRepository, queries)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Shows a form to create a new Station.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/new")
    public String showNewStationForm(@PathVariable String currentDraftId, Model model) {
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withShowNewStationForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates a new Station or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/new")
    public String createNewStation(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "updatedStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        try {
            addTrainStationCommand.addTrainStation(
                currentDraftId,
                stationRow.stationName,
                Double.parseDouble(stationRow.latitude),
                Double.parseDouble(stationRow.longitude)
            );
        } catch (ValidationException exception) {
            return new DraftsView(currentDraftId, draftRepository, queries)
                .withShowNewStationForm(true)
                .withStationErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }

        return "redirect:/drafts/{currentDraftId}/stations";
    }

    /**
     * Shows a form to edit an existing Station.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/{stationId}/edit")
    public String editStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId,
        Model model
    ) {
        return new DraftsView(currentDraftId, draftRepository, queries)
            .withStationIdToEdit(stationId)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Updates an existing Station or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/{stationId}/edit")
    public String updateStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId,
        @ModelAttribute(name = "updatedStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        try {
            updateTrainStationCommand.updateTrainStation(
                currentDraftId,
                stationId,
                stationRow.stationName,
                Double.parseDouble(stationRow.latitude),
                Double.parseDouble(stationRow.longitude)
            );
        } catch (ValidationException exception) {
            return new DraftsView(currentDraftId, draftRepository, queries)
                .withStationIdToEdit(stationId)
                .withStationErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }

        return "redirect:/drafts/{currentDraftId}/stations";
    }

    /**
     * Deletes an existing Station and redirects to Stations page.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/{stationId}/delete")
    public String deleteStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId
    ) {
        deleteTrainStationCommand
            .deleteTrainStation(currentDraftId, stationId);

        return "redirect:/drafts/{currentDraftId}/stations";
    }
}
