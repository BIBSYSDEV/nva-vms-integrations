package no.sikt.nva.vms.browser;

import nva.commons.apigateway.exceptions.ApiGatewayException;

public class BadRequestException extends ApiGatewayException {

    private final Integer responseStatusCode;

    public BadRequestException(String message, Integer responseStatusCode) {
        super(message);
        this.responseStatusCode = responseStatusCode;
    }

    @Override
    protected Integer statusCode() {
        return responseStatusCode;
    }
}
