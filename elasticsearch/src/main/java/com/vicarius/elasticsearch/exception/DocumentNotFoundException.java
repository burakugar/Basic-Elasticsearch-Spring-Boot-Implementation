package com.vicarius.elasticsearch.exception;

public final class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(final String message) {
        super(message);
    }
}
