package no.sikt.nva.vms.browser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.util.List;
import nva.commons.core.paths.UriWrapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonTypeName("VideoPresentationPagedResult")
public class PagedResult<T> {

    private static final String SIZE = "size";
    private static final String OFFSET = "offset";
    @JsonProperty("@context")
    private final URI context;
    private final URI id;
    private final List<T> results;
    private final int totalSize;
    private final URI nextResults;
    private final URI previousResults;

    @JsonCreator
    public PagedResult(@JsonProperty("@context") final URI context, @JsonProperty("id") final URI id,
                       @JsonProperty("results") final List<T> results, @JsonProperty("totalSize") final int totalSize,
                       @JsonProperty("nextResults") final URI nextResults,
                       @JsonProperty("previousResults") final URI previousResults) {
        this.context = context;
        this.id = id;
        this.results = results;
        this.totalSize = totalSize;
        this.nextResults = nextResults;
        this.previousResults = previousResults;
    }

    @SuppressWarnings("PMD")
    public PagedResult(final URI context, final URI baseUri, final int pageSize, final int offset, final int totalSize,
                       final List<T> results) {
        this.context = context;
        this.id = UriWrapper.fromUri(baseUri)
                      .addQueryParameter(SIZE, Integer.toString(pageSize))
                      .addQueryParameter(OFFSET, Integer.toString(offset))
                      .getUri();
        this.totalSize = totalSize;
        this.results = results;

        if (totalSize <= pageSize) {
            this.previousResults = null;
            this.nextResults = null;
        } else if (offset <= pageSize) {
            this.previousResults = null;
            this.nextResults = createNextResultUri(baseUri, pageSize, offset);
        } else if (offset + pageSize >= totalSize) {
            this.previousResults = createPreviousResultUri(baseUri, pageSize, offset);
            this.nextResults = null;
        } else {
            this.previousResults = createPreviousResultUri(baseUri, pageSize, offset);
            this.nextResults = createNextResultUri(baseUri, pageSize, offset);
        }
    }

    public URI createNextResultUri(URI baseUri, Integer pageSize, Integer offset) {
        return UriWrapper.fromUri(baseUri)
                   .addQueryParameter(SIZE, Integer.toString(pageSize))
                   .addQueryParameter(OFFSET, Integer.toString(offset + pageSize + 1))
                   .getUri();
    }

    public URI createPreviousResultUri(URI baseUri, Integer pageSize, Integer offset) {
        return UriWrapper.fromUri(baseUri)
                   .addQueryParameter(SIZE, Integer.toString(pageSize))
                   .addQueryParameter(OFFSET, Integer.toString(offset - pageSize + 1))
                   .getUri();
    }

    public URI getContext() {
        return context;
    }

    public URI getId() {
        return id;
    }

    public List<T> getResults() {
        return results;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public URI getNextResults() {
        return nextResults;
    }

    public URI getPreviousResults() {
        return previousResults;
    }
}
