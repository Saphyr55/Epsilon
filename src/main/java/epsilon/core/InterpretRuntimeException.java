package epsilon.core;

import epsilon.syntax.Token;

public class InterpretRuntimeException extends RuntimeException {

    public final Token token;

    public InterpretRuntimeException(Token token, String message) {
        super(message);
        this.token = token;
    }


}
