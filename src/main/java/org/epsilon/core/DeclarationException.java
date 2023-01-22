package org.epsilon.core;

import org.epsilon.Token;

public class DeclarationException extends InterpretRuntimeException {

    public DeclarationException(Token token, String message) {
        super(token, message);
    }
}
