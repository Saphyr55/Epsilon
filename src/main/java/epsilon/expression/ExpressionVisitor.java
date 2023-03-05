package epsilon.expression;

public interface ExpressionVisitor<R>  {

    R visitBinaryExpression(Expression.Binary expression);
    R visitGroupingExpression(Expression.Grouping expression);
    R visitLiteralExpression(LiteralExpression expression);
    R visitUnaryExpression(Expression.Unary expression);
    R visitLetExpression(Expression.Let expression);
    R visitAssignExpression(AssignExpression expression);
    R visitLogicalExpression(Expression.Logical expression);
    R visitCallExpression(CallExpression expression);
    R visitAnonymousFuncExpression(Expression.AnonymousFunc expression);
    R visitGetterExpression(GetterExpression getterExpression);
    R visitSetterExpression(Expression.Setter setter);
    R visitInitStructExpression(InitSructExpression expression);
    R visitThisExpression(Expression.This aThis);
}
