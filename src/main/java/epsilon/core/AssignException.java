package epsilon.core;

import epsilon.syntax.Token;

public class AssignException extends InterpretRuntimeException {

    public AssignException(Token token, String message) {
        super(token, message);
    }

}
