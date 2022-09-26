package no.sikt.nva.vms.browser.provider;

import com.kaltura.client.types.MediaEntry;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import no.sikt.nva.vms.browser.PagedResult;
import no.sikt.nva.vms.browser.VideoPresentation;
import no.sikt.nva.vms.browser.VideoProvider;
import no.sikt.nva.vms.kaltura.KalturaClient;
import no.sikt.nva.vms.kaltura.KalturaException;
import no.sikt.nva.vms.kaltura.MediaEntriesResult;

public class KalturaVideoProvider implements VideoProvider {

    public static final String OFFSET_OUT_OF_RANGE = "Offset out of range";
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

    @Override
    public PagedResult<VideoPresentation> fetchVideoPresentations(int size, int offset)
        throws ProviderFailedException, IllegalInputException {
        var presentations = fetchPresentations();
        validateOffset(offset, presentations);
        int lastIndexOfVideoOnThisPage = calculateLastIndexOfVideoOnThisPage(size, offset, presentations);

        return new PagedResult<>(context, baseUri, size, offset, presentations.size(),
                                 presentations.subList(offset, lastIndexOfVideoOnThisPage));
    }

    private void validateOffset(final int offset, List<VideoPresentation> presentations) throws IllegalInputException {
        if (offset > presentations.size()) {
            throw new IllegalInputException(OFFSET_OUT_OF_RANGE);
        }
    }

    private int calculateLastIndexOfVideoOnThisPage(int size, int offset, List<VideoPresentation> presentations) {
        return Math.min(offset + size, presentations.size());
    }

    private List<VideoPresentation> fetchPresentations() throws ProviderFailedException {
        return getMediaEntries().mediaEntries.stream()
                   .map(this::convertMediaEntryToVideoPresentation)
                   .collect(Collectors.toList());
    }

    private MediaEntriesResult getMediaEntries() throws ProviderFailedException {
        try {
            return kalturaClient.getMediaEntries(username);
        } catch (KalturaException e) {
            throw new ProviderFailedException("Problems with provider", e);
        }
    }

    private VideoPresentation convertMediaEntryToVideoPresentation(MediaEntry mediaEntry) {
        return new VideoPresentation(mediaEntry.getId(), mediaEntry.getName(), mediaEntry.getDescription(),
                                     mediaEntry.getDuration(), mediaEntry.getUserId(), mediaEntry.getDownloadUrl(),
                                     mediaEntry.getDataUrl(), mediaEntry.getThumbnailUrl(), mediaEntry.getId());
    }
}
