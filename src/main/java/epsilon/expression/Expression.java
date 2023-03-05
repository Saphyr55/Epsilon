package epsilon.expression;

public interface Expression {

    <R> R accept(ExpressionVisitor<R> visitor);

}
