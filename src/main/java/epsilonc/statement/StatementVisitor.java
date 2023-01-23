package epsilonc.statement;

public interface StatementVisitor<R> {

    R visitExpressionStatement(ExpressionStatement statement);
    R visitReturnStatement(ReturnStatement statement);
    R visitLetStatement(LetStatement statement);
    R visitBlockStatement(BlockStatement statement);
    R visitIfStatement(IfStatement statement);
    R visitWhileStatement(WhileStatement whileStatement);
    R visitBreakStatement(BreakStatement breakStatement);
    R visitFunctionStatement(FunctionStatement functionStatement);

}
