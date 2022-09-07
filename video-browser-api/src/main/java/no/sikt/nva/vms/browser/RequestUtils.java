package no.sikt.nva.vms.browser;

import static nva.commons.core.attempt.Try.attempt;
import java.net.URI;
import nva.commons.apigateway.RequestInfo;
import nva.commons.core.JacocoGenerated;
import nva.commons.core.paths.UriWrapper;

@JacocoGenerated
public final class RequestUtils {

    private static final String HTTPS = "https";
    private static final String DOMAIN_NAME = "domainName";
    private static final String PATH = "path";

    private RequestUtils() {
    }

    private static String getDomainName(final RequestInfo requestInfo) {
        return attempt(() -> requestInfo.getRequestContext().get(DOMAIN_NAME).asText()).orElseThrow();
    }

    private static String getPath(final RequestInfo requestInfo) {
        return attempt(() -> requestInfo.getRequestContext().get(PATH).asText()).orElseThrow();
    }

    public static URI generateBaseRequestUri(final RequestInfo requestInfo) {
        return new UriWrapper(HTTPS, getDomainName(requestInfo))
                   .addChild(getPath(requestInfo))
                   .getUri();
    }
}
