package epsilonc.resolver;

import epsilonc.Environment;
import epsilonc.core.*;
import epsilonc.expression.*;
import epsilonc.statement.*;
import epsilonc.object.*;
import epsilonc.syntax.Kind;
import epsilonc.syntax.Syntax;
import epsilonc.syntax.Token;
import epsilonc.type.NativeType;
import epsilonc.type.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static epsilonc.utils.Utils.*;

public class Interpreter implements ExpressionVisitor<Value>, StatementVisitor<Void> {

    private final Map<Expression, Integer> locals;
    private final Environment globals;
    private Environment environment;

    public Interpreter() {
        globals = new Environment();
        locals = new HashMap<>();
        environment = globals;
        NativeFunction.defineAll(this);
        NativeType.defineAllNativeTypes(this);
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
        Value value = statement.getValue() == null ? Value.ofVoid() : evaluate(statement.getValue());
        throw new ReturnRuntimeException(value);
    }

    @Override
    public Void visitLetStatement(LetStatement statement) {
        Value value = null;
        if (statement.getInitializer() != null)
            value = evaluate(statement.getInitializer());
        environment.define(statement.getName().text(),
                value == null ? Value.of(environment.getType(statement.getType())) : value,
                statement.isMutable());
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
        statement.getParams().values().forEach(param -> environment.getValue(param));
        environment.define(statement.getName().text(),
                Value.ofFunc(statement.createCallable(environment, environment.getType(statement.getReturnType()))));
        return null;
    }

    @Override
    public Void visitClassStatement(ClassStatement statement) {

        Map<String, Let> fields = new HashMap<>();
        for (var s : statement.getFields()) {

            fields.put(s.getName().text(),
                    new Let(Value.of(environment.getType(s.getType()), evaluate(s.getInitializer()).get()),
                    s.isMutable()));
        }

        Map<String, FuncCallable> methods = new HashMap<>();
        statement.getMethods().forEach(this::visitFunctionStatement);

        Map<String, FuncCallable> functions = new HashMap<>();
        statement.getStaticFunctions().forEach(this::visitFunctionStatement);

        EClass epsClass = new EClass(statement.getName().text(), methods, functions, fields);
        environment.define(statement.getName().text(), Value.ofType(epsClass));
        return null;
    }

    @Override
    public Void visitStructStatement(StructStatement statement) {
        Map<String, Let> properties = new HashMap<>();
        statement.getProperties().forEach(s -> properties.put(
                        s.getName().text(),
                        new Let(Value.of(environment.getType(s.getType())),
                        s.isMutable())));
        Struct tp = new Struct(statement.getName().text(), properties);
        environment.define(statement.getName().text(), Value.ofType(tp));
        return null;
    }

    @Override
    public Void visitInitStatement(InitStatement initStatement) {
        return null;
    }

