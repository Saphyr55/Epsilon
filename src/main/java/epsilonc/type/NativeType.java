package epsilonc.type;

public class NativeType {

    public static final TFunc Func = new TFunc();
    public static final TNumber Number = new TNumber();
    public static final TBool Bool = new TBool();
    public static final TString String = new TString();
    public static final TVoid Void = new TVoid();
    public static final TNull Null = new TNull();

    public record TFunc() implements Type { public String name() {return "function"; }}
    public record TVoid() implements Type { public String name() { return "void"; } }
    public record TString() implements Type { public String name() { return "string"; } }
    public record TBool() implements Type { public String name() { return "bool"; } }
    public record TNull() implements Type { public String name() { return "null"; } }
    public static class TNumber implements Type { public String name() { return "number"; } }

}

