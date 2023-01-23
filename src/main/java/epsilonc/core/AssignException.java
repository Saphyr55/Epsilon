package epsilonc.core;

import epsilonc.Token;

public class AssignException extends InterpretRuntimeException {

    public AssignException(Token token, String message) {
        super(token, message);
    }

}
