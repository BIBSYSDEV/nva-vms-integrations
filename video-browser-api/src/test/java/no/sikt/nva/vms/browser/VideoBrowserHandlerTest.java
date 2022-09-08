package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static no.unit.nva.testutils.RandomDataGenerator.randomInteger;
import static no.unit.nva.testutils.RandomDataGenerator.randomString;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import no.unit.nva.commons.json.JsonUtils;
import no.unit.nva.stubs.FakeContext;
import no.unit.nva.testutils.HandlerRequestBuilder;
import nva.commons.apigateway.GatewayResponse;
import nva.commons.core.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VideoBrowserHandlerTest {

    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_OFFSET = "0";
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
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           Collections.singletonList(
                                                                                               getDefaultVideoPresentation())));
        var size = 1;
        var offset = 0;
        var input = createInput(String.valueOf(size), String.valueOf(offset));
        var output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var pagedResult = getBodyObject(response.getBody());

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getResults(), iterableWithSize(SINGLE_RESULT_SIZE));
        assertThat(pagedResult.getId().toString(), is(equalTo(getExpectedPagedResultId(baseUri, size, offset))));
        assertThat(pagedResult.getPreviousResults(), nullValue());
        assertThat(pagedResult.getNextResults(), nullValue());
        assertThat(pagedResult.getTotalSize(), is(equalTo(SINGLE_RESULT_SIZE)));
    }

    @Test
    public void shouldHandleMultipleResultsWhenNumberOfVideosIsLargerThanPageSize() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(20);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var size = 10;
        var offset = randomInteger(videoPresentations.size());
        var input = createInput(String.valueOf(size), String.valueOf(offset));
        var output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var pagedResult = getBodyObject(response.getBody());

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getId().toString(), is(equalTo(getExpectedPagedResultId(baseUri, size, offset))));
        assertThat(pagedResult.getTotalSize(), is(equalTo(videoPresentations.size())));
    }

    @Test
    public void shouldHandleCustomPageSize() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(240);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var size = 25;
        var offset = randomInteger(videoPresentations.size());
        var input = createInput(String.valueOf(size), String.valueOf(offset));
        var output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var pagedResult = getBodyObject(response.getBody());

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getId().toString(), is(equalTo(getExpectedPagedResultId(baseUri, size, offset))));
        assertThat(pagedResult.getTotalSize(), is(equalTo(videoPresentations.size())));
    }

    @Test
    public void shouldReturnNullForPreviousResultsWhenOffsetIsZero() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(20);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var size = 10;
        var offset = 0;
        var input = createInput(String.valueOf(size), String.valueOf(offset));
        var output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var pagedResult = getBodyObject(response.getBody());

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getId().toString(), is(equalTo(getExpectedPagedResultId(baseUri, size, offset))));
        assertThat(pagedResult.getTotalSize(), is(equalTo(videoPresentations.size())));
        assertThat(pagedResult.getPreviousResults(), is(equalTo(null)));
    }

    @Test
    public void shouldReturnNullForNextResultsWhenSumOfOffsetAndPageSizeIsLargerThanTotalNumberOfVideos()
        throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(20);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var size = 10;
        var offset = Math.round(videoPresentations.size() / 10) * 10;
        var input = createInput(String.valueOf(size), String.valueOf(offset));
        var output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var pagedResult = getBodyObject(response.getBody());

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getId().toString(), is(equalTo(getExpectedPagedResultId(baseUri, size, offset))));
        assertThat(pagedResult.getTotalSize(), is(equalTo(videoPresentations.size())));
        assertThat(pagedResult.getNextResults(), is(equalTo(null)));
    }

    @Test
    public void shouldUseDefaultPageSizeAndOffsetValuesIfMissing() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(20);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var input = new HandlerRequestBuilder<Void>(OBJECT_MAPPER).build();
        var output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var pagedResult = getBodyObject(response.getBody());

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getId().toString(), is(equalTo(
            getExpectedPagedResultId(baseUri, Integer.parseInt(DEFAULT_PAGE_SIZE), Integer.parseInt(DEFAULT_OFFSET)))));
        assertThat(pagedResult.getTotalSize(), is(equalTo(videoPresentations.size())));
    }

    @Test
    public void shouldHandleEmptyResult() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = new ArrayList<VideoPresentation>();
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var input = createInput(DEFAULT_PAGE_SIZE, DEFAULT_OFFSET);
        var output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
        var pagedResult = getBodyObject(response.getBody());
        assertThat(pagedResult.getContext(), is(equalTo(pageResultContext)));
        assertThat(pagedResult.getTotalSize(), is(equalTo(videoPresentations.size())));
    }

    /**
     * Longs are handled by VideoBrowserHandler when parsing.
     */
    @Test
    public void shouldThrowExceptionIfNegativePageSizeOrOffsetValues() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(20);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var size = -5;
        var offset = -1;
        var input = createInput(String.valueOf(size), String.valueOf(offset));
        var output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        assertThat(response.getStatusCode(), is(equalTo(HTTP_BAD_REQUEST)));
    }

    @Test
    public void shouldThrowExceptionIfInvalidPageSizeOrOffsetValues() throws IOException {
        var pageResultContext = randomUri();
        var baseUri = randomUri();
        var videoPresentations = generateRandomVideoListWithMinSize(20);
        var handler = new VideoBrowserHandler(environment, () -> new InMemoryVideoProvider(pageResultContext, baseUri,
                                                                                           videoPresentations));
        var size = "someSize";
        var offset = "someOffset";
        var input = createInput(size, offset);
        var output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        assertThat(response.getStatusCode(), is(equalTo(HTTP_BAD_REQUEST)));
    }

    private static VideoPresentation getDefaultVideoPresentation() {
        return new VideoPresentation.Builder().withId(randomString())
                   .withDescription(randomString())
                   .withTitle(randomString())
                   .withTimeRecorded(randomString())
                   .withPresenter(randomString())
                   .withDownloadUrl(randomString())
                   .withStreamingUrl(randomString())
                   .withThumbnailUrl(randomString())
                   .withContentIdentifier(randomString())
                   .build();
    }

    private static List<VideoPresentation> generateRandomVideoListWithMinSize(int minValue) {
        return IntStream.range(0, minValue + randomInteger(50))
                   .boxed()
                   .map(i -> getDefaultVideoPresentation())
                   .collect(Collectors.toList());
    }

    private String getExpectedPagedResultId(URI baseUri, int size, int offset) {
        return String.format("%s?size=%d&offset=%d", baseUri.toString(), size, offset);
    }

    private InputStream createInput(final String size, final String offset) throws JsonProcessingException {
        final Map<String, String> queryParameters = Map.of(SIZE_PARAMETER_NAME, size, OFFSET_PARAMETER_NAME, offset);
        return new HandlerRequestBuilder<Void>(OBJECT_MAPPER).withRequestContextValue(
                DOMAIN_NAME_REQUEST_CONTEXT_PARAMETER_NAME, "api.nva.unit.no")
                   .withRequestContextValue(PATH_REQUEST_CONTEXT_PARAMETER_NAME, "/vms/presentations")
                   .withQueryParameters(queryParameters)
                   .build();
    }

    private PagedResult<VideoPresentation> getBodyObject(final String body) throws JsonProcessingException {
        final JavaType type = OBJECT_MAPPER.getTypeFactory()
                                  .constructParametricType(PagedResult.class, VideoPresentation.class);
        return OBJECT_MAPPER.readValue(body, type);
    }
}
