package no.sikt.nva.vms.browser;

import static java.net.HttpURLConnection.HTTP_OK;
import com.amazonaws.services.lambda.runtime.Context;
import nva.commons.apigateway.ApiGatewayHandler;
import nva.commons.apigateway.RequestInfo;
import nva.commons.core.Environment;

public class VideoBrowserHandler extends ApiGatewayHandler<Void, Void> {

    public VideoBrowserHandler(Environment environment) {
        super(Void.class, environment);
    }

    @Override
    protected Void processInput(Void input, RequestInfo requestInfo, Context context) {
        return null;
    }

    @Override
    protected Integer getSuccessStatusCode(Void input, Void output) {
        return HTTP_OK;
    }
}
