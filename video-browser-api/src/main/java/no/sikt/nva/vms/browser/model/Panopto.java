package no.sikt.nva.vms.browser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import nva.commons.core.JacocoGenerated;

@JacocoGenerated
public class Panopto {

    private String instanceEndpoint;
    private String username;
    private String password;
    private String identityProviderInstanceName;
    private String serverFqdn;
    private String applicationKey;
    private String clientId;
    private String clientSecret;

    @JsonProperty("instanceEndpoint")
    public String getInstanceEndpoint() {
        return this.instanceEndpoint;
    }


    @JsonProperty("username")
    public String getUsername() {
        return this.username;
    }



    @JsonProperty("password")
    public String getPassword() {
        return this.password;
    }


    @JsonProperty("identityProviderInstanceName")
    public String getIdentityProviderInstanceName() {
        return this.identityProviderInstanceName;
    }


    @JsonProperty("serverFqdn")
    public String getServerFqdn() {
        return this.serverFqdn;
    }


    @JsonProperty("applicationKey")
    public String getApplicationKey() {
        return this.applicationKey;
    }



    @JsonProperty("clientId")
    public String getClientId() {
        return this.clientId;
    }

    @JsonProperty("clientSecret")
    public String getClientSecret() {
        return this.clientSecret;
    }

}