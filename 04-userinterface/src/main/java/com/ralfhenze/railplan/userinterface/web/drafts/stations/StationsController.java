package com.ralfhenze.railplan.userinterface.web.drafts.stations;

import com.ralfhenze.railplan.application.TrainStationService;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StationsController {

    private final RailNetworkDraftRepository draftRepository;
    private final TrainStationService trainStationService;

    @Autowired
    public StationsController(
        final RailNetworkDraftRepository draftRepository,
        final TrainStationService trainStationService
    ) {
        this.draftRepository = draftRepository;
        this.trainStationService = trainStationService;
    }

    /**
     * Shows a list of Stations.
     */
    @GetMapping({"/drafts/{currentDraftId}", "/drafts/{currentDraftId}/stations"})
    @ResponseBody
    public String showDraftPage(@PathVariable String currentDraftId, Model model) {
        return new StationsView(currentDraftId, draftRepository)
            .getHtml(model);
    }

    /**
     * Shows a form to create new Stations from presets.
     */
    @GetMapping("/drafts/{currentDraftId}/stations/new-from-preset")
    public String showPresetStationForm(@PathVariable String currentDraftId, Model model) {
        return new StationsView(currentDraftId, draftRepository)
            .withShowPresetStationForm(true, false)
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
                if (presetStation.isPresent()) {
                    trainStationService.addStationToDraft(
                        new AddTrainStationCommand(
                            currentDraftId,
                            presetStation.get().name,
                            presetStation.get().latitude,
                            presetStation.get().longitude
                        )
                    );
                } else {
                    throw new EntityNotFoundException(
                        "Station \"" + stationName + "\" does not exist"
                    );
                }
            }
        } catch (ValidationException exception) {
            return new StationsView(currentDraftId, draftRepository)
                .withShowPresetStationForm(true, true)
                .withValidationException(exception)
                .addRequiredAttributesTo(model)
                .getViewName();
        }

        return "redirect:/drafts/{currentDraftId}/stations";
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
    public String createNewCustomStation(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "updatedStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        try {
            trainStationService.addStationToDraft(
                new AddTrainStationCommand(
                    currentDraftId,
                    stationRow.stationName,
                    Double.parseDouble(stationRow.latitude),
                    Double.parseDouble(stationRow.longitude)
                )
            );
        } catch (ValidationException exception) {
            return new StationsView(currentDraftId, draftRepository)
                .withShowCustomStationForm(true)
                .withValidationException(exception)
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
            trainStationService.updateStationOfDraft(
                new UpdateTrainStationCommand(
                    currentDraftId,
                    stationId,
                    stationRow.stationName,
                    Double.parseDouble(stationRow.latitude),
                    Double.parseDouble(stationRow.longitude)
                )
            );
        } catch (ValidationException exception) {
            return new StationsView(currentDraftId, draftRepository)
                .withStationIdToEdit(stationId)
                .withValidationException(exception)
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
        trainStationService.deleteStationFromDraft(
            new DeleteTrainStationCommand(currentDraftId, stationId)
        );

        return "redirect:/drafts/{currentDraftId}/stations";
    }
}
