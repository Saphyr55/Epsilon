package epsilonc.core;

import epsilonc.Parser;
import epsilonc.syntax.Token;

public class ResolverRuntimeException extends RuntimeException {

    public ResolverRuntimeException() { }

    public ResolverRuntimeException(Token tk, String msg) {
        super(Parser.diagnostic(tk, msg));
    }

}
