package epsilonc.core;

import epsilonc.object.FuncNative;
import epsilonc.Environment;
import epsilonc.object.Value;
import epsilonc.resolver.Interpreter;
import epsilonc.type.NativeType;

public final class NativeFunction {


    public static void defineAll(Interpreter interpreter) {
        Environment globals = interpreter.getGlobals();

        globals.define("random", FuncNative.createValue((inter, args) -> Value.ofNumber(Math.random())));

        globals.define("clock", FuncNative.createValue((i, args) ->  Value.ofNumber((double) System.currentTimeMillis() / 1000.0)));

        globals.define("println", FuncNative.createValue(1, (i, args) ->{
            System.out.println(i.stringify(args.get(0)));
            return Value.ofVoid();
        }));

        globals.define("print", FuncNative.createValue(1, (i, args) -> {
            System.out.print(i.stringify(args.get(0)));
            return Value.ofVoid();
        }));

    }


}
