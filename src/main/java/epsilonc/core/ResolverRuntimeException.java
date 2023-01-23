package epsilonc.core;

import epsilonc.Parser;
import epsilonc.Token;

public class ResolverRuntimeException extends RuntimeException {

    public ResolverRuntimeException() { }

    public ResolverRuntimeException(Token tk, String msg) {
        super(Parser.diagnostic(tk, msg));
    }

}
