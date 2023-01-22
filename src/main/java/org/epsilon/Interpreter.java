package org.epsilon;

import org.epsilon.core.InterpretRuntimeException;
import org.epsilon.expression.*;
import org.epsilon.stataments.ExpressionStatement;
import org.epsilon.stataments.PrintStatement;
import org.epsilon.stataments.Statement;
import org.epsilon.stataments.StatementVisitor;

import java.util.List;

import static org.epsilon.utils.Utils.isEqual;
import static org.epsilon.utils.Utils.isTruthy;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements)
                execute(statement);

        } catch (InterpretRuntimeException error) {
            Diagnostic.sendInterpretError(error);
        }
    }

    private void execute(Statement statement) {
        statement.accept(this);
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        evaluate(statement.getExpression());
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement statement) {
        Object value = evaluate(statement.getExpression());
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpression expression) {

        Object left = evaluate(expression.getLeft());
        Object right = evaluate(expression.getRight());
        Token op = expression.getOp();

        return switch (op.kind()) {
            case NotEqual -> !isEqual(left, right);
            case Equal -> isEqual(left, right);
            case LessEqual -> {
                checkNumberOperands(op, left, right);
                yield (double) left <= (double) right;
            }
            case Less -> {
                checkNumberOperands(op, left, right);
                yield (double) left < (double) right;
            }
            case GreaterEqual -> {
                checkNumberOperands(op, left, right);
                yield (double) left >= (double) right;
            }
            case Greater -> {
                checkNumberOperands(op, left, right);
                yield (double) left > (double) right;
            }
            case Minus -> {
                checkNumberOperands(op, left, right);
                yield (double) left - (double) right;
            }
            case Slash -> {
                checkNumberOperands(op, left, right);
                yield (double) left / (double) right;
            }
            case Star -> {
                checkNumberOperands(op, left, right);
                yield (double) left * (double) right;
            }
            case Plus -> {
                if (left instanceof Double && right instanceof Double) yield  (double)left + (double)right;
                if (left instanceof String l && right instanceof String r) yield  l + r;
                if (left instanceof String l && right instanceof Double r) yield l + r;
                if (left instanceof Double l && right instanceof String r) yield l + r;

                throw new InterpretRuntimeException(op, "Operands must be two numbers or two strings.");
            }
            default -> null;
        };

    }

    @Override
    public Object visitGroupingExpr(GroupingExpression expression) {
        return evaluate(expression.getExpression());
    }

    @Override
    public Object visitLiteralExpr(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public Object visitUnaryExpr(UnaryExpression expression) {
        Object right = evaluate(expression.getRight());
        return switch (expression.getOp().kind()) {
            case Minus -> {
                checkNumberOperand(expression.getOp(), right);
                yield - (double) right;
            }
            case Not -> !isTruthy(right);
            default -> null;
        };
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new InterpretRuntimeException(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new InterpretRuntimeException(operator, "Operands must be numbers.");
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

}
