package no.sikt.nva.vms.browser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class VideoPresentation {

    private final String id;
    private final String title;
    private final String description;
    private final String timeRecorded;
    private final String presenter;
    private final String downloadUrl;
    private final String streamingUrl;
    private final String thumbnailUrl;
    private final String contentIdentifier;

    @JsonCreator
    public VideoPresentation(final @JsonProperty("id") String id,
                             final @JsonProperty("title") String title,
                             final @JsonProperty("description") String description,
                             final @JsonProperty("timeRecorded") String timeRecorded,
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

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeRecorded() {
        return timeRecorded;
    }

    public String getPresenter() {
        return presenter;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getStreamingUrl() {
        return streamingUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getContentIdentifier() {
        return contentIdentifier;
    }

    public static class Builder {

        private String id;
        private String title;
        private String description;
        private String timeRecorded;
        private String presenter;
        private String downloadUrl;
        private String streamingUrl;
        private String thumbnailUrl;
        private String contentIdentifier;

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withTimeRecorded(final String timeRecorded) {
            this.timeRecorded = timeRecorded;
            return this;
        }

        public Builder withPresenter(final String presenter) {
            this.presenter = presenter;
            return this;
        }

        public Builder withDownloadUrl(final String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder withStreamingUrl(final String streamingUrl) {
            this.streamingUrl = streamingUrl;
            return this;
        }

        public Builder withThumbnailUrl(final String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder withContentIdentifier(final String contentIdentifier) {
            this.contentIdentifier = contentIdentifier;
            return this;
        }

        public VideoPresentation build() {
            return new VideoPresentation(id, title, description, timeRecorded, presenter, downloadUrl, streamingUrl,
                                         thumbnailUrl, contentIdentifier);
        }
    }
}
