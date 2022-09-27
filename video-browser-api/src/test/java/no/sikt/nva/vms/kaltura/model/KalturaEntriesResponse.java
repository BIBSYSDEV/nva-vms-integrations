package no.sikt.nva.vms.kaltura.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class KalturaEntriesResponse {

    @JsonProperty("objects")
    private final List<FakeMediaEntry> objects;
    @JsonProperty("totalCount")
    private final Integer totalCount;
    @JsonProperty("objectType")
    private final String objectType;

    @JsonCreator
    public  KalturaEntriesResponse(List<FakeMediaEntry> objects, Integer totalCount, String objectType) {
        this.objects = objects;
        this.totalCount = totalCount;
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "{" + "objects=" + objects + ", totalCount=" + totalCount + ", objectType='" + objectType + '\'' + '}';
    }
}
