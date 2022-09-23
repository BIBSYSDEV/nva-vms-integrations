package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static nva.commons.core.attempt.Try.attempt;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.Array;
import java.net.URI;
import no.sikt.nva.vms.browser.model.Institution;
import no.sikt.nva.vms.browser.model.Kaltura;
import no.sikt.nva.vms.browser.model.VideoProviderConfig;
import no.sikt.nva.vms.kaltura.KalturaClient;
import no.unit.nva.commons.json.JsonUtils;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.apigateway.exceptions.UnauthorizedException;
import nva.commons.core.Environment;
import nva.commons.core.JacocoGenerated;
import nva.commons.core.paths.UriWrapper;
import nva.commons.secrets.SecretsReader;
import org.jetbrains.annotations.Nullable;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

public class VideoBrowserHandler extends ApiGatewayHandler<Void, PagedResult<VideoPresentation>> {

    public static final String KALTURA_CLIENT_CONFIG_SECRET_NAME = "videoIntegrationConfig";
    public static final String AT_SIGN = "@";
    /* default */ static final String NVA_APPLICATION_DOMAIN_ENV_NAME = "ApiDomain";
    @SuppressWarnings("PMD")
    private static final String NEGATIVE_QUERY_PARAMETERS_EXCEPTION_MESSAGE = "Negative offset and/or size values are"
                                                                              + " not allowed";
    private static final String SIZE = "size";
    private static final String OFFSET = "offset";
    private static final String DEFAULT_SIZE = "10";
    private static final String DEFAULT_OFFSET = "0";
    private static final String HTTPS_SCHEME = UriWrapper.HTTPS + "://";
    public static final String NO_CONFIG_FOR_INSTITUTION_MESSAGE = "NO config for your institution";
    private final URI apiBaseUrl;
    private final SecretsReader secretsReader;

    @JacocoGenerated
    public VideoBrowserHandler() {
        this(new Environment(), SecretsReader.defaultSecretsManagerClient());
    }

    public VideoBrowserHandler(final Environment environment, final SecretsManagerClient secretsManagerClient) {
        super(Void.class, environment);
        this.secretsReader = new SecretsReader(secretsManagerClient);
        this.apiBaseUrl = getBaseUrlFromHost(environment);
    }

    @Override
    protected PagedResult<VideoPresentation> processInput(final Void input, final RequestInfo requestInfo,
                                                          final Context context)
        throws BadRequestException, UnauthorizedException {

        var pageSize = getPageSize(requestInfo);
        var offset = getOffset(requestInfo);
        var username = requestInfo.getUserName();
        validatePageSizeAndOffset(pageSize, offset);

        return getVideoPresentationPagedResult(context, pageSize, offset, username);
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, PagedResult<VideoPresentation> output) {
        return HTTP_OK;
    }

    private void validatePageSizeAndOffset(Integer pageSize, Integer offset) throws BadRequestException {
        if (pageSize < 0 || offset < 0) {
            throw new BadRequestException(NEGATIVE_QUERY_PARAMETERS_EXCEPTION_MESSAGE, HTTP_BAD_REQUEST);
        }
    }

    @Nullable
    private PagedResult<VideoPresentation> getVideoPresentationPagedResult(Context context, Integer pageSize,
                                                                           Integer offset, String username) {
        Institution institution = getInstitution(username);
        if (institution.getKaltura() != null) {
            return attempt(() -> fetchVideoPresentationsWithKalturaProvider(context, pageSize, offset, username,
                                                                            institution.getKaltura())).orElseThrow();
        } else {
            /*TODO: implement Panopto**/
            return null;
        }
    }

    private PagedResult<VideoPresentation> fetchVideoPresentationsWithKalturaProvider(Context context, Integer pageSize,
                                                                                      Integer offset, String username,
                                                                                      Kaltura configForKaltura)
        throws Exception {
        return new KalturaVideoProvider(context.toString(), getKalturaClient(configForKaltura), username,
                                        apiBaseUrl).fetchVideoPresentations(pageSize, offset);
    }

    private KalturaClient getKalturaClient(Kaltura kaltura) {
        return new KalturaClient(kaltura.getKalturaClientServiceUrl(), kaltura.getKalturaClientAdminSecret(),
                                 kaltura.getKalturaClientAdminID(), kaltura.getKalturaClientUserID(),
                                 kaltura.getKalturaClientConnectTimeout(), kaltura.getKalturaClientReadTimeout());
    }

    private String getInstitutionFeideDomainFromEmail(String institutionsEmail) {
        return (String) Array.get(institutionsEmail.split(AT_SIGN), 1);
    }

    private Integer getPageSize(RequestInfo requestInfo) {
        return Integer.parseInt(requestInfo.getQueryParameterOpt(SIZE).orElse(DEFAULT_SIZE));
    }

    private Integer getOffset(RequestInfo requestInfo) {
        return attempt(
            () -> Integer.parseInt(requestInfo.getQueryParameterOpt(OFFSET).orElse(DEFAULT_OFFSET))).orElseThrow();
    }

    private String getVideoIntegrationConfig() {
        return secretsReader.fetchPlainTextSecret(KALTURA_CLIENT_CONFIG_SECRET_NAME);
    }

    private Institution getMatchingInstitutionFromConfig(String config, String institutionId)
        throws JsonProcessingException {
        return JsonUtils.dtoObjectMapper.readValue(config, VideoProviderConfig.class)
                   .getInstitutions()
                   .stream()
                   .filter(institution -> institution.getId().equals(institutionId))
                   .findAny()
                   .orElseThrow();
    }

    private Institution getInstitution(String username) {
        var institutionFeideDomain = getInstitutionFeideDomainFromEmail(username);
        var videoIntegrationConfig = getVideoIntegrationConfig();
        return attempt(
            () -> getMatchingInstitutionFromConfig(videoIntegrationConfig, institutionFeideDomain))
                   .orElseThrow();
    }

    private URI getBaseUrlFromHost(Environment environment) {
        return UriWrapper.fromUri(HTTPS_SCHEME + environment.readEnv(NVA_APPLICATION_DOMAIN_ENV_NAME)).getUri();
    }
}
