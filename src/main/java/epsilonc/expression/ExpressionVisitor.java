package epsilonc.expression;

public interface ExpressionVisitor<R>  {

   R visitBinaryExpr(BinaryExpression expr);
   R visitGroupingExpr(GroupingExpression expr);
   R visitLiteralExpr(LiteralExpression expr);
   R visitUnaryExpr(UnaryExpression expr);
   R visitLetExpression(LetExpression letExpression);

}
