package epsilon.core;

public class Diagnostic {

    public static void sendInterpretError(InterpretRuntimeException exception) {
        System.err.println(exception.getMessage() +
                    "\n[line: " + (exception.token.line() + 1 ) +
                    ", col:" + (exception.token.col()  + 1 )+ "]");
    }

}
