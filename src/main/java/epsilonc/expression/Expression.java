package epsilonc.expression;

public interface Expression {

    <R> R accept(ExpressionVisitor<R> visitor);

}
