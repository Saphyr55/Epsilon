package org.epsilon.expression;

import org.epsilon.core.InterpretRuntimeException;

public class Diagnostic {

    public static void sendInterpretError(InterpretRuntimeException exception) {
        System.err.println(exception.getMessage() +
                "\n[line: " + exception.token.line() +
                ", col:" + exception.token.col() + "]");
    }

}
