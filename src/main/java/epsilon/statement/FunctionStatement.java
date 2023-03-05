package epsilon.statement;

import epsilon.Environment;
import epsilon.object.Value;
import epsilon.syntax.Token;
import epsilon.object.FuncCallable;
import epsilon.type.Type;

import java.util.List;

public class FunctionStatement implements Statement {

    private Token name;
    private List<Token> paramsId;
    private List<Token> paramsType;
    private List<Statement> body;
    private Token returnType;

    public FunctionStatement(Token name, List<Token> paramsId, List<Token> paramsType, List<Statement> body, Token returnType) {
        this.name = name;
        this.paramsId = paramsId;
        this.paramsType = paramsType;
        this.body = body;
        this.returnType = returnType;
    }

    public FuncCallable createCallable(Environment environment, Type returnTypeDeclaration) {
        return new FuncCallable(environment, this, returnTypeDeclaration);
    }

    public Value createValue(Environment environment) {
        getParamsType().forEach(environment::getType);
        Type returnType = environment.getType(getReturnType());
        for (Statement stmt : body) {
            if (stmt instanceof ReturnStatement rs) rs.setFunctionStatement(this);
        }
        return Value.ofFunc(createCallable(environment, returnType));
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitFunctionStatement(this);
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParamsId() {
        return paramsId;
    }

    public List<Token> getParamsType() {
        return paramsType;
    }

    public List<Statement> getBody() {
        return body;
    }

    public Token getReturnType() {
        return returnType;
    }
}
