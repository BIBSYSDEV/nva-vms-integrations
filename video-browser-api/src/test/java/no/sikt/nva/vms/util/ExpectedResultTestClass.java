package no.sikt.nva.vms.util;

import static no.sikt.nva.vms.browser.VideoBrowserHandlerTest.API_HOST_VALUE;
import static no.sikt.nva.vms.browser.VideoBrowserHandlerTest.DEFAULT_PAGE_SIZE;
import static no.sikt.nva.vms.browser.VideoBrowserHandlerTest.OFFSET_PARAMETER_NAME;
import static no.sikt.nva.vms.browser.VideoBrowserHandlerTest.SIZE_PARAMETER_NAME;
import java.net.URI;
import no.sikt.nva.vms.browser.VideoPresentation;
import nva.commons.core.paths.UriWrapper;

public class ExpectedResultTestClass {

    public static final int TOTAL_SIZE = 22;
    public static final URI NEXT_RESULTS_WITH_OFFSET_0 = UriWrapper.fromHost(API_HOST_VALUE)
                                                             .addQueryParameter(SIZE_PARAMETER_NAME, DEFAULT_PAGE_SIZE)
                                                             .addQueryParameter(OFFSET_PARAMETER_NAME, "10")
                                                             .getUri();

    public static final URI NEXT_RESULTS_WITH_OFFSET_10 = UriWrapper.fromHost(API_HOST_VALUE)
                                                              .addQueryParameter(SIZE_PARAMETER_NAME, DEFAULT_PAGE_SIZE)
                                                              .addQueryParameter(OFFSET_PARAMETER_NAME, "20")
                                                              .getUri();
    public static final URI PREVIOUS_RESULTS_WITH_OFFSET_10 = UriWrapper.fromHost(API_HOST_VALUE)
                                                                  .addQueryParameter(SIZE_PARAMETER_NAME,
                                                                                     DEFAULT_PAGE_SIZE)
                                                                  .addQueryParameter(OFFSET_PARAMETER_NAME, "0")
                                                                  .getUri();
    public static final VideoPresentation VIDEO_PRESENTATION = new VideoPresentation("asdadad", "asdasd", null, 5,
                                                                                     "pcb", "https:someUrl",
                                                                                     "https" + ":some", "https:someUrl",
                                                                                     "asdadad");

}
