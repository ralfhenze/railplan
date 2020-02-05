package com.ralfhenze.railplan.userinterface.web.networks.stations;

import com.ralfhenze.railplan.application.TrainStationService;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.EntityNotFoundException;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.RailNetworkRepository;
import com.ralfhenze.railplan.domain.railnetwork.presets.PresetStation;
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

    private final RailNetworkRepository networkRepository;
    private final TrainStationService trainStationService;

    @Autowired
    public StationsController(
        final RailNetworkRepository networkRepository,
        final TrainStationService trainStationService
    ) {
        this.networkRepository = networkRepository;
        this.trainStationService = trainStationService;
    }

    /**
     * Shows a list of Stations.
     */
    @GetMapping({"/networks/{networkId}", "/networks/{networkId}/stations"})
    @ResponseBody
    public String showNetworkPage(@PathVariable String networkId) {
        return new StationsView(networkId, networkRepository).getHtml().render();
    }

    /**
     * Shows a form to create new Stations from presets.
     */
    @GetMapping("/networks/{networkId}/stations/new-from-preset")
    @ResponseBody
    public String showPresetStationForm(@PathVariable String networkId) {
        return new StationsView(networkId, networkRepository)
            .withShowPresetStationForm(true)
            .getHtml()
            .render();
    }

    /**
     * Creates new Stations from presets or shows validation errors.
     */
    @PostMapping("/networks/{networkId}/stations/new-from-preset")
    @ResponseBody
    public String createNewStationsFromPresets(
        @PathVariable String networkId,
        PresetStationFormModel presetStationFormModel,
        HttpServletResponse response
    ) {
        try {
            for (final var stationName : presetStationFormModel.getPresetStationsToAdd()) {
                final var station = PresetStation.getOptionalOfName(stationName);

                if (station.isPresent()) {
                    trainStationService.addStationToNetwork(
                        new AddTrainStationCommand(
                            networkId,
                            station.get().getName(),
                            station.get().getLatitude(),
                            station.get().getLongitude()
                        )
                    );
                } else {
                    throw new EntityNotFoundException(
                        "Preset Station \"" + stationName + "\" does not exist"
                    );
                }
            }
        } catch (ValidationException exception) {
            return new StationsView(networkId, networkRepository)
                .withShowPresetStationForm(true)
                .withPresetStationFormModel(presetStationFormModel)
                .withValidationException(exception)
                .getHtml()
                .render();
        }

        return redirectTo("/networks/" + networkId + "/stations", response);
    }

    /**
     * Shows a form to create a new custom Station.
     */
    @GetMapping("/networks/{networkId}/stations/new-custom")
    @ResponseBody
    public String showNewCustomStationForm(@PathVariable String networkId) {
        return new StationsView(networkId, networkRepository)
            .withShowCustomStationForm(true)
            .getHtml()
            .render();
    }

    /**
     * Creates a new custom Station or shows validation errors.
     */
    @PostMapping("/networks/{networkId}/stations/new-custom")
    @ResponseBody
    public String createNewCustomStation(
        @PathVariable String networkId,
        StationTableRow stationRow,
        HttpServletResponse response
    ) {
        try {
            trainStationService.addStationToNetwork(
                new AddTrainStationCommand(
                    networkId,
                    stationRow.stationName,
                    getCoordinateDouble(stationRow.latitude),
                    getCoordinateDouble(stationRow.longitude)
                )
            );
        } catch (ValidationException exception) {
            return new StationsView(networkId, networkRepository)
                .withShowCustomStationForm(true)
                .withValidationException(exception)
                .withStationTableRow(stationRow)
                .getHtml()
                .render();
        }

        return redirectTo("/networks/" + networkId + "/stations", response);
    }

    /**
     * Shows a form to edit an existing Station.
     */
    @GetMapping("/networks/{networkId}/stations/{stationId}/edit")
    @ResponseBody
    public String editStation(
        @PathVariable String networkId,
        @PathVariable String stationId
    ) {
        return new StationsView(networkId, networkRepository)
            .withStationIdToEdit(Integer.parseInt(stationId))
            .getHtml()
            .render();
    }

    /**
     * Updates an existing Station or shows validation errors.
     */
    @PostMapping("/networks/{networkId}/stations/{stationId}/edit")
    @ResponseBody
    public String updateStation(
        @PathVariable String networkId,
        @PathVariable String stationId,
        StationTableRow stationRow,
        HttpServletResponse response
    ) {
        final var intStationId = Integer.parseInt(stationId);

        try {
            trainStationService.updateStationOfNetwork(
                new UpdateTrainStationCommand(
                    networkId,
                    intStationId,
                    stationRow.stationName,
                    getCoordinateDouble(stationRow.latitude),
                    getCoordinateDouble(stationRow.longitude)
                )
            );
        } catch (ValidationException exception) {
            return new StationsView(networkId, networkRepository)
                .withStationIdToEdit(intStationId)
                .withValidationException(exception)
                .withStationTableRow(stationRow)
                .getHtml()
                .render();
        }

        return redirectTo("/networks/" + networkId + "/stations", response);
    }

    /**
     * Deletes an existing Station and redirects to Stations page.
     */
    @GetMapping("/networks/{networkId}/stations/{stationId}/delete")
    public String deleteStation(
        @PathVariable String networkId,
        @PathVariable String stationId
    ) {
        trainStationService.deleteStationFromNetwork(
            new DeleteTrainStationCommand(
                networkId,
                Integer.parseInt(stationId)
            )
        );

        return "redirect:/networks/{networkId}/stations";
    }

    private double getCoordinateDouble(final String coordinate) {
        try {
            return Double.parseDouble(coordinate);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
