package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_OK;
import static no.sikt.nva.vms.browser.RequestUtils.generateBaseRequestUri;
import com.amazonaws.services.lambda.runtime.Context;
import java.net.URI;
import java.util.function.Supplier;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import nva.commons.core.paths.UriWrapper;

public class VideoBrowserHandler extends ApiGatewayHandler<Void, PagedResult<VideoPresentation>> {

    private static final URI DEFAULT_BROWSE_CONTEXT = URI.create("https://api.nva.unit.no/vms/presentations/browse");
    private static final String SIZE = "size";
    private static final String OFFSET = "offset";
    private static final String DEFAULT_SIZE = "10";
    private static final String DEFAULT_OFFSET = "0";

    private final Supplier<VideoProvider> videoProvider;

    @JacocoGenerated
    public VideoBrowserHandler() {
        this(new Environment(), () -> null);
    }
    public VideoBrowserHandler(final Environment environment, final Supplier<VideoProvider> videoProvider) {
        super(Void.class, environment);
        this.videoProvider = videoProvider;
    }

    @Override
    protected PagedResult<VideoPresentation> processInput(final Void input,
                                                          final RequestInfo requestInfo,
                                                          final Context context) {
        var size = Integer.parseInt(requestInfo.getQueryParameterOpt(SIZE).orElse(DEFAULT_SIZE));
        var offset = Integer.parseInt(requestInfo.getQueryParameterOpt(OFFSET).orElse(DEFAULT_OFFSET));

        var baseRequestUri = generateBaseRequestUri(requestInfo);

        return videoProvider.get().fetchVideoPresentations(size, offset);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, PagedResult<VideoPresentation> output) {
        return HTTP_OK;
    }

    private static URI generatePreviousResults() {
        // TODO: will be implemented when we actually start returning results.
        return null;
    }

    private static URI generateNextResults() {
        // TODO: will be implemented when we actually start returning results.
        return null;
    }

    private URI generateId(final URI baseRequestUri, final int size, final int offset) {
        return UriWrapper.fromUri(baseRequestUri)
                   .addQueryParameter(SIZE, Integer.toString(size))
                   .addQueryParameter(OFFSET, Integer.toString(offset))
                   .getUri();
    }
}
