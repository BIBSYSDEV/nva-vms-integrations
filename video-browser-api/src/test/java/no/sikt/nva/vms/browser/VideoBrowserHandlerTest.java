package no.sikt.nva.vms.browser;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static no.sikt.nva.vms.browser.VideoBrowserHandler.NVA_APPLICATION_DOMAIN_ENV_NAME;
import static no.sikt.nva.vms.kaltura.KalturaMock.KALTURA_GET_ENTRIES_PATH;
import static no.sikt.nva.vms.util.ExpectedResultTestClass.NEXT_RESULTS_WITH_OFFSET_10;
import static no.sikt.nva.vms.util.ExpectedResultTestClass.PREVIOUS_RESULTS_WITH_OFFSET_10;
import static no.sikt.nva.vms.util.ExpectedResultTestClass.VIDEO_PRESENTATION;
import static no.unit.nva.testutils.RandomDataGenerator.randomString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import no.sikt.nva.vms.util.ExpectedResultTestClass;
import no.unit.nva.commons.json.JsonUtils;
import no.unit.nva.stubs.FakeSecretsManagerClient;
import no.unit.nva.testutils.HandlerRequestBuilder;
import nva.commons.apigateway.GatewayResponse;
import nva.commons.core.Environment;
import nva.commons.core.ioutils.IoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest
public class VideoBrowserHandlerTest {

    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_OFFSET = "0";
    public static final String TEST_USER_EMAIL_FOR_KALTURA = "epost@kaltura.no";
    public static final String TEST_USER_EMAIL_FOR_PANOPTO = "epost@panopto.no";
    public static final String VIDEO_INTEGRATION_CONFIG_SECRET = "videoIntegrationConfig";
    public static final String VMS_PRESENTATIONS_PATH = "/vms/presentations";
    public static final String PAGE_SIZE_PARAM = "pageSize";
    public static final String PAGE_OFFSET_PARAM = "offset";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String KALTURA_SERVICE_URL_PLACEHOLDER = "@@KALTURA_SERVICE_URL@@";
    public static final String SIZE_PARAMETER_NAME = "size";
    public static final String OFFSET_PARAMETER_NAME = "offset";
    public static final String API_HOST_VALUE = "api.sandbox.nva.aws.unit.no";
    public static final String OFFSET_10 = "10";
    public static final String OFFSET_20 = "20";
    private static final ObjectMapper OBJECT_MAPPER = JsonUtils.dtoObjectMapper;
    private static final String ALLOWED_ORIGIN_ENV_NAME = "ALLOWED_ORIGIN";
    private static final String ALLOWED_ORIGIN_ALL = "*";
    private static final String DOMAIN_NAME_REQUEST_CONTEXT_PARAMETER_NAME = "domainName";
    private static final String PATH_REQUEST_CONTEXT_PARAMETER_NAME = "path";
    private static final String APPLICATION_JSON_CONTENT_TYPE_VALUE = "application/json";
    private static final String OFFSET_30 = "30";
    private final Environment environment = mock(Environment.class);
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private Context context;
    private FakeSecretsManagerClient fakeSecretsManagerClient;

    @BeforeEach
    public void init(final WireMockRuntimeInfo wmRuntimeInfo) {
        this.fakeSecretsManagerClient = new FakeSecretsManagerClient();
        when(environment.readEnv(NVA_APPLICATION_DOMAIN_ENV_NAME)).thenReturn(API_HOST_VALUE);
        var videoIntegrationConfig = IoUtils.stringFromResources(Path.of("videoIntegrationConfig.json"));
        var config = videoIntegrationConfig.replace(KALTURA_SERVICE_URL_PLACEHOLDER, wmRuntimeInfo.getHttpBaseUrl());

        fakeSecretsManagerClient.putPlainTextSecret(VIDEO_INTEGRATION_CONFIG_SECRET, config);
        when(environment.readEnv(ALLOWED_ORIGIN_ENV_NAME)).thenReturn(ALLOWED_ORIGIN_ALL);

        context = mock(Context.class);
    }

