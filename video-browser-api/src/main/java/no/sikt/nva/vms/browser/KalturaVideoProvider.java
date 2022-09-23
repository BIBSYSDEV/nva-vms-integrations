package no.sikt.nva.vms.browser;

import com.kaltura.client.types.MediaEntry;
import java.net.URI;
import java.util.stream.Collectors;
import no.sikt.nva.vms.kaltura.KalturaClient;

public class KalturaVideoProvider implements VideoProvider {

    private final String context;
    private final URI baseUri;
    private final KalturaClient kalturaClient;
    private final String username;

    public KalturaVideoProvider(String context, KalturaClient kalturaClient, String username, URI baseUri) {
        this.context = context;
        this.kalturaClient = kalturaClient;
        this.username = username;
        this.baseUri = baseUri;
    }

    @SuppressWarnings("PMD")
    @Override
    public PagedResult<VideoPresentation> fetchVideoPresentations(int size, int offset) throws Exception {
        var mediaEntries = kalturaClient.getMediaEntries(username);
        var presentations = mediaEntries.mediaEntries.stream()
                                .map(this::convertMediaEntryToVideoPresentation)
                                .collect(Collectors.toList());

        var lastIndexOfVideoOnThisPage = offset + size;
        if (lastIndexOfVideoOnThisPage > presentations.size()) {
            lastIndexOfVideoOnThisPage = presentations.size();
        }
        if (offset > presentations.size()) {
            offset = 0;
        }
        return new PagedResult<>(context, baseUri, size, offset, presentations.size(),
                                 presentations.subList(offset, lastIndexOfVideoOnThisPage));
    }

    private VideoPresentation convertMediaEntryToVideoPresentation(MediaEntry mediaEntry) {
        return new VideoPresentation(mediaEntry.getId(), mediaEntry.getName(), mediaEntry.getDescription(),
                                     mediaEntry.getDuration(), mediaEntry.getUserId(), mediaEntry.getDownloadUrl(),
                                     mediaEntry.getDataUrl(), mediaEntry.getThumbnailUrl(), mediaEntry.getId());
    }
}
