package epsilon.core;

import epsilon.object.NativeFunc;
import epsilon.Environment;
import epsilon.object.Value;
import epsilon.resolver.Interpreter;

public final class NativeFunction {


    public static void defineAll(Interpreter interpreter) {
        Environment globals = interpreter.getGlobals();

        globals.define("random", NativeFunc.createValue((inter, args) -> Value.ofNumber(Math.random())));

        globals.define("clock", NativeFunc.createValue((i, args) ->  Value.ofNumber((double) System.currentTimeMillis() / 1000.0)));

        globals.define("to_type", NativeFunc.createValue(1, (inter, args) -> Value.ofString(args.get(0).toString())));

        globals.define("println", NativeFunc.createValue(1, (i, args) ->{
            System.out.println(i.stringify(args.get(0)));
            return Value.ofVoid();
        }));

        globals.define("print", NativeFunc.createValue(1, (i, args) -> {
            System.out.print(i.stringify(args.get(0)));
            return Value.ofVoid();
        }));

    }


}
