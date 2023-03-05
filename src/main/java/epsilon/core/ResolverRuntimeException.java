package epsilon.core;

import epsilon.Parser;
import epsilon.syntax.Token;

public class ResolverRuntimeException extends RuntimeException {

    public ResolverRuntimeException() { }

    public ResolverRuntimeException(Token tk, String msg) {
        super(Parser.diagnostic(tk, msg));
    }

}
