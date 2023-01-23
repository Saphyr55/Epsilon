package epsilonc.core;

import epsilonc.FunctionCallable;
import epsilonc.Environment;
import epsilonc.Interpreter;

import java.util.List;

public interface NativeFunction {

    static void defineAll(Interpreter interpreter) {
        Environment globals = interpreter.getGlobals();
        globals.define("clock",(FunctionCallable) (i, args) -> (double) System.currentTimeMillis() / 1000.0);
        globals.define("println", new FunctionCallable.Native(1, (i, args) -> {
            System.out.println(i.stringify(args.get(0)));
            return null;
        }));
        globals.define("print", new FunctionCallable.Native(1, (i, args) -> {
            System.out.print(i.stringify(args.get(0)));
            return null;
        }));

    }


}
