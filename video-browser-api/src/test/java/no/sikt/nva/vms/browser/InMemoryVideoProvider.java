package no.sikt.nva.vms.browser;

import java.net.URI;
import java.util.List;

public class InMemoryVideoProvider implements VideoProvider {
    private final URI context;
    private final URI baseUri;
    private final List<VideoPresentation> videoPresentations;

    public InMemoryVideoProvider(final URI context,
                                 final URI baseUri,
                                 final List<VideoPresentation> videoPresentations) {
        this.context = context;
        this.baseUri = baseUri;
        this.videoPresentations = videoPresentations;
    }

    @Override
    public PagedResult<VideoPresentation> fetchVideoPresentations(int pageSize, int offset) {
        var lastIndexOfNextVideosOnPage = offset + pageSize;
        if (lastIndexOfNextVideosOnPage > videoPresentations.size()) {
            lastIndexOfNextVideosOnPage = videoPresentations.size();
        }
        if (offset > videoPresentations.size()) {
            offset = videoPresentations.size();
        }
        return new PagedResult<>(context,
                                 baseUri,
                                 pageSize,
                                 offset,
                                 videoPresentations.size(),
                                 videoPresentations.subList(offset, lastIndexOfNextVideosOnPage));
    }
}