package epsilon.expression;

import epsilon.statement.FunctionStatement;
import epsilon.syntax.Token;

public interface Expression {

    <R> R accept(ExpressionVisitor<R> visitor);

    record AnonymousFunc(FunctionStatement statement) implements Expression {

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitAnonymousFuncExpression(this);
        }

    }

    record Binary(Expression left, Token op, Expression right) implements Expression {

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitBinaryExpression(this);
        }

        @Override
        public String toString() {
            return "Binary{" +
                    "left=" + left +
                    ", right=" + right +
                    ", op=" + op +
                    '}';
        }
    }

    record Grouping(Expression expression) implements Expression {

        @Override
        public String toString() {
            return "Grouping{" +
                    "expr=" + expression +
                    '}';
        }

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitGroupingExpression(this);
        }

    }

    record Let(Token name) implements Expression {

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitLetExpression(this);
        }

        @Override
        public String toString() {
            return "LetExpression{" +
                    "name=" + name +
                    '}';
        }
    }

    record Setter(Expression object, Token name, Expression value) implements Expression {

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitSetterExpression(this);
        }

    }

    record This(Token kw) implements Expression {
        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitThisExpression(this);
        }

    }

    record Logical(Expression left, Token op, Expression right) implements Expression {

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitLogicalExpression(this);
        }
    }

    record Unary(Token op, Expression right) implements Expression {

        @Override
        public <R> R accept(ExpressionVisitor<R> visitor) {
            return visitor.visitUnaryExpression(this);
        }

        @Override
        public String toString() {
            return "Unary{" +
                    "op=" + op +
                    ", right=" + right +
                    '}';
        }

    }

}
