package no.fintlabs.kafka.util;

import java.util.function.Consumer;

public class RequestReyplyAsyncOperationArgs<T> extends RequestReplyOperationArgs<T> {

    public final Consumer<T> successCallback;
    public final Consumer<Throwable> failureCallback;

    public RequestReyplyAsyncOperationArgs(RequestReplyOperationArgs<T> requestReplyOperationArgs, Consumer<T> successCallback, Consumer<Throwable> failureCallback) {
        super(requestReplyOperationArgs);
        this.successCallback = successCallback;
        this.failureCallback = failureCallback;
    }

}