    @Test
    void shouldReturnPagedResultForKalturaWithCorrectResultsSizeAndNextResult() throws IOException {
        constructTest(DEFAULT_PAGE_SIZE, DEFAULT_OFFSET, TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "videoPresentationsResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var actualPagedResult = getBodyObject(response.getBody());

        assertThat(actualPagedResult.getTotalSize(), is(equalTo(ExpectedResultTestClass.TOTAL_SIZE)));
        assertThat(actualPagedResult.getNextResults(), is(equalTo(ExpectedResultTestClass.NEXT_RESULTS_WITH_OFFSET_0)));
        assertThat(actualPagedResult.getPreviousResults(), is(nullValue()));
    }

    @Test
    void shouldReturnPagedResultForKalturaWithCorrectResultsSizeAndPreviousResult() throws IOException {
        constructTest(DEFAULT_PAGE_SIZE, OFFSET_10, TEST_USER_EMAIL_FOR_KALTURA, OFFSET_10,
                      "videoPresentationsResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var actualPagedResult = getBodyObject(response.getBody());

        assertThat(actualPagedResult.getTotalSize(), is(equalTo(ExpectedResultTestClass.TOTAL_SIZE)));
        assertThat(actualPagedResult.getNextResults(), is(equalTo(NEXT_RESULTS_WITH_OFFSET_10)));
        assertThat(actualPagedResult.getPreviousResults(), is(equalTo(PREVIOUS_RESULTS_WITH_OFFSET_10)));
    }

    @Test
    void shouldReturnPagedResultWithCorrectVideoPresentation() throws IOException {
        constructTest(DEFAULT_PAGE_SIZE, DEFAULT_OFFSET, TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "singleVideoPresentationResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var actualPagedResult = getBodyObject(response.getBody());
        var actualVideoPresentation = actualPagedResult.getResults().get(0);

        assertThat(actualPagedResult.getTotalSize(), is(equalTo(1)));
        assertThat(actualPagedResult.getNextResults(), is(nullValue()));
        assertThat(actualVideoPresentation, is(equalTo(VIDEO_PRESENTATION)));
    }

    @Test
    void shouldReturnNullForNextResultsWhenSumOfOffsetAndPageSizeIsLargerThanTotalSize() throws IOException {
        constructTest(DEFAULT_PAGE_SIZE, OFFSET_20, TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "videoPresentationsResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        var actualPagedResult = getBodyObject(response.getBody());

        assertThat(actualPagedResult.getNextResults(), is(nullValue()));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenOffsetIsLargerThanVideoPresentationsSize() throws IOException {
        constructTest(DEFAULT_PAGE_SIZE, OFFSET_30, TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "videoPresentationsResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);
        assertThat(response.getStatusCode(), is(equalTo(HTTP_BAD_REQUEST)));
    }

    @Test
    void shouldReturnBadRequestIfPageSizeIsNegative() throws IOException {
        constructTest("-10", "0", TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "singleVideoPresentationResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);

        assertThat(response.getStatusCode(), is(equalTo(HTTP_BAD_REQUEST)));
    }

    @Test
    void shouldReturnBadRequestIfOffsetIsNegative() throws IOException {
        constructTest("10", "-10", TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "singleVideoPresentationResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);

        assertThat(response.getStatusCode(), is(equalTo(HTTP_BAD_REQUEST)));
    }

    @Test
    void shouldThrowKalturaProviderExceptionIfConfigIsNotCorrect() throws IOException {
        var videoIntegrationConfig = IoUtils.stringFromResources(Path.of("videoIntegrationConfig.json"));
        var config = videoIntegrationConfig.replace(KALTURA_SERVICE_URL_PLACEHOLDER, randomString());

        fakeSecretsManagerClient.putPlainTextSecret(VIDEO_INTEGRATION_CONFIG_SECRET, config);
        constructTest("10", "0", TEST_USER_EMAIL_FOR_KALTURA, DEFAULT_OFFSET,
                      "singleVideoPresentationResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);

        assertThat(response.getStatusCode(), is(equalTo(500)));
    }

    @Test
    void temporaryTestWhilePanoptoFunctionalityIsNotImplemented() throws IOException {
        constructTest(DEFAULT_PAGE_SIZE, DEFAULT_OFFSET, TEST_USER_EMAIL_FOR_PANOPTO, DEFAULT_OFFSET,
                      "singleVideoPresentationResponse.json");
        var response = GatewayResponse.fromOutputStream(output, PagedResult.class);

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
    }

    private void constructTest(String defaultPageSize, String offset20, String testUserEmailForKaltura,
                               String defaultOffset, String file) throws IOException {
        var input = createRequest(defaultPageSize, offset20, testUserEmailForKaltura);
        stubRequestForVideoPresentations(defaultOffset, testUserEmailForKaltura);
        stubRequestForFetchingEntriesFromKaltura(jsonToString(file));
        VideoBrowserHandler handler = createVideoBrowserHandler();
        handler.handleRequest(input, output, context);
    }

    private String jsonToString(String file) {
        return IoUtils.stringFromResources(Path.of(file));
    }

    private VideoBrowserHandler createVideoBrowserHandler() {
        return new VideoBrowserHandler(environment, fakeSecretsManagerClient);
    }

    private PagedResult<VideoPresentation> getBodyObject(final String body) throws JsonProcessingException {
        final JavaType type = OBJECT_MAPPER.getTypeFactory()
                                  .constructParametricType(PagedResult.class, VideoPresentation.class);
        return OBJECT_MAPPER.readValue(body, type);
    }

    private InputStream createRequest(final String pageSize, final String offset, String feideId)
        throws JsonProcessingException {
        final Map<String, String> queryParameters = Map.of(SIZE_PARAMETER_NAME, pageSize, OFFSET_PARAMETER_NAME,
                                                           offset);
        return new HandlerRequestBuilder<Void>(OBJECT_MAPPER).withRequestContextValue(
                DOMAIN_NAME_REQUEST_CONTEXT_PARAMETER_NAME, "api.nva.unit.no")
                   .withRequestContextValue(PATH_REQUEST_CONTEXT_PARAMETER_NAME, VMS_PRESENTATIONS_PATH)
                   .withQueryParameters(queryParameters)
                   .withFeideId(feideId)
                   .build();
    }

    private void stubRequestForVideoPresentations(String offset, String responseBody) {
        stubFor(get(urlPathEqualTo(VMS_PRESENTATIONS_PATH))
                    .withQueryParam(PAGE_SIZE_PARAM, WireMock.equalTo(DEFAULT_PAGE_SIZE))
                    .withQueryParam(PAGE_OFFSET_PARAM, WireMock.equalTo(offset))
                    .willReturn(ok().withHeader(CONTENT_TYPE, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                                    .withBody(responseBody)));
    }

    private void stubRequestForFetchingEntriesFromKaltura(String responseBody) {
        stubFor(post(urlPathEqualTo(KALTURA_GET_ENTRIES_PATH))
                    .withHeader("Accept-Charset", WireMock.equalTo("utf-8,ISO-8859-1;q=0.7,*;q=0.5"))
                    .withHeader("Accept", WireMock.equalTo("application/json"))
                    .willReturn(ok().withBody(responseBody)));
    }
}
