package com.ralfhenze.railplan.userinterface.web.drafts.release;

import com.ralfhenze.railplan.domain.common.validation.ValidationError;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftId;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.draft.RailNetworkDraftRepository;
import com.ralfhenze.railplan.domain.railnetwork.lifecycle.release.ReleasedRailNetwork;
import com.ralfhenze.railplan.infrastructure.persistence.dto.RailNetworkDraftDto;
import com.ralfhenze.railplan.userinterface.web.GermanySvgViewFragment;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Prepares the necessary data for resources/templates/release.html.
 */
public class ReleaseView {

    private final String currentDraftId;
    private final RailNetworkDraftRepository draftRepository;
    private Map<String, List<String>> releaseErrors = Map.of();
    private ReleasedRailNetwork network;

    public class ReleaseErrors {
        public List<String> startDateErrors = List.of();
        public List<String> endDateErrors = List.of();
    }

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

    public ReleaseView withNetwork(final ReleasedRailNetwork network) {
        this.network = network;
        return this;
    }

    public ReleaseView addRequiredAttributesTo(final Model model) {
        final var draftDto = getDraftDto();

        if (network == null) {
            model.addAttribute("releaseErrors", new ReleaseErrors());
            model.addAttribute("validityPeriod", new ValidityPeriodDto(
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(1).plusMonths(1).toString()
            ));
        } else {
            final var releaseErrors = new ReleaseErrors();
            releaseErrors.startDateErrors = getErrorsAsString(network.getPeriod().getStartDateErrors());
            releaseErrors.endDateErrors = getErrorsAsString(network.getPeriod().getEndDateErrors());
            model.addAttribute("releaseErrors", releaseErrors);
        }

        new GermanySvgViewFragment(draftDto.getStations(), draftDto.getTracks())
            .addRequiredAttributesTo(model);

        return this;
    }

    private List<String> getErrorsAsString(List<ValidationError> validationErrors) {
        return validationErrors.stream().map(e -> e.getMessage()).collect(Collectors.toList());
    }

    private RailNetworkDraftDto getDraftDto() {
        final var draft = draftRepository
            .getRailNetworkDraftOfId(new RailNetworkDraftId(currentDraftId));

        return new RailNetworkDraftDto(draft);
    }
}
