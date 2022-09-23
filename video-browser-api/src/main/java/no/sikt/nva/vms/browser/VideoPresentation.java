package no.sikt.nva.vms.browser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import no.unit.nva.commons.json.JsonSerializable;
import nva.commons.core.JacocoGenerated;

public final class VideoPresentation implements JsonSerializable {

    private final String id;
    private final String title;
    private final String description;
    private final Integer timeRecorded;
    private final String presenter;
    private final String downloadUrl;
    private final String streamingUrl;
    private final String thumbnailUrl;
    private final String contentIdentifier;

    @JsonCreator
    public VideoPresentation(final @JsonProperty("id") String id, final @JsonProperty("title") String title,
                             final @JsonProperty("description") String description,
                             final @JsonProperty("timeRecorded") Integer timeRecorded,
                             final @JsonProperty("presenter") String presenter,
                             final @JsonProperty("downloadUrl") String downloadUrl,
                             final @JsonProperty("streamingUrl") String streamingUrl,
                             final @JsonProperty("thumbnailUrl") String thumbnailUrl,
                             final @JsonProperty("contentIdentifier") String contentIdentifier) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeRecorded = timeRecorded;
        this.presenter = presenter;
        this.downloadUrl = downloadUrl;
        this.streamingUrl = streamingUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.contentIdentifier = contentIdentifier;
    }

    @JacocoGenerated
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, timeRecorded, presenter, downloadUrl, streamingUrl, thumbnailUrl,
                            contentIdentifier);
    }

    @JacocoGenerated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VideoPresentation that = (VideoPresentation) o;
        return Objects.equals(id, that.id)
               && Objects.equals(title, that.title)
               && Objects.equals(description, that.description)
               && Objects.equals(timeRecorded, that.timeRecorded)
               && Objects.equals(presenter, that.presenter)
               && Objects.equals(downloadUrl, that.downloadUrl)
               && Objects.equals(streamingUrl, that.streamingUrl)
               && Objects.equals(thumbnailUrl, that.thumbnailUrl)
               && Objects.equals(contentIdentifier, that.contentIdentifier);
    }
}
