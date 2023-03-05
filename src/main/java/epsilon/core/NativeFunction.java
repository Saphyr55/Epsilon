package epsilon.core;

import epsilon.object.*;
import epsilon.Environment;
import epsilon.resolver.Interpreter;
import epsilon.type.NativeType;

import java.util.List;

public final class NativeFunction {

    public static void defineAll(Interpreter interpreter) {
        Environment globals = interpreter.getGlobals();

        globals.define("println", NativeFunc.createValue(new NativeFunc() {
            @Override
            public Prototype prototype() {
                return new Prototype("println", List.of(NativeType.Type));
            }

            @Override
            public Value call(Interpreter inter, List<Value> args) {
                System.out.println(inter.stringify(args.get(0)));
                return Value.ofVoid();
            }

        }));

        globals.define("print", NativeFunc.createValue(new NativeFunc() {
            @Override
            public Prototype prototype() {
                return new Prototype("print", List.of(NativeType.Type));
            }

            @Override
            public Value call(Interpreter inter, List<Value> args) {
                System.out.print(inter.stringify(args.get(0)));
                return Value.ofVoid();
            }
        }));

    }


}
