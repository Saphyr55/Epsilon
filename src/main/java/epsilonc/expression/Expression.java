package epsilonc.expression;

import epsilonc.type.Type;

public interface Expression {

    <R> R accept(ExpressionVisitor<R> visitor);

}
