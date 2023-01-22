package org.epsilon;

public class Expression {

    public static final class Binary extends Expression {

        public Expression left, right;
        public Token op;

        public Binary(Expression left, Token op, Expression right) {
            this.left = left;
            this.right = right;
            this.op = op;
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

    public static final class Grouping extends Expression {

        public Expression expr;

        public Grouping(Expression expr) { this.expr = expr; }

        @Override
        public String toString() {
            return "Grouping{" +
                    "expr=" + expr +
                    '}';
        }
    }

    public static final class Literal extends Expression {
        public Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Literal{" +
                    "value=" + value +
                    '}';
        }
    }

    public static final class Unary extends Expression {

        public Token op;
        public Expression right;

        public Unary(Token op, Expression right) {
            this.right = right;
            this.op = op;
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
