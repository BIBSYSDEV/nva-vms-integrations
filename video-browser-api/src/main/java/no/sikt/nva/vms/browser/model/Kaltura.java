package no.sikt.nva.vms.browser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.unit.nva.commons.json.JsonSerializable;

public class Kaltura implements JsonSerializable {

    private String kalturaClientServiceUrl;
    private String kalturaClientAdminSecret;
    private int kalturaClientAdminID;
    private String kalturaClientUserID;
    private int kalturaClientConnectTimeout;
    private int kalturaClientReadTimeout;

    @JsonProperty("kalturaClientServiceUrl")
    public String getKalturaClientServiceUrl() {
        return this.kalturaClientServiceUrl;
    }

    @JsonProperty("kalturaClientAdminSecret")
    public String getKalturaClientAdminSecret() {
        return this.kalturaClientAdminSecret;
    }

    @JsonProperty("kalturaClientAdminID")
    public int getKalturaClientAdminID() {
        return this.kalturaClientAdminID;
    }

    @JsonProperty("kalturaClientUserID")
    public String getKalturaClientUserID() {
        return this.kalturaClientUserID;
    }

    @JsonProperty("kalturaClientConnectTimeout")
    public int getKalturaClientConnectTimeout() {
        return this.kalturaClientConnectTimeout;
    }

    @JsonProperty("kalturaClientReadTimeout")
    public int getKalturaClientReadTimeout() {
        return this.kalturaClientReadTimeout;
    }
}