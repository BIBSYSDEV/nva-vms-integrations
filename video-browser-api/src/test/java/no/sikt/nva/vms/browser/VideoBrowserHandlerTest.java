package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    private final Context context = new FakeContext();
    private final Environment environment = mock(Environment.class);

    @BeforeEach
    public void init() {
        when(environment.readEnv(ALLOWED_ORIGIN_ENV_NAME)).thenReturn(ALLOWED_ORIGIN_ALL);
    }

    @Test
    public void placeholder() throws IOException {
        var handler = new VideoBrowserHandler(environment);
        var input = new HandlerRequestBuilder<Void>(OBJECT_MAPPER).build();
        var output = new ByteArrayOutputStream();
        handler.handleRequest(input, output, context);

        var response = GatewayResponse.fromOutputStream(output, Void.class);

        assertThat(response.getStatusCode(), is(equalTo(HTTP_OK)));
    }
}
