package epsilonc.core;

import epsilonc.type.FuncNative;
import epsilonc.type.FunctionCallable;
import epsilonc.Environment;
import epsilonc.Interpreter;

public interface NativeFunction {

    static void defineAll(Interpreter interpreter) {
        Environment globals = interpreter.getGlobals();
        globals.define("clock",(FunctionCallable) (i, args) -> (double) System.currentTimeMillis() / 1000.0);
        globals.define("println", new FuncNative(1, (i, args) -> {
            System.out.println(i.stringify(args.get(0)));
            return null;
        }));
        globals.define("print", new FuncNative(1, (i, args) -> {
            System.out.print(i.stringify(args.get(0)));
            return null;
        }));

    }


}