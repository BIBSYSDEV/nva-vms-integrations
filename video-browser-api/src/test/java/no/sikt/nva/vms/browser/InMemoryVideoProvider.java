package no.sikt.nva.vms.browser;

import java.net.URI;
import java.util.List;

public class InMemoryVideoProvider implements VideoProvider{
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
    public PagedResult<VideoPresentation> fetchVideoPresentations(int size, int offset) {
        return new PagedResult<>(context,
                                 baseUri,
                                 size,
                                 offset,
                                 videoPresentations.size(),
                                 videoPresentations.subList(offset, size));
    }
}
