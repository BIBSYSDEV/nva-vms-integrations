package no.sikt.nva.vms.browser.provider;

public abstract class AbstractVideoProviderException extends Exception {

    public AbstractVideoProviderException(String message) {
        super(message);
    }

    public AbstractVideoProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
