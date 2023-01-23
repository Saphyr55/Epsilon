package epsilonc.core;

import epsilonc.Token;

public class DeclarationException extends InterpretRuntimeException {

    public DeclarationException(Token token, String message) {
        super(token, message);
    }
}
