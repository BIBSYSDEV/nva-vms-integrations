package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_OK;
import static no.unit.nva.testutils.RandomDataGenerator.randomUri;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import no.unit.nva.commons.json.JsonUtils;
import no.unit.nva.stubs.FakeContext;
import no.unit.nva.testutils.HandlerRequestBuilder;
import nva.commons.apigateway.GatewayResponse;
import nva.commons.core.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VideoBrowserHandlerTest {

    private static final ObjectMapper OBJECT_MAPPER = JsonUtils.dtoObjectMapper;
    private static final String ALLOWED_ORIGIN_ENV_NAME = "ALLOWED_ORIGIN";
    private static final String ALLOWED_ORIGIN_ALL = "*";
    private static final String SIZE_PARAMETER_NAME = "size";
    private static final String OFFSET_PARAMETER_NAME = "offset";
    private static final String DOMAIN_NAME_REQUEST_CONTEXT_PARAMETER_NAME = "domainName";
    private static final String PATH_REQUEST_CONTEXT_PARAMETER_NAME = "path";
    private static final Integer SINGLE_RESULT_SIZE = 1;

    private final Context context = new FakeContext();
    private final Environment environment = mock(Environment.class);

    @BeforeEach
    public void init() {
        when(environment.readEnv(ALLOWED_ORIGIN_ENV_NAME)).thenReturn(ALLOWED_ORIGIN_ALL);
    }

    @Test
    public void shouldHandleSingleResultCorrectly() throws IOException {
        var size = 1;
        var offset = 0;
        var input = createInput(size, offset);
        var output = new ByteArrayOutputStream();

        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext,
                                                                                           baseUri,
                                                                                           Collections.singletonList(
                                                                                               getDefaultVideoPresentation())));
        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));

        var pagedResult = getBodyObject(response.getBody());
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getResults(), iterableWithSize(SINGLE_RESULT_SIZE));
        assertThat(pagedResult.getId().toString(), is(equalTo(getExpectedPagedResultId(baseUri, size, offset))));
        assertThat(pagedResult.getPreviousResults(), nullValue());
        assertThat(pagedResult.getNextResults(), nullValue());
        assertThat(pagedResult.getTotalSize(), is(equalTo(SINGLE_RESULT_SIZE)));
    }

    private String getExpectedPagedResultId(URI baseUri, int size, int offset) {
        return String.format("%s?size=%d&offset=%d", baseUri.toString(), size, offset);
    }

    private InputStream createInput(final int size, final int offset) throws JsonProcessingException {
        final Map<String, String> queryParameters = Map.of(SIZE_PARAMETER_NAME, Integer.toString(size),
                                                           OFFSET_PARAMETER_NAME, Integer.toString(offset));
        return new HandlerRequestBuilder<Void>(OBJECT_MAPPER)
                   .withRequestContextValue(DOMAIN_NAME_REQUEST_CONTEXT_PARAMETER_NAME, "api.nva.unit.no")
                   .withRequestContextValue(PATH_REQUEST_CONTEXT_PARAMETER_NAME, "/vms/presentations")
                   .withQueryParameters(queryParameters)
                   .build();
    }

    private PagedResult<VideoPresentation> getBodyObject(final String body) throws JsonProcessingException {
        final JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(PagedResult.class,
                                                                                     VideoPresentation.class);
        return OBJECT_MAPPER.readValue(body, type);
    }

    private static VideoPresentation getDefaultVideoPresentation() {
        return new VideoPresentation.Builder()
                   .withId("id")
                   .withDescription("description")
                   .withTitle("title")
                   .withTimeRecorded("timeRecorded")
                   .withPresenter("presenter")
                   .withDownloadUrl("dowloadUrl")
                   .withStreamingUrl(("streamingUrl"))
                   .withThumbnailUrl("thumbnailUrl")
                   .withContentIdentifier("contentIdentifier")
                   .build();
    }
}
