package no.sikt.nva.vms.browser;

import com.kaltura.client.types.MediaEntry;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import no.sikt.nva.vms.kaltura.KalturaClient;
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
    public PagedResult<VideoPresentation> fetchVideoPresentations(int size, int offset) throws Exception {
        var presentations = getPresentations();
        validateOffset(offset, presentations);
        int lastIndexOfVideoOnThisPage = calculateLastIndexOfVideoOnThisPage(size, offset, presentations);

        return new PagedResult<>(context, baseUri, size, offset, presentations.size(),
                                 presentations.subList(offset, lastIndexOfVideoOnThisPage));
    }

    private void validateOffset(final int offset, List<VideoPresentation> presentations) throws BadRequestException {
        if (offset > presentations.size()) {
            throw new BadRequestException(OFFSET_OUT_OF_RANGE, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    private int calculateLastIndexOfVideoOnThisPage(int size, int offset, List<VideoPresentation> presentations) {
        return Math.min(offset + size, presentations.size());
    }

    private List<VideoPresentation> getPresentations() throws Exception {
        return getMediaEntries().mediaEntries.stream()
                   .map(this::convertMediaEntryToVideoPresentation)
                   .collect(Collectors.toList());
    }

    private MediaEntriesResult getMediaEntries() throws Exception {
        return kalturaClient.getMediaEntries(username);
    }

    private VideoPresentation convertMediaEntryToVideoPresentation(MediaEntry mediaEntry) {
        return new VideoPresentation(mediaEntry.getId(), mediaEntry.getName(), mediaEntry.getDescription(),
                                     mediaEntry.getDuration(), mediaEntry.getUserId(), mediaEntry.getDownloadUrl(),
                                     mediaEntry.getDataUrl(), mediaEntry.getThumbnailUrl(), mediaEntry.getId());
    }
}
