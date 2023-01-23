package epsilonc.expression;

public interface ExpressionVisitor<R>  {

   R visitBinaryExpr(BinaryExpression expression);
   R visitGroupingExpr(GroupingExpression expression);
   R visitLiteralExpr(LiteralExpression expression);
   R visitUnaryExpr(UnaryExpression expression);
   R visitLetExpression(LetExpression expression);
   R visitAssignExpression(AssignExpression expression);
    R visitLogicalExpression(LogicalExpression expression);
    R visitCallExpression(CallExpression expression);
    R visitAnonymousFuncExpression(AnonymousFuncExpression expression);
}
