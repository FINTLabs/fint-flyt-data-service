package no.fintlabs.exceptions;

public class NoSuchLinkException extends RuntimeException {

    public NoSuchLinkException(String resourceName, String key) {
        super(String.format("No link with key='%s' in resource=%s", key, resourceName));
    }

}