    @Override
    public Value visitBinaryExpression(BinaryExpression expression) {

        Value left = evaluate(expression.getLeft());
        Value right = evaluate(expression.getRight());
        Token op = expression.getOp();

        return switch (op.kind()) {
            case NotEqual -> isNotEqual(left, right);
            case Equal -> isEqual(left, right);
            case LessEqual -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() <= (double) right.get());
            }
            case GreaterEqual -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() >= (double) right.get());
            }
            case Less -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() < (double) right.get());
            }
            case Greater -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() > (double) right.get());
            }
            case Minus -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() - (double) right.get());
            }
            case Slash -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() / (double) right.get());
            }
            case Star -> {
                checkNumberOperands(op, left, right);
                yield Value.ofNumber((double) left.get() * (double) right.get());
            }
            case Plus -> {
                if (left.getType() instanceof NativeType.TNumber && right.getType() instanceof NativeType.TNumber)
                    yield Value.ofNumber((double) left.get() + (double) right.get());

                if (left.getType() instanceof NativeType.TString && right.getType() instanceof NativeType.TString)
                    yield Value.ofString(left.get() + (String) right.get());

                if (left.getType() instanceof NativeType.TString && right.getType() instanceof NativeType.TNumber)
                    yield Value.ofString((String) left.get() + (double) right.get());

                if (right.getType() instanceof NativeType.TString && left.getType() instanceof NativeType.TNumber)
                    yield Value.ofString((double) left.get() + (String) right.get());

                if (left.get() == null) {
                    if (right.getType() instanceof NativeType.TString)
                        yield  Value.ofString(Syntax.Word.Null + right.get());

                    if (right.getType() instanceof NativeType.TNumber)
                        yield Value.ofString(Syntax.Word.Null + right.get());
                }

                if (right.get() == null) {
                    if (left.getType() instanceof NativeType.TString)
                        yield Value.ofString(left.get() + Syntax.Word.Null);

                    if (left.getType() instanceof NativeType.TNumber)
                        yield  Value.ofString(left.get() + Syntax.Word.Null);
                }

                throw new InterpretRuntimeException(op, "Operands must be two numbers or two strings.");
            }
            default -> Value.ofVoid();
        };

    }

    @Override
    public Value visitGroupingExpression(GroupingExpression expression) {
        return evaluate(expression.getExpression());
    }

    @Override
    public Value visitLiteralExpression(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public Value visitUnaryExpression(UnaryExpression expression) {
        Value right = evaluate(expression.getRight());
        return switch (expression.getOp().kind()) {
            case Minus -> {
                checkNumberOperand(expression.getOp(), right);
                yield Value.ofNumber(-(double)right.get());
            }
            case Not -> Value.ofBool(!isTruthy(right));
            default -> Value.ofVoid();
        };
    }

    @Override
    public Value visitLetExpression(LetExpression expression) {
        return lookUpVariable(expression.getName(), expression);
    }

    @Override
    public Value visitAssignExpression(AssignExpression expression) {
        Value value = evaluate(expression.value());
        Integer distance = locals.get(expression);
        if (distance != null) environment.assignAt(distance, expression.name(), value);
        else globals.assign(expression.name(), value);
        return value;
    }

    @Override
    public Value visitLogicalExpression(LogicalExpression expression) {
        Value left = evaluate(expression.getLeft());

        if (expression.getOp().kind() == Kind.LogicalOr && isTruthy(left)) {
            return left;
        } else if (!isTruthy(left)) {
            return left;
        }

        return evaluate(expression.getRight());
    }

    @Override
    public Value visitCallExpression(CallExpression expression) {
        Value value = evaluate(expression.getCallable());

        List<Value> arguments = expression.getArguments().stream().toList()
                        .stream().map(this::evaluate).toList();

        if (value.get() instanceof Callable callable) {
            if (arguments.size() != callable.arity()) {
                throw new InterpretRuntimeException(expression.getParen(), "Expected " +
                        callable.arity() + " arguments but got " +
                        arguments.size() + ".");
            }
            return callable.call(this, arguments);
        }

        throw new InterpretRuntimeException(expression.getParen(), "Can only call functions and classes.");
    }

    @Override
    public Value visitAnonymousFuncExpression(AnonymousFuncExpression expression) {
        return Value.ofFunc(expression.getStatement().createCallable(environment,
                        environment.getType(expression.getStatement().getReturnType())));
    }

    @Override
    public Value visitGetterExpression(GetterExpression expression) {
        Value value = evaluate(expression.getValue());

        if (value.get() instanceof Instance instance) {
            return instance.get(expression.getName());
        }
        throw new InterpretRuntimeException(expression.getName(), "Only instances have properties.");
    }

    @Override
    public Value visitSetterExpression(SetterExpression expression) {
        Value object = evaluate(expression.getObject());

        if (object.get() instanceof Instance instance) {
            Value value = evaluate(expression.getValue());
            instance.set(expression.getName(), value);
            return value;
        }
        throw new InterpretRuntimeException(expression.getName(), "Only instances have fields");
    }

    @Override
    public Value visitInitStructExpression(InitSructExpression expression) {

        Map<String, Value> attributions = new HashMap<>();

        expression.getStatements().forEach(stmt -> {
            if (stmt instanceof InitStatement is)
                attributions.put(is.getName().text(), evaluate(is.getValue()));
        });

        if (environment.getType(expression.getType()) instanceof Struct struct) {
            check(expression.getType(), struct, attributions);
            return Value.of(struct, new InstanceStruct(struct, attributions));
        }

        throw new InterpretRuntimeException(expression.getType(),
                "Not recognize the type '"+expression.getType().text()+"'.");
    }

    public Value evaluate(Expression expression) {
        if (expression == null) return Value.ofVoid();
        return expression.accept(this);
    }

    public void execute(Statement statement) {
        statement.accept(this);
    }

    public void checkNumberOperand(Token operator, Value operand) {
        if (operand.getType() instanceof NativeType.TNumber) return;
        throw new InterpretRuntimeException(operator, "Operand must be a number.");
    }

    public void checkNumberOperands(Token operator, Value left, Value right) {
        if (left.getType() instanceof NativeType.TNumber && right.getType() instanceof NativeType.TNumber) return;
        throw new InterpretRuntimeException(operator, "Operands must be numbers.");
    }

    public String stringify(Value value) {
        Object o = value.get();
        if (o == null) return Syntax.Word.Null;

        if (value.getType() instanceof NativeType.TNumber) {
            String text_ = o.toString();
            if (text_.endsWith(".0")) {
                text_ = text_.substring(0, text_.length() - 2);
            }
            return text_;
        }
        return o.toString();
    }

    public void resolve(Expression expr, int depth) {
        locals.put(expr, depth);
    }

    private Value lookUpVariable(Token name, Expression expression) {
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

    private static void check(Token name, Struct struct, Map<String, Value> attributions) {
        for (Map.Entry<String, Value> stringObjectEntry : attributions.entrySet()) {
            if (!struct.getProperties().containsKey(stringObjectEntry.getKey())) {
                throw new InterpretRuntimeException(name,
                        "'"+name.text()+"' not correspond at '" + struct.getName()+"'.");
            }
        }
    }

}
