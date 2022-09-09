package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static nva.commons.core.attempt.Try.attempt;
import com.amazonaws.services.lambda.runtime.Context;
import java.net.URI;
import java.util.function.Supplier;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import nva.commons.core.paths.UriWrapper;

public class VideoBrowserHandler extends ApiGatewayHandler<Void, PagedResult<VideoPresentation>> {

    @SuppressWarnings("PMD")
    private static final URI DEFAULT_BROWSE_CONTEXT = URI.create("https://api.nva.unit.no/vms/presentations/browse");
    private static final String NEGATIVE_QUERY_PARAMETERS_EXCEPTION_MESSAGE = "Negative offset and/or size values are"
                                                                              + " not allowed";
    private static final String INVALID_QUERY_PARAMETERS_EXCEPTION_MESSAGE = "Invalid offset and/or size values";
    private static final String SIZE = "size";
    private static final String OFFSET = "offset";
    private static final String DEFAULT_SIZE = "10";
    private static final String DEFAULT_OFFSET = "0";

    private final Supplier<VideoProvider> videoProvider;

    //    @JacocoGenerated
    //    public VideoBrowserHandler() {
    //        this(new Environment(), () -> null);
    //    }

    public VideoBrowserHandler(final Environment environment, final Supplier<VideoProvider> videoProvider) {
        super(Void.class, environment);
        this.videoProvider = videoProvider;
    }

    @Override
    protected PagedResult<VideoPresentation> processInput(final Void input, final RequestInfo requestInfo,
                                                          final Context context)
        throws BadRequestException {

        var pageSize = attempt(
            () -> Integer.parseInt(requestInfo.getQueryParameterOpt(SIZE).orElse(DEFAULT_SIZE)))
                           .orElseThrow(exception -> new BadRequestException(INVALID_QUERY_PARAMETERS_EXCEPTION_MESSAGE,
                                                                             HTTP_BAD_REQUEST));

        var offset = attempt(
            () -> Integer.parseInt(requestInfo.getQueryParameterOpt(OFFSET).orElse(DEFAULT_OFFSET))).orElseThrow();

        if (pageSize < 0 || offset < 0) {
            throw new BadRequestException(NEGATIVE_QUERY_PARAMETERS_EXCEPTION_MESSAGE, HTTP_BAD_REQUEST);
        }

        return videoProvider.get().fetchVideoPresentations(pageSize, offset);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, PagedResult<VideoPresentation> output) {
        return HTTP_OK;
    }

    @SuppressWarnings("PMD")
    @JacocoGenerated
    private static URI generatePreviousResults() {
        // TODO: will be implemented when we actually start returning results.
        return null;
    }

    @SuppressWarnings("PMD")
    @JacocoGenerated
    private static URI generateNextResults() {
        // TODO: will be implemented when we actually start returning results.
        return null;
    }

    @SuppressWarnings("PMD")
    @JacocoGenerated
    private URI generateId(final URI baseRequestUri, final int size, final int offset) {
        return UriWrapper.fromUri(baseRequestUri)
                   .addQueryParameter(SIZE, Integer.toString(size))
                   .addQueryParameter(OFFSET, Integer.toString(offset))
                   .getUri();
    }
}
