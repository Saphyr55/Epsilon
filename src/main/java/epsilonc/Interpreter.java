package epsilonc;

import epsilonc.core.InterpretRuntimeException;
import epsilonc.expression.*;
import epsilonc.statement.*;

import java.util.List;

import static epsilonc.utils.Utils.isEqual;
import static epsilonc.utils.Utils.isTruthy;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    private Environment environment = new Environment();

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

    private void executeBlock(List<Statement> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Statement statement : statements)
                execute(statement);
        } finally {
            this.environment = previous;
        }
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        evaluate(statement.getExpression());
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement statement) {
        Object value = evaluate(statement.getExpression());
        System.out.print(stringify(value));
        return null;
    }

    @Override
    public Void visitLetStatement(LetStatement statement) {
        Object value = null;
        if (statement.getInitializer() != null)
            value = evaluate(statement.getInitializer());

        environment.define(statement.getName().text(), value);
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement statement) {
        executeBlock(statement.getStatements(), new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement statement) {
        if (isTruthy(evaluate(statement.getCondition()))) {
            execute(statement.getThenBranch());
        } else if (statement.getElseBranch() != null) {
            execute(statement.getElseBranch());
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement statement) {
        while (isTruthy(evaluate(statement.getCondition())))
        {

            execute(statement.getBody());
        }
        return null;
    }

    @Override
    public Void visitBreakStatement(BreakStatement breakStatement) {
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

                if (left == null) {
                    if (right instanceof String r) yield Syntax.Word.Nil + r;
                    if (right instanceof Double r) yield Syntax.Word.Nil + r;
                }

                if (right == null) {
                    if (left instanceof String l) yield l + Syntax.Word.Nil;
                    if (left instanceof Double l) yield l + Syntax.Word.Nil;
                }

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

    @Override
    public Object visitLetExpression(LetExpression expression) {
        Token name = expression.getName();
        return environment.get(name);
    }

    @Override
    public Object visitAssignExpression(AssignExpression expression) {
        Object value = evaluate(expression.getValue());
        environment.assign(expression.getName(), value);
        return value;
    }

    @Override
    public Object visitLogicalExpression(LogicalExpression expression) {
        Object left = evaluate(expression.getLeft());

        if (expression.getOp().kind() == Kind.LogicalOr && isTruthy(left)) {
            return left;
        } else if (!isTruthy(left)) {
            return left;
        }

        return evaluate(expression.getRight());
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
            String text_ = object.toString();
            if (text_.endsWith(".0")) {
                text_ = text_.substring(0, text_.length() - 2);
            }
            return text_;
        }

        return object.toString();
    }

}
