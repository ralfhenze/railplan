package com.ralfhenze.railplan.userinterface.web.drafts.release;

import com.ralfhenze.railplan.domain.common.validation.ValidationException;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.userinterface.web.GermanySvg;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Prepares the necessary data for resources/templates/release.html.
 */
public class ReleaseView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private Map<String, List<String>> releaseErrors = Map.of();

    public ReleaseView(
        final String currentDraftId,
        final RailNetworkDraftRepository draftRepository
    ) {
        this.currentDraftId = currentDraftId;
        this.draftRepository = draftRepository;
    }

    public String getViewName() {
        return "release";
    }

    public ReleaseView withReleaseErrorsProvidedBy(final ValidationException exception) {
        this.releaseErrors = exception.getErrorMessagesAsHashMap();
        return this;
    }

    public ReleaseView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();

        model.addAttribute("currentDraftDto", draftDto);

        model.addAttribute("releaseErrors", releaseErrors);
        if (releaseErrors.isEmpty()) {
            model.addAttribute("validityPeriod", new ValidityPeriodDto(
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(1).plusMonths(1).toString()
            ));
        }

        final var germanySvg = new GermanySvg();
        model.addAttribute("germanyWidth", GermanySvg.MAP_WIDTH);
        model.addAttribute("germanyHeight", GermanySvg.MAP_HEIGHT);
        model.addAttribute("germanySvgPath", germanySvg.getPath());
        model.addAttribute("germanySvgStations", germanySvg.getStationCoordinates(
            draftDto.getStations())
        );
        model.addAttribute("germanySvgTracks", germanySvg.getTrackCoordinates(
            draftDto.getStations(), draftDto.getTracks())
        );

        return this;
    }

    private RailNetworkDraftDto getDraftDto() {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));

        return new RailNetworkDraftDto(draft);
    }
}
