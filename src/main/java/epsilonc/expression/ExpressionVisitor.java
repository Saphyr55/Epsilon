package epsilonc.expression;

public interface ExpressionVisitor<R>  {

    R visitBinaryExpression(BinaryExpression expression);
    R visitGroupingExpression(GroupingExpression expression);
    R visitLiteralExpression(LiteralExpression expression);
    R visitUnaryExpression(UnaryExpression expression);
    R visitLetExpression(LetExpression expression);
    R visitAssignExpression(AssignExpression expression);
    R visitLogicalExpression(LogicalExpression expression);
    R visitCallExpression(CallExpression expression);
    R visitAnonymousFuncExpression(AnonymousFuncExpression expression);
    R visitGetterExpression(GetterExpression getterExpression);
    R visitSetterExpression(SetterExpression setterExpression);
    R visitBlockExpression(BlockExpression blockExpression);
}
