package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
public class StationsController {

    private final RailNetworkDraftRepository draftRepository;
    private final AddTrainStationCommand addTrainStationCommand;
    private final DeleteTrainStationCommand deleteTrainStationCommand;
    private final UpdateTrainStationCommand updateTrainStationCommand;

    @Autowired
    public StationsController(
        final RailNetworkDraftRepository draftRepository,
        final AddTrainStationCommand addTrainStationCommand,
        final DeleteTrainStationCommand deleteTrainStationCommand,
        final UpdateTrainStationCommand updateTrainStationCommand
    ) {
        this.draftRepository = draftRepository;
        this.addTrainStationCommand = addTrainStationCommand;
        this.deleteTrainStationCommand = deleteTrainStationCommand;
        this.updateTrainStationCommand = updateTrainStationCommand;
    }

    /**
     * Shows a list of Stations.
     */
    @GetMapping({"/drafts/{currentDraftId}", "/drafts/{currentDraftId}/stations"})
    public String showDraftPage(@PathVariable String currentDraftId, Model model) {
        return new StationsView(currentDraftId, draftRepository)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Shows a form to create new Stations from presets.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/new-from-preset")
    public String showPresetStationForm(@PathVariable String currentDraftId, Model model) {
        return new StationsView(currentDraftId, draftRepository)
            .withShowPresetStationForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates new Stations from presets or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/new-from-preset")
    public String createNewStationsFromPresets(
        @PathVariable String currentDraftId,
        @ModelAttribute PresetStationFormModel presetStationFormModel,
        Model model
    ) {
        try {
            for (final var stationName : presetStationFormModel.getPresetStationsToAdd()) {
                final var presetStation = PresetStation.getOptionalOf(stationName);
                if (!presetStation.isEmpty()) {
                    addTrainStationCommand.addTrainStation(
                        currentDraftId,
                        presetStation.get().name,
                        presetStation.get().latitude,
                        presetStation.get().longitude
                    );
                } else {
                    throw new ValidationException(
                        Map.of("Station Name", List.of(stationName + " does not exist"))
                    );
                }
            }
            return "redirect:/drafts/{currentDraftId}/stations";
        } catch (ValidationException exception) {
            return new StationsView(currentDraftId, draftRepository)
                .withShowPresetStationForm(true)
                .withPresetStationErrorsProvidedBy(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }
    }

    /**
     * Shows a form to create a new custom Station.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/new-custom")
    public String showNewStationForm(@PathVariable String currentDraftId, Model model) {
        return new StationsView(currentDraftId, draftRepository)
            .withShowCustomStationForm(true)
            .addRequiredAttributesTo(model)
            .getViewName();
    }

    /**
     * Creates a new custom Station or shows validation errors.
     */
    @PostMapping("/drafts/{currentDraftId}/stations/new-custom")
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
            return new StationsView(currentDraftId, draftRepository)
                .withShowCustomStationForm(true)
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
        return new StationsView(currentDraftId, draftRepository)
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
            return new StationsView(currentDraftId, draftRepository)
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