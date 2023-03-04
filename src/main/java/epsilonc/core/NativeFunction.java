package epsilonc.core;

import epsilonc.object.FuncNative;
import epsilonc.Environment;
import epsilonc.resolver.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public final class NativeFunction {


    public static void defineAll(Interpreter interpreter) {
        Environment globals = interpreter.getGlobals();

        globals.define("random", NativeType.Func, new FuncNative(0, (inter, args) -> Math.random()));

        globals.define("clock", NativeType.Func, new FuncNative(0, (i, args) -> (double) System.currentTimeMillis() / 1000.0));

        globals.define("println", NativeType.Func, new FuncNative(1, (i, args) -> {
            System.out.println(i.stringify(args.get(0)));
            return null;
        }));

        globals.define("print", NativeType.Func, new FuncNative(1, (i, args) -> {
            System.out.print(i.stringify(args.get(0)));
            return null;
        }));

    }


}
