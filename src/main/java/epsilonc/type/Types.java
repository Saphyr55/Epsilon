package epsilonc.type;

import epsilonc.core.NativeType;
import epsilonc.expression.Expression;
import epsilonc.syntax.Token;

public class Types {


    public record TEquality(Expression left, Expression right, Token op) implements Type {
        @Override public String name() { return NativeType.Bool; }
    }

    public record TLogOp(Expression left, Expression right, Token op) implements Type {
        @Override public String name() { return NativeType.Bool; }
    }

    public record TBinOp(Expression left, Expression right, Token op) implements Type {
        @Override public String name() { return NativeType.Number; }
    }

    public record TNumber() implements Type {
        @Override public String name() { return NativeType.Number; }
    }

    public record TTrue() implements Type {
        @Override public String name() { return NativeType.Bool; }
    }

    public record TFalse() implements Type {
        @Override public String name() { return NativeType.Bool; }
    }

}
