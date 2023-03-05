package epsilon.resolver;

import epsilon.Environment;
import epsilon.core.*;
import epsilon.expression.*;
import epsilon.statement.*;
import epsilon.object.*;
import epsilon.syntax.Kind;
import epsilon.syntax.Syntax;
import epsilon.syntax.Token;
import epsilon.type.NativeType;
import epsilon.type.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static epsilon.utils.Utils.*;

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
        evaluate(statement.expression());
        return null;
    }
    
    @Override
    public Void visitReturnStatement(ReturnStatement statement) {
        Value value = statement.getValue() == null ? Value.ofVoid() : evaluate(statement.getValue());
        Token returnTkFunc = statement.getFunctionStatement().returnType();
        String txt = returnTkFunc == null ? NativeType.Void.name() : returnTkFunc.text();
        if(!value.getType().isInstance(environment.getType(returnTkFunc))) {
            throw new InterpretRuntimeException(statement.getKeyword(), "Can't return a type '"+ value.getType().name() +
                            "' because we expect a type '" + txt + "'.");
        }

        throw new ReturnRuntimeException(value);
    }

    @Override
    public Void visitLetStatement(LetStatement statement) {
        Value value = null;
        if (statement.initializer() != null)
            value = evaluate(statement.initializer());

        Type type = environment.getType(statement.type());

        if (!(type instanceof NativeType.TUndefined) && value != null && !type.isInstance(value.getType()))
            throw new InterpretRuntimeException(statement.name(), "At decleration '"+statement.name().text() + "'," +
                    " impossible to init because we expect '"+ value.getType().name() +"' as type. And it is declared by a '" +
                    statement.type().text() +"'.");

        value = value == null ? Value.of(type) : value;
        environment.define(statement.name().text(), value, statement.mutable());
        return null;
    }

    @Override
    public Void visitBlockStatement(BlockStatement statement) {
        executeBlock(statement.statements(), new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement statement) {
        if (isTruthy(evaluate(statement.condition()))) {
            execute(statement.thenBranch());
        } else if (statement.elseBranch() != null) {
            execute(statement.elseBranch());
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement statement) {
        while (isTruthy(evaluate(statement.condition()))) {
            execute(statement.body());
        }
        return null;
    }

    @Override
    public Void visitFunctionStatement(FunctionStatement statement) {
        environment.define(statement.name().text(), statement.createValue(environment));
        return null;
    }

    @Override
    public Void visitClassStatement(ClassStatement statement) {
        environment.define(statement.getName().text(), Value.ofVoid(), true);
        Map<String, Let> fields = new HashMap<>();
        for (var s : statement.getFields()) {

            fields.put(s.name().text(),
                    new Let(Value.of(environment.getType(s.type()), evaluate(s.initializer()).get()),
                    s.mutable()));
        }

        Map<Prototype, FuncCallable> methods = new HashMap<>();
        statement.getMethods().forEach(methodStmt -> {
            var types = methodStmt.paramsType().stream().map(token -> environment.getType(token)).toList();
            methods.put(new Prototype(methodStmt.name().text(), types),
                    (FuncCallable) methodStmt.createValue(environment).get());
        });

        Map<Prototype, FuncCallable> constructors = new HashMap<>();
        for (FunctionStatement constructor : statement.getConstructors()) {
            var types = constructor.paramsType().stream().map(token -> environment.getType(token)).toList();
            constructors.put(new Prototype(constructor.name().text(), types),
                    (FuncCallable) constructor.createValue(environment).get());
        }
        
        EClass epsClass = new EClass(statement.getName().text(), constructors, methods, fields);
        environment.assign(statement.getName(), Value.ofType(epsClass));
        return null;
    }

    @Override
    public Void visitStructStatement(StructStatement statement) {
        environment.define(statement.name().text(), Value.ofVoid(), true);
        Map<String, Let> properties = new HashMap<>();
        statement.properties().forEach(s -> properties.put(s.name().text(),
                new Let(Value.of(environment.getType(s.type())), s.mutable())));
        Struct tp = new Struct(statement.name().text(), properties);
        environment.assign(statement.name(), Value.ofType(tp));
        return null;
    }

    @Override
    public Void visitInitStatement(InitStatement initStatement) {
        return null;
    }

    @Override
    public Value visitBinaryExpression(Expression.Binary expression) {

        Value left = evaluate(expression.left());
        Value right = evaluate(expression.right());


        Token op = expression.op();

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

                if (left.getType() instanceof NativeType.TNumber && right.getType() instanceof NativeType.TString)
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
    public Value visitGroupingExpression(Expression.Grouping expression) {
        return evaluate(expression.expression());
    }

    @Override
    public Value visitLiteralExpression(LiteralExpression expression) {
        return expression.getValue();
    }

    @Override
    public Value visitUnaryExpression(Expression.Unary expression) {
        Value right = evaluate(expression.right());
        return switch (expression.op().kind()) {
            case Minus -> {
                checkNumberOperand(expression.op(), right);
                yield Value.ofNumber(-(double)right.get());
            }
            case Not -> Value.ofBool(!isTruthy(right));
            default -> Value.ofVoid();
        };
    }

    @Override
    public Value visitLetExpression(Expression.Let expression) {
        return lookUpVariable(expression.name(), expression);
    }

    @Override
    public Value visitAssignExpression(AssignExpression expression) {

        Value value = evaluate(expression.value());
        Integer distance = locals.get(expression);
        Type type = environment.getType(expression.name());

        if (!(type instanceof NativeType.TUndefined) && !type.isInstance(value.getType()))
            throw new InterpretRuntimeException(expression.name(), "At declaration '"+expression.name().text() + "'," +
                    " impossible to init because we expect '"+ type.name() +"' as type.");

        if (distance != null)
            environment.assignAt(distance, expression.name(), value);
        else
            globals.assign(expression.name(), value);
        return value;
    }

    @Override
    public Value visitLogicalExpression(Expression.Logical expression) {
        Value left = evaluate(expression.left());

        if (expression.op().kind() == Kind.LogicalOr && isTruthy(left)) {
            return left;
        } else if (!isTruthy(left)) {
            return left;
        }

        return evaluate(expression.right());
    }

    @Override
    public Value visitCallExpression(CallExpression expression) {

        Value value = evaluate(expression.getCallable());

        List<Value> arguments = expression.getArguments().stream().toList()
                        .stream().map(this::evaluate).toList();

        System.out.println(value);

        if (value.get() instanceof List) {
            List<Value> values = (List<Value>) value.get();
            List<Callable> functions = values.stream().map(value1 -> (Callable) value1.get()).toList();
            for (Callable function : functions) {
                if (function.prototype().sameArgsT(arguments.stream().map(Value::getType).toList())) {
                    return function.call(this, arguments);
                }
            }
            throw new InterpretRuntimeException(expression.getParen(), "Function does not exist.");
        }
        if (value.get() instanceof Callable callable) {
            if (callable.prototype().sameArgsT(arguments.stream().map(Value::getType).toList())) {
                return callable.call(this, arguments);
            }
            throw new InterpretRuntimeException(expression.getParen(), "Function does not exist.");
        }

        throw new InterpretRuntimeException(expression.getParen(), "Can only call functions and classes.");
    }

    @Override
    public Value visitAnonymousFuncExpression(Expression.AnonymousFunc expression) {
        return expression.statement().createValue(environment);
    }

    @Override
    public Value visitGetterExpression(GetterExpression expression) {
        Value typeObj = evaluate(expression.getObject());

        if (typeObj.get() instanceof Instance instance) {
            return instance.get(expression.getName());
        }

        throw new InterpretRuntimeException(expression.getName(), "Only instances have properties.");
    }

    @Override
    public Value visitSetterExpression(Expression.Setter expression) {
        Value object = evaluate(expression.object());

        if (object.get() instanceof Instance instance) {
            Value value = evaluate(expression.value());
            Type oldType = instance.get(expression.name()).getType();
            if (!value.getType().isInstance(oldType))
                throw new InterpretRuntimeException(expression.name(),
                        "At decleration '"+ object.getType().name() + "." +expression.name().text() + "'," +
                        " impossible to init because we expect '"+ oldType.name() +"' as type.");
            instance.set(expression.name(), value);
            return value;
        }
        throw new InterpretRuntimeException(expression.name(), "Only instances have fields");
    }

    @Override
    public Value visitInitStructExpression(InitSructExpression expression) {

        Map<String, Value> attributions = new HashMap<>();

        expression.getStatements().forEach(stmt -> {
            if (stmt instanceof InitStatement is)
                attributions.put(is.name().text(), evaluate(is.value()));
        });

        if (environment.getType(expression.getType()) instanceof Struct struct) {
            check(expression.getType(), struct, attributions);
            return Value.of(struct, new InstanceStruct(struct, attributions));
        }

        throw new InterpretRuntimeException(expression.getType(),
                "Not recognize the type '"+expression.getType().text()+"'.");
    }

    @Override
    public Value visitThisExpression(Expression.This aThis) {
        return lookUpVariable(aThis.kw(), aThis);
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
