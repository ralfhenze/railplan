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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static com.ralfhenze.railplan.userinterface.web.ControllerHelper.redirectTo;

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
    @GetMapping({"/drafts/{draftId}", "/drafts/{draftId}/stations"})
    @ResponseBody
    public String showDraftPage(@PathVariable String draftId) {
        return new StationsView(draftId, draftRepository).getHtml().render();
    }

    /**
     * Shows a form to create new Stations from presets.
     */
    @GetMapping("/drafts/{draftId}/stations/new-from-preset")
    @ResponseBody
    public String showPresetStationForm(@PathVariable String draftId) {
        return new StationsView(draftId, draftRepository)
            .withShowPresetStationForm(true)
            .getHtml()
            .render();
    }

    /**
     * Creates new Stations from presets or shows validation errors.
     */
    @PostMapping("/drafts/{draftId}/stations/new-from-preset")
    @ResponseBody
    public String createNewStationsFromPresets(
        @PathVariable String draftId,
        PresetStationFormModel presetStationFormModel,
        HttpServletResponse response
    ) {
        try {
            for (final var stationName : presetStationFormModel.getPresetStationsToAdd()) {
                final var presetStation = PresetStation.getOptionalOf(stationName);
                if (presetStation.isPresent()) {
                    trainStationService.addStationToDraft(
                        new AddTrainStationCommand(
                            draftId,
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
            return new StationsView(draftId, draftRepository)
                .withShowPresetStationForm(true)
                .withPresetStationFormModel(presetStationFormModel)
                .withValidationException(exception)
                .getHtml()
                .render();
        }

        return redirectTo("/drafts/" + draftId + "/stations", response);
    }

    /**
     * Shows a form to create a new custom Station.
     */
    @GetMapping("/drafts/{draftId}/stations/new-custom")
    @ResponseBody
    public String showNewCustomStationForm(@PathVariable String draftId) {
        return new StationsView(draftId, draftRepository)
            .withShowCustomStationForm(true)
            .getHtml()
            .render();
    }

    /**
     * Creates a new custom Station or shows validation errors.
     */
    @PostMapping("/drafts/{draftId}/stations/new-custom")
    @ResponseBody
    public String createNewCustomStation(
        @PathVariable String draftId,
        StationTableRow stationRow,
        HttpServletResponse response
    ) {
        try {
            trainStationService.addStationToDraft(
                new AddTrainStationCommand(
                    draftId,
                    stationRow.stationName,
                    Double.parseDouble(stationRow.latitude),
                    Double.parseDouble(stationRow.longitude)
                )
            );
        } catch (ValidationException exception) {
            return new StationsView(draftId, draftRepository)
                .withShowCustomStationForm(true)
                .withValidationException(exception)
                .withStationTableRow(stationRow)
                .getHtml()
                .render();
        }

        return redirectTo("/drafts/" + draftId + "/stations", response);
    }

    /**
     * Shows a form to edit an existing Station.
     */
    @GetMapping("/drafts/{draftId}/stations/{stationId}/edit")
    @ResponseBody
    public String editStation(
        @PathVariable String draftId,
        @PathVariable String stationId
    ) {
        return new StationsView(draftId, draftRepository)
            .withStationIdToEdit(Integer.parseInt(stationId))
            .getHtml()
            .render();
    }

    /**
     * Updates an existing Station or shows validation errors.
     */
    @PostMapping("/drafts/{draftId}/stations/{stationId}/edit")
    @ResponseBody
    public String updateStation(
        @PathVariable String draftId,
        @PathVariable String stationId,
        StationTableRow stationRow,
        HttpServletResponse response
    ) {
        final var intStationId = Integer.parseInt(stationId);

        try {
            trainStationService.updateStationOfDraft(
                new UpdateTrainStationCommand(
                    draftId,
                    intStationId,
                    stationRow.stationName,
                    Double.parseDouble(stationRow.latitude),
                    Double.parseDouble(stationRow.longitude)
                )
            );
        } catch (ValidationException exception) {
            return new StationsView(draftId, draftRepository)
                .withStationIdToEdit(intStationId)
                .withValidationException(exception)
                .withStationTableRow(stationRow)
                .getHtml()
                .render();
        }

        return redirectTo("/drafts/" + draftId + "/stations", response);
    }

    /**
     * Deletes an existing Station and redirects to Stations page.
     */
    @GetMapping("/drafts/{draftId}/stations/{stationId}/delete")
    public String deleteStation(
        @PathVariable String draftId,
        @PathVariable String stationId
    ) {
        trainStationService.deleteStationFromDraft(
            new DeleteTrainStationCommand(
                draftId,
                Integer.parseInt(stationId)
            )
        );

        return "redirect:/drafts/{draftId}/stations";
    }
}
