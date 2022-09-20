package no.sikt.nva.vms.browser;

import java.util.List;

public interface VideoProvider {
    PagedResult<VideoPresentation> fetchVideoPresentations(int size, int offset);

    List<VideoPresentation> fetchAllVideos();

}
