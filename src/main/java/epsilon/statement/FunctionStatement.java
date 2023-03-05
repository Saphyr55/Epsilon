package epsilon.statement;

import epsilon.Environment;
import epsilon.object.Value;
import epsilon.syntax.Token;
import epsilon.object.FuncCallable;
import epsilon.type.NativeType;
import epsilon.type.Type;

import java.util.List;

public record FunctionStatement(Token name,
                                List<Token> paramsId,
                                List<Token> paramsType,
                                List<Statement> body,
                                Token returnType) implements Statement {

    public FuncCallable createCallable(Environment environment, Type returnTypeDeclaration) {
        return new FuncCallable(environment, this, returnTypeDeclaration);
    }

    public Value createValue(Environment environment) {
        paramsType().forEach(environment::getType);
        Type returnType_ = NativeType.Void;
        if (returnType != null)
            returnType_ = environment.getType(returnType());

        for (Statement stmt : body) {
            if (stmt instanceof ReturnStatement rs) rs.setFunctionStatement(this);
        }
        return Value.ofFunc(createCallable(environment, returnType_));
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitFunctionStatement(this);
    }
}
