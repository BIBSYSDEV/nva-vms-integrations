package no.sikt.nva.vms.browser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class VideoProviderConfig {

    private List<Institution> institutions;

    @JsonProperty("institutions")
    public List<Institution> getInstitutions() {
        return this.institutions;
    }
}

