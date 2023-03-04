package epsilonc.type;

import epsilonc.object.Value;
import epsilonc.resolver.Interpreter;

public class NativeType {

    public static final TFunc Func = new TFunc();
    public static final TNumber Number = new TNumber();
    public static final TBool Bool = new TBool();
    public static final TString String = new TString();
    public static final TVoid Void = new TVoid();
    public static final TUndefined Undefined = new TUndefined();

    public record TFunc() implements Type { public String name() {return "function"; }}
    public record TVoid() implements Type { public String name() { return "void"; } }
    public record TString() implements Type { public String name() { return "string"; } }
    public record TBool() implements Type { public String name() { return "bool"; } }
    public static class TNumber implements Type { public String name() { return "number"; } }
    public record TUndefined() implements Type { public String name() {return "undefined"; }}

    public static void defineAllNativeTypes(Interpreter interpreter) {
        interpreter.getGlobals().define(Func.name(), Value.of(Func, Func));
        interpreter.getGlobals().define(Number.name(), Value.of(Number, Number));
        interpreter.getGlobals().define(Bool.name(), Value.of(Bool, Bool));
        interpreter.getGlobals().define(String.name(), Value.of(String, String));
        interpreter.getGlobals().define(Void.name(), Value.of(Void, Void));
    }

}

