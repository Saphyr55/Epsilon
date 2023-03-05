package epsilon.resolver;

import epsilon.core.ClassType;
import epsilon.syntax.Syntax;
import epsilon.syntax.Token;
import epsilon.core.FunctionType;
import epsilon.core.ResolverRuntimeException;
import epsilon.expression.*;
import epsilon.statement.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ScopeResolver implements Resolver<Void, Void> {

    private FunctionType currentFunction = FunctionType.None;
    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private ClassType currentClass = ClassType.NONE;

    public ScopeResolver(Interpreter interpreter) {
        this.interpreter = interpreter;
        beginScope();
    }

    @Override
    public Void visitBinaryExpression(Expression.Binary expression) {
        resolve(expression.left());
        resolve(expression.right());
        return null;
    }

    @Override
    public Void visitGroupingExpression(Expression.Grouping expression) {
        resolve(expression.expression());
        return null;
    }

    @Override
    public Void visitLiteralExpression(LiteralExpression expression) {
        return null;
    }

    @Override
    public Void visitUnaryExpression(Expression.Unary expression) {
        resolve(expression.right());
        return null;
    }

    @Override
    public Void visitLetExpression(Expression.Let expression) {
        if (!scopes.isEmpty() && scopes.peek().get(expression.name().text()) == Boolean.FALSE) {
            throw new ResolverRuntimeException(expression.name(), "Can't read local variable in its own initializer.");
        }
        resolveLocal(expression, expression.name());
        return null;
    }

    @Override
    public Void visitAssignExpression(AssignExpression expression) {
        resolve(expression.value());
        resolveLocal(expression, expression.name());
        return null;
    }

    @Override
    public Void visitLogicalExpression(Expression.Logical expression) {
        resolve(expression.left());
        resolve(expression.right());
        return null;
    }

    @Override
    public Void visitCallExpression(CallExpression expression) {
        resolve(expression.getCallable());
        for (Expression argument : expression.getArguments()) resolve(argument);
        return null;
    }

    @Override
    public Void visitAnonymousFuncExpression(Expression.AnonymousFunc expression) {
        resolveFunction(expression.statement(), FunctionType.Anonymous);
        return null;
    }

    @Override
    public Void visitGetterExpression(GetterExpression expression) {
        resolve(expression.getObject());
        return null;
    }

    @Override
    public Void visitSetterExpression(Expression.Setter expression) {
        resolve(expression.value());
        resolve(expression.object());
        return null;
    }

    @Override
    public Void visitInitStructExpression(InitSructExpression expression) {
        resolve(expression.getStatements());
        return null;
    }

    @Override
    public Void visitThisExpression(Expression.This aThis) {
        if (currentClass == ClassType.NONE) {
            throw new ResolverRuntimeException(aThis.kw(),
                    "Can't use 'this' outside of a class.");
        }
        resolveLocal(aThis, aThis.kw());
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        resolve(statement.expression());
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
        declare(statement.name());
        if (statement.initializer() != null)
            resolve(statement.initializer());
        define(statement.name());
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement statement) {
        beginScope();
        resolve(statement.statements());
        endScope();
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement statement) {
        resolve(statement.condition());
        resolve(statement.thenBranch());
        if (statement.elseBranch() != null) resolve(statement.elseBranch());
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement statement) {
        resolve(statement.condition());
        resolve(statement.body());
        return null;
    }

    @Override
    public Void visitFunctionStatement(FunctionStatement statement) {
        declare(statement.name());
        define(statement.name());
        resolveFunction(statement, FunctionType.Function);
        return null;
    }

    @Override
    public Void visitClassStatement(ClassStatement statement) {
        ClassType enclosingClass = currentClass;
        currentClass = ClassType.CLASS;
        declare(statement.getName());
        define(statement.getName());
        // resolve(statement.getStaticFunctions());
        beginScope();
        scopes.peek().put(Syntax.Word.This, true);
        resolve(statement.getFields());
        for (FunctionStatement method : statement.getMethods()) {
            resolveFunction(method, FunctionType.Method);
        }
        endScope();
        currentClass = enclosingClass;
        return null;
    }

    @Override
    public Void visitStructStatement(StructStatement statement) {
        declare(statement.name());
        define(statement.name());
        beginScope();
        resolve(statement.properties());
        endScope();
        return null;
    }

    @Override
    public Void visitInitStatement(InitStatement statement) {
        resolve(statement.value());
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
        for (var param : statement.paramsId()) {
            declare(param);
            define(param);
        }
        resolve(statement.body());
        endScope();
        currentFunction = enclosingFunction;
    }

}
