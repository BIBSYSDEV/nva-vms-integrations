package no.sikt.nva.vms.browser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import nva.commons.core.JacocoGenerated;

public class Institution {

    private String id;
    private Kaltura kaltura;
    private Panopto panopto;

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("kaltura")
    public Kaltura getKaltura() {
        return this.kaltura;
    }

    @JacocoGenerated
    @JsonProperty("panopto")
    public Panopto getPanopto() {
        return this.panopto;
    }
}
