package epsilonc.resolver;

import epsilonc.Environment;
import epsilonc.core.*;
import epsilonc.expression.*;
import epsilonc.statement.*;
import epsilonc.object.*;
import epsilonc.syntax.Kind;
import epsilonc.syntax.Syntax;
import epsilonc.syntax.Token;
import epsilonc.type.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static epsilonc.utils.Utils.*;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    private final Map<Expression, Integer> locals;
    private final Environment globals;
    private Environment environment;

    public Interpreter() {
        globals = new Environment();
        locals = new HashMap<>();
        environment = globals;
        NativeFunction.defineAll(this);
    }

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements)
                execute(statement);
        } catch (InterpretRuntimeException error) {
            Diagnostic.sendInterpretError(error);
        }
    }

    public void executeBlock(List<Statement> statements, Environment environment) {
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
    public Void visitReturnStatement(ReturnStatement statement) {
        Object value = statement.getValue() == null ? null : evaluate(statement.getValue());
        throw new ReturnRuntimeException(value);
    }

    @Override
    public Void visitLetStatement(LetStatement statement) {
        Object value = null;
        if (statement.getInitializer() != null)
            value = evaluate(statement.getInitializer());
        environment.define(statement.getName().text(), statement.getType().text(), value, statement.isMutable());
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
        while (isTruthy(evaluate(statement.getCondition()))) {
            execute(statement.getBody());
        }
        return null;
    }

    @Override
    public Void visitFunctionStatement(FunctionStatement statement) {
        Object o = environment.getValue(statement.getReturnType());
        if (o instanceof Type type) {
            environment.define(statement.getName().text(), NativeType.Func,
                    statement.createCallable(environment, type), false);
            return null;
        }
        throw new InterpretRuntimeException(statement.getName(), "Dont recognize '" + o + "' as type.");
    }

    @Override
    public Void visitClassStatement(ClassStatement statement) {

        Map<String, FuncCallable> functions = new HashMap<>();
        statement.getStaticFunctions().forEach(s -> {
            if (environment.getValue(s.getName()) instanceof Type type)
                functions.put(s.getName().text(), s.createCallable(environment, type));
        });

        Map<String, Let> fields = new HashMap<>();
        statement.getFields().forEach(s -> fields.put(s.getName().text(),
                new Let(evaluate(s.getInitializer()), s.getType().text(), s.isMutable())));

        Map<String, FuncCallable> methods = new HashMap<>();
        statement.getMethods().forEach(s -> {
            if (environment.getValue(s.getName()) instanceof Type type)
                methods.put(s.getName().text(), s.createCallable(environment, type));
        });

        EpsilonClass epsClass = new EpsilonClass(statement.getName().text(), methods, functions, fields);
        environment.define(statement.getName().text(), statement.getName().text(), epsClass);
        return null;
    }

    @Override
    public Void visitTypeStatement(StructStatement statement) {
        Map<String, Let> properties = new HashMap<>();
        statement.getProperties().forEach(ps ->
                properties.put(ps.getName().text(), new Let(null, ps.getType().text(), ps.isMutable())));
        EpsilonStruct tp = new EpsilonStruct(statement.getName().text(), properties);
        environment.define(statement.getName().text(), statement.getName().text(), tp, false);
        return null;
    }

    @Override
    public Void visitInitStatement(InitStatement initStatement) {
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression expression) {

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
                    if (right instanceof String r) yield Syntax.Word.Null + r;
                    if (right instanceof Double r) yield Syntax.Word.Null + r;
                }

                if (right == null) {
                    if (left instanceof String l) yield l + Syntax.Word.Null;
                    if (left instanceof Double l) yield l + Syntax.Word.Null;
                }

                throw new InterpretRuntimeException(op, "Operands must be two numbers or two strings.");
            }
            default -> null;
        };

    }

    @Override
    public Object visitGroupingExpression(GroupingExpression expression) {
        return evaluate(expression.getExpression());
    }

    @Override
    public Object visitLiteralExpression(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression expression) {
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
        return lookUpVariable(expression.getName(), expression);
    }

    @Override
    public Object visitAssignExpression(AssignExpression expression) {
        Object value = evaluate(expression.getValue());
        Integer distance = locals.get(expression);
        if (distance != null) environment.assignAt(distance, expression.getName(), value);
        else globals.assign(expression.getName(), value);
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

    @Override
    public Object visitCallExpression(CallExpression expression) {
        Object callable = evaluate(expression.getCallable());

        List<Object> arguments = expression.getArguments().stream().toList()
                        .stream().map(this::evaluate).toList();

        if (callable instanceof Callable c) {
            if (arguments.size() != c.arity()) {
                throw new InterpretRuntimeException(expression.getParen(), "Expected " +
                        c.arity() + " arguments but got " +
                        arguments.size() + ".");
            }
            return c.call(this, arguments);
        }

        throw new InterpretRuntimeException(expression.getParen(), "Can only call functions and classes.");
    }

    @Override
    public Object visitAnonymousFuncExpression(AnonymousFuncExpression expression) {

        if (environment.getValue(expression.getStatement().getReturnType()) instanceof Type type)
            return expression.getStatement().createCallable(environment, type);

        throw new InterpretRuntimeException(expression.getStatement().getReturnType(), "Dont recognize '" +
                expression.getStatement().getReturnType() + "' as type.");
    }

    @Override
    public Object visitGetterExpression(GetterExpression expression) {
        Object object = evaluate(expression.getObjet());
        if (object instanceof Instance instance) {
            return instance.get(expression.getName());
        }
        throw new InterpretRuntimeException(expression.getName(), "Only instances have properties.");
    }

    @Override
    public Object visitSetterExpression(SetterExpression expression) {
        Object object = evaluate(expression.getObject());
        if (object instanceof Instance instanceClass) {
            Object value = evaluate(expression.getValue());
            instanceClass.set(expression.getName(), value);
            return value;
        }
        throw new InterpretRuntimeException(expression.getName(), "Only instance have fields");
    }

    @Override
    public Object visitInitStructExpression(InitSructExpression expression) {
        Map<String, Object> attributions = new HashMap<>();
        expression.getStatements().forEach(s -> {
            if (s instanceof InitStatement is)
                attributions.put(is.getName().text(), evaluate(is.getValue()));
        });
        Object oType = environment.getValue(expression.getType());
        if (oType instanceof EpsilonStruct epsilonStruct) {
            check(expression.getType(), epsilonStruct, attributions);
            return new InstanceType(epsilonStruct, attributions);
        }
        throw new InterpretRuntimeException(expression.getType(),
                "Not recognize the type '"+expression.getType().text()+"'.");
    }

    public Object evaluate(Expression expression) {
        if (expression == null) return null;
        return expression.accept(this);
    }

    public void execute(Statement statement) {
        statement.accept(this);
    }

    public void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new InterpretRuntimeException(operator, "Operand must be a number.");
    }

    public void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new InterpretRuntimeException(operator, "Operands must be numbers.");
    }

    public String stringify(Object object) {

        if (object == null) return Syntax.Word.Null;

        if (object instanceof Double) {
            String text_ = object.toString();
            if (text_.endsWith(".0")) {
                text_ = text_.substring(0, text_.length() - 2);
            }
            return text_;
        }

        return object.toString();
    }

    public void resolve(Expression expr, int depth) {
        locals.put(expr, depth);
    }

    private Object lookUpVariable(Token name, Expression expression) {
        Integer distance = locals.get(expression);
        if (distance != null) {
            return environment.getAt(distance, name.text());
        } else {
            return globals.getValue(name);
        }
    }

    public Environment getGlobals() {
        return globals;
    }

    public Environment getEnvironment() {
        return environment;
    }

    private static void check(Token name, EpsilonStruct epsilonStruct, Map<String, Object> attributions) {
        for (Map.Entry<String, Object> stringObjectEntry : attributions.entrySet()) {
            if (!epsilonStruct.getProperties().containsKey(stringObjectEntry.getKey())) {
                throw new InterpretRuntimeException(name,
                        "'"+name.text()+"' not correspond at '" + epsilonStruct.getName()+"'.");
            }
        }
    }



}