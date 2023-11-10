package com.vicarius.elasticsearch.exception;

public final class ElasticsearchOperationException extends RuntimeException {
    public ElasticsearchOperationException(final String message, final Throwable cause) {
        super(message + cause.getMessage(), cause);
    }

    public ElasticsearchOperationException(final String message) {
        super(message);
    }
}
