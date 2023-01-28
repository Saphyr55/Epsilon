package epsilonc.resolver;

import epsilonc.expression.*;
import epsilonc.statement.*;

public class TypeResolver implements Resolver<Void, Void> {

    private final Interpreter interpreter;
    private final ScopeResolver scopeResolver;

    public TypeResolver(ScopeResolver scopeResolver, Interpreter interpreter) {
        this.interpreter = interpreter;
        this.scopeResolver = scopeResolver;
    }

    @Override
    public Void visitBinaryExpression(BinaryExpression expression) {
        return null;
    }

    @Override
    public Void visitGroupingExpression(GroupingExpression expression) {
        return null;
    }

    @Override
    public Void visitLiteralExpression(LiteralExpression expression) {
        return null;
    }

    @Override
    public Void visitUnaryExpression(UnaryExpression expression) {
        return null;
    }

    @Override
    public Void visitLetExpression(LetExpression expression) {
        return null;
    }

    @Override
    public Void visitAssignExpression(AssignExpression expression) {
        return null;
    }

    @Override
    public Void visitLogicalExpression(LogicalExpression expression) {
        return null;
    }

    @Override
    public Void visitCallExpression(CallExpression expression) {
        return null;
    }

    @Override
    public Void visitAnonymousFuncExpression(AnonymousFuncExpression expression) {
        return null;
    }

    @Override
    public Void visitGetterExpression(GetterExpression getterExpression) {
        return null;
    }

    @Override
    public Void visitSetterExpression(SetterExpression setterExpression) {
        return null;
    }

    @Override
    public Void visitBlockExpression(InitTypeExpression initTypeExpression) {
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        return null;
    }

    @Override
    public Void visitReturnStatement(ReturnStatement statement) {
        return null;
    }

    @Override
    public Void visitLetStatement(LetStatement statement) {
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement statement) {
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement statement) {
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement whileStatement) {
        return null;
    }

    @Override
    public Void visitFunctionStatement(FunctionStatement functionStatement) {
        return null;
    }

    @Override
    public Void visitClassStatement(ClassStatement classStatement) {
        return null;
    }

    @Override
    public Void visitTypeStatement(TypeStatement typeStatement) {
        return null;
    }

    @Override
    public Void visitInitStatement(InitStatement initStatement) {
        return null;
    }

}
