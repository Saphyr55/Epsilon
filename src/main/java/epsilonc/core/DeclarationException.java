package epsilonc.core;

import epsilonc.syntax.Token;

public class DeclarationException extends InterpretRuntimeException {

    public DeclarationException(Token token, String message) {
        super(token, message);
    }
}
