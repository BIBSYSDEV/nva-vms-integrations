package no.sikt.nva.vms.browser;

public interface VideoProvider {

    PagedResult<VideoPresentation> fetchVideoPresentations(int size, int offset) throws Exception;
}
