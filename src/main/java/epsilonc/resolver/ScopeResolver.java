package epsilonc.resolver;

import epsilonc.syntax.Token;
import epsilonc.core.FunctionType;
import epsilonc.core.ResolverRuntimeException;
import epsilonc.expression.*;
import epsilonc.statement.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ScopeResolver implements Resolver<Void, Void> {

    public record Scope(String type, boolean isDefine) { }

    private FunctionType currentFunction = FunctionType.None;
    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();

    public ScopeResolver(Interpreter interpreter) {
        this.interpreter = interpreter;
        beginScope();
    }

    @Override
    public Void visitBinaryExpression(BinaryExpression expression) {
        resolve(expression.getLeft());
        resolve(expression.getRight());
        return null;
    }

    @Override
    public Void visitGroupingExpression(GroupingExpression expression) {
        resolve(expression.getExpression());
        return null;
    }

    @Override
    public Void visitLiteralExpression(LiteralExpression expression) {
        return null;
    }

    @Override
    public Void visitUnaryExpression(UnaryExpression expression) {
        resolve(expression.getRight());
        return null;
    }

    @Override
    public Void visitLetExpression(LetExpression expression) {
        if (!scopes.isEmpty() && scopes.peek().get(expression.getName().text()) == Boolean.FALSE) {
            throw new ResolverRuntimeException(expression.getName(), "Can't read local variable in its own initializer.");
        }
        resolveLocal(expression, expression.getName());
        return null;
    }

    @Override
    public Void visitAssignExpression(AssignExpression expression) {
        resolve(expression.value());
        resolveLocal(expression, expression.name());
        return null;
    }

    @Override
    public Void visitLogicalExpression(LogicalExpression expression) {
        resolve(expression.getLeft());
        resolve(expression.getRight());
        return null;
    }

    @Override
    public Void visitCallExpression(CallExpression expression) {
        resolve(expression.getCallable());
        for (Expression argument : expression.getArguments()) resolve(argument);
        return null;
    }

    @Override
    public Void visitAnonymousFuncExpression(AnonymousFuncExpression expression) {
        resolveFunction(expression.getStatement(), FunctionType.Anonymous);
        return null;
    }

    @Override
    public Void visitGetterExpression(GetterExpression expression) {
        resolve(expression.getObjet());
        return null;
    }

    @Override
    public Void visitSetterExpression(SetterExpression expression) {
        resolve(expression.getValue());
        resolve(expression.getObject());
        return null;
    }

    @Override
    public Void visitInitStructExpression(InitSructExpression expression) {
        resolve(expression.getStatements());
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        resolve(statement.getExpression());
        return null;
    }

    @Override
    public Void visitReturnStatement(ReturnStatement statement) {
        if (currentFunction == FunctionType.None) {
            throw new ResolverRuntimeException(statement.getKeyword(),
                    "Can't return from top-level code.");
        }
        if (statement.getValue() != null) resolve(statement.getValue());
        return null;
    }

    @Override
    public Void visitLetStatement(LetStatement statement) {
        declare(statement.getName());
        if (statement.getInitializer() != null)
            resolve(statement.getInitializer());
        define(statement.getName());
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement statement) {
        beginScope();
        resolve(statement.getStatements());
        endScope();
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement statement) {
        resolve(statement.getCondition());
        resolve(statement.getThenBranch());
        if (statement.getElseBranch() != null) resolve(statement.getElseBranch());
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement statement) {
        resolve(statement.getCondition());
        resolve(statement.getBody());
        return null;
    }

    @Override
    public Void visitFunctionStatement(FunctionStatement statement) {
        declare(statement.getName());
        define(statement.getName());
        resolveFunction(statement, FunctionType.Function);
        return null;
    }

    @Override
    public Void visitClassStatement(ClassStatement statement) {
        declare(statement.getName());
        define(statement.getName());
        resolve(statement.getFields());
        resolve(statement.getStaticFunctions());
        resolve(statement.getMethods());
        return null;
    }

    @Override
    public Void visitStructStatement(StructStatement statement) {
        declare(statement.getName());
        define(statement.getName());
        resolve(statement.getProperties());
        return null;
    }

    @Override
    public Void visitInitStatement(InitStatement statement) {
        resolve(statement.getValue());
        return null;
    }

    private void beginScope() {
        scopes.push(new HashMap<>());
    }

    private void endScope() {
        scopes.pop();
    }

    private void declare(Token name) {

        if (scopes.isEmpty()) return;

        Map<String, Boolean> scope = scopes.peek();
        if (scope.containsKey(name.text()))
            throw new ResolverRuntimeException(name, "Already a variable with this name in this scope.");

        scope.put(name.text(), false);
    }

    private void define(Token name) {
        if (scopes.isEmpty()) return;
        scopes.peek().put(name.text(), true);
    }

    private void resolveLocal(Expression expression, Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.text())) {
                interpreter.resolve(expression, scopes.size() - 1 - i);
                return;
            }
        }
    }

    private void resolveFunction(FunctionStatement statement, FunctionType type) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = type;
        beginScope();
        for (var param : statement.getParams().entrySet()) {
            declare(param.getKey());
            define(param.getKey());
        }
        resolve(statement.getBody());
        endScope();
        currentFunction = enclosingFunction;
    }

}
