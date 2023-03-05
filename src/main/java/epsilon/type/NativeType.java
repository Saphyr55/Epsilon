package epsilon.type;

import epsilon.object.Value;
import epsilon.resolver.Interpreter;

public class NativeType {

    public static final TType Type = new TType();
    public static final TBool Bool = new TBool();
    public static final TString String = new TString();
    public static final TVoid Void = new TVoid();
    public static final TNumber Number = new TNumber();
    public static final TInt32 Int32 = new TInt32();
    public static final TFloat32 Float32 = new TFloat32();
    public static final TFloat64 Float64 = new TFloat64();
    public static final TInt64 Int64 = new TInt64();
    public static final TUndefined Undefined = new TUndefined();
    public static final TFunc Func = new TFunc();
    public static final TChar Char = new TChar();

    public static class TType implements Type { public String name() { return "type"; } }
    public static class TChar extends TType { public String name() { return "char"; } }
    public static class TFunc extends TType { public String name() { return "function"; } }
    public static class TVoid extends TType { public String name() { return "void"; } }
    public static class TString extends TType { public String name() { return "string"; } }
    public static class TBool extends TType { public String name() { return "bool"; } }
    public static class TNumber extends TType { public String name() { return "number"; } }
    public static class TUndefined extends TType { public String name() {return "undefined"; } }
    public static class TInt32 extends TNumber { public String name() { return "i32"; } }
    public static class TInt64 extends TNumber { public String name() { return "i64"; } }
    public static class TFloat32 extends TNumber { public String name() { return "f32"; } }
    public static class TFloat64 extends TNumber { public String name() { return "f64"; } }

    public static void defineAllNativeTypes(Interpreter interpreter) {
        interpreter.getGlobals().define(Func.name(), Value.ofType(Func));
        interpreter.getGlobals().define(Number.name(), Value.ofType(Number));
        interpreter.getGlobals().define(Int32.name(), Value.ofType(Int32));
        interpreter.getGlobals().define(Float32.name(), Value.ofType(Float32));
        interpreter.getGlobals().define(Int64.name(), Value.ofType(Int64));
        interpreter.getGlobals().define(Float64.name(), Value.ofType(Float64));
        interpreter.getGlobals().define(Bool.name(), Value.ofType(Bool));
        interpreter.getGlobals().define(String.name(), Value.ofType(String));
        interpreter.getGlobals().define(Void.name(), Value.ofType(Void));
        interpreter.getGlobals().define(Type.name(), Value.ofType(Type));
    }

}

