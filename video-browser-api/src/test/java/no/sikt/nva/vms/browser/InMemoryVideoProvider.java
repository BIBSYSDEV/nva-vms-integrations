package no.sikt.nva.vms.browser;

import com.amazonaws.services.lambda.runtime.Context;
import java.net.URI;
import java.util.List;

public class InMemoryVideoProvider implements VideoProvider {
    private final String context;
    private final URI baseUri;
    private final List<VideoPresentation> videoPresentations;

    public InMemoryVideoProvider(final String context,
                                 final URI baseUri,
                                 final List<VideoPresentation> videoPresentations) {
        this.context = context;
        this.baseUri = baseUri;
        this.videoPresentations = videoPresentations;
    }

    @Override
    public PagedResult<VideoPresentation> fetchVideoPresentations(int pageSize, int offset) {
        var lastIndexOfVideoOnThisPage = offset + pageSize;
        if (lastIndexOfVideoOnThisPage > videoPresentations.size()) {
            lastIndexOfVideoOnThisPage = videoPresentations.size();
        }
        if (offset > videoPresentations.size()) {
            offset = videoPresentations.size();
        }
        return new PagedResult<>(context,
                                 baseUri,
                                 pageSize,
                                 offset,
                                 videoPresentations.size(),
                                 videoPresentations.subList(offset, lastIndexOfVideoOnThisPage));
    }

}
