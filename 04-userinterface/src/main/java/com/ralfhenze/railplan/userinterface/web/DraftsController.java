package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.application.commands.AddRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.AddRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.AddTrainStationCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailNetworkDraftCommand;
import com.ralfhenze.railplan.application.commands.DeleteRailwayTrackCommand;
import com.ralfhenze.railplan.application.commands.DeleteTrainStationCommand;
import com.ralfhenze.railplan.application.commands.UpdateTrainStationCommand;
import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.infrastructure.persistence.MongoDbQueries;
import com.ralfhenze.railplan.infrastructure.persistence.RailNetworkDraftMongoDbRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailwayTrackDto;
import com.ralfhenze.railplan.infrastructure.persistence.dto.TrainStationDto;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DraftsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    class StationTableRow {
        public boolean showInputField = false;
        public boolean disabled = false;
        public String stationId = "";
        public String stationName = "";
        public List<String> stationNameErrors = new ArrayList<>();
        public String latitude = "0.0";
        public List<String> latitudeErrors = new ArrayList<>();
        public String longitude = "0.0";
        public List<String> longitudeErrors = new ArrayList<>();

        public StationTableRow() {}

        public boolean isShowInputField() {
            return showInputField;
        }

        public void setShowInputField(boolean showInputField) {
            this.showInputField = showInputField;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public List<String> getStationNameErrors() {
            return stationNameErrors;
        }

        public void setStationNameErrors(List<String> stationNameErrors) {
            this.stationNameErrors = stationNameErrors;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public List<String> getLatitudeErrors() {
            return latitudeErrors;
        }

        public void setLatitudeErrors(List<String> latitudeErrors) {
            this.latitudeErrors = latitudeErrors;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public List<String> getLongitudeErrors() {
            return longitudeErrors;
        }

        public void setLongitudeErrors(List<String> longitudeErrors) {
            this.longitudeErrors = longitudeErrors;
        }
    }

    @GetMapping("/drafts")
    public String drafts(Model model) {
        final var queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());

        return "drafts";
    }

    @GetMapping("/drafts/new")
    public String addDraft() {
        final var draftId =
            new AddRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
                .addRailNetworkDraft().get()
                .getId().get().toString();

        return "redirect:/drafts/" + draftId;
    }

    @GetMapping("/drafts/{currentDraftId}")
    public String draft(@PathVariable String currentDraftId, Model model) {
        setModelAttributes(currentDraftId, model, null, null, false, false);

        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @GetMapping("/drafts/{currentDraftId}/delete")
    public String deleteDraft(@PathVariable String currentDraftId) {
        new DeleteRailNetworkDraftCommand(new RailNetworkDraftMongoDbRepository(mongoTemplate))
            .deleteRailNetworkDraft(currentDraftId);

        return "redirect:/drafts";
    }

    @GetMapping("/drafts/{currentDraftId}/stations/new")
    public String showNewStationForm(@PathVariable String currentDraftId, Model model) {
        setModelAttributes(currentDraftId, model, null, null, true, false);

        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @PostMapping("/drafts/{currentDraftId}/stations/new")
    public String createNewStation(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        try {
            new AddTrainStationCommand(draftRepository).addTrainStation(
                currentDraftId,
                stationRow.stationName,
                Double.parseDouble(stationRow.latitude),
                Double.parseDouble(stationRow.longitude)
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model, null, exception.getErrorMessagesAsHashMap(), true, false);
            model.addAttribute("newTrack", new RailwayTrackDto());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    @GetMapping("/drafts/{currentDraftId}/stations/{stationId}/edit")
    public String editStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId,
        Model model
    ) {
        setModelAttributes(currentDraftId, model, stationId, null, false, false);

        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @PostMapping("/drafts/{currentDraftId}/stations/{stationId}/edit")
    public String updateStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId,
        @ModelAttribute(name = "updatedStationTableRow") StationTableRow stationRow,
        Model model
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        try {
            new UpdateTrainStationCommand(draftRepository).updateTrainStation(
                currentDraftId,
                stationId,
                stationRow.stationName,
                Double.parseDouble(stationRow.latitude),
                Double.parseDouble(stationRow.longitude)
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model, stationId, exception.getErrorMessagesAsHashMap(), false, false);
            model.addAttribute("newTrack", new RailwayTrackDto());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    @GetMapping("/drafts/{currentDraftId}/stations/{stationId}/delete")
    public String deleteStation(
        @PathVariable String currentDraftId,
        @PathVariable String stationId
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        new DeleteTrainStationCommand(draftRepository)
            .deleteTrainStation(stationId, currentDraftId);

        return "redirect:/drafts/{currentDraftId}";
    }

    @GetMapping("/drafts/{currentDraftId}/tracks/new")
    public String showNewTrackForm(
        @PathVariable String currentDraftId,
        Model model
    ) {
        setModelAttributes(currentDraftId, model, null, null, false, true);
        model.addAttribute("newTrack", new RailwayTrackDto());

        return "drafts";
    }

    @PostMapping("/drafts/{currentDraftId}/tracks/new")
    public String createNewTrack(
        @PathVariable String currentDraftId,
        @ModelAttribute(name = "newTrack") RailwayTrackDto trackDto,
        Model model
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        try {
            new AddRailwayTrackCommand(draftRepository).addRailwayTrack(
                currentDraftId,
                String.valueOf(trackDto.getFirstStationId()),
                String.valueOf(trackDto.getSecondStationId())
            );
        } catch (ValidationException exception) {
            setModelAttributes(currentDraftId, model, null, null, false, true);
            model.addAttribute("trackErrors", exception.getErrorMessagesAsHashMap());

            return "drafts";
        }

        return "redirect:/drafts/{currentDraftId}";
    }

    @GetMapping("/drafts/{currentDraftId}/tracks/{firstStationId}/{secondStationId}/delete")
    public String deleteStation(
        @PathVariable String currentDraftId,
        @PathVariable String firstStationId,
        @PathVariable String secondStationId
    ) {
        final var draftRepository = new RailNetworkDraftMongoDbRepository(mongoTemplate);

        new DeleteRailwayTrackCommand(draftRepository)
            .deleteRailwayTrack(firstStationId, secondStationId, currentDraftId);

        return "redirect:/drafts/{currentDraftId}";
    }

    private void setModelAttributes(
        final String currentDraftId,
        final Model model,
        final String stationIdToEdit,
        final Map<String, List<String>> errorMessages,
        final boolean showNewStationForm,
        final boolean showNewTrackForm
    ) {
        final var queries = new MongoDbQueries(mongoTemplate);
        model.addAttribute("draftIds", queries.getAllDraftIds());

        final var draft = new RailNetworkDraftMongoDbRepository(mongoTemplate)
            .getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));
        final var draftDto = new RailNetworkDraftDto(draft);

        model.addAttribute("currentDraftDto", draftDto);

        final Map<Integer, String> stationNames = draftDto
            .getStations()
            .stream()
            .collect(Collectors.toMap(TrainStationDto::getId, TrainStationDto::getName));

        model.addAttribute("stationNames", stationNames);

        final List<List<Pair<String, String>>> tracksWithStationNames = draftDto
            .getTracks()
            .stream()
            .map(trackDto -> List.of(
                Pair.with(
                    String.valueOf(trackDto.getFirstStationId()),
                    stationNames.get(trackDto.getFirstStationId())
                ),
                Pair.with(
                    String.valueOf(trackDto.getSecondStationId()),
                    stationNames.get(trackDto.getSecondStationId())
                )
            ))
            .collect(Collectors.toList());

        model.addAttribute("tracks", tracksWithStationNames);
        model.addAttribute("trackErrors", new HashMap<String, List<String>>());
        model.addAttribute("showNewTrackForm", showNewTrackForm);

        final List<StationTableRow> stationTableRows = draftDto
            .getStations()
            .stream()
            .map(stationDto -> {
                final var row = new StationTableRow();
                row.stationId = String.valueOf(stationDto.getId());
                row.stationName = stationDto.getName();
                row.latitude = String.valueOf(stationDto.getLatitude());
                row.longitude = String.valueOf(stationDto.getLongitude());
                row.showInputField = (row.stationId.equals(stationIdToEdit));

                if (row.stationId.equals(stationIdToEdit)) {
                    if (errorMessages != null) {
                        row.stationNameErrors = errorMessages.getOrDefault("Station name", List.of());
                        row.latitudeErrors = errorMessages.getOrDefault("Latitude", List.of());
                        row.longitudeErrors = errorMessages.getOrDefault("Longitude", List.of());
                    }

                    if (model.containsAttribute("updatedStationTableRow")) {
                        final var updatedStationTableRow = (StationTableRow) model
                            .getAttribute("updatedStationTableRow");
                        row.stationName = updatedStationTableRow.getStationName();
                        row.latitude = updatedStationTableRow.getLatitude();
                        row.longitude = updatedStationTableRow.getLongitude();
                    }
                }

                return row;
            })
            .collect(Collectors.toList());

        model.addAttribute("stationTableRows", stationTableRows);
        model.addAttribute("showNewStationForm", showNewStationForm);

        final var newStationTableRow = new StationTableRow();
        newStationTableRow.showInputField = true;
        newStationTableRow.disabled = (stationIdToEdit != null);
        if (stationIdToEdit == null && errorMessages != null) {
            newStationTableRow.stationNameErrors = errorMessages.getOrDefault("Station name", List.of());
            newStationTableRow.latitudeErrors = errorMessages.getOrDefault("Latitude", List.of());
            newStationTableRow.longitudeErrors = errorMessages.getOrDefault("Longitude", List.of());
        }
        model.addAttribute("newStationTableRow", newStationTableRow);
    }
}
