package no.sikt.nva.vms.browser.provider;

public class ProviderFailedException extends  AbstractVideoProviderException{

    public ProviderFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
