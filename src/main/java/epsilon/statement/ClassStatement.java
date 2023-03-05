package epsilon.statement;

import epsilon.syntax.Token;

import java.util.List;

public class ClassStatement implements Statement {

    private final Token name;
    private final List<FunctionStatement> methods;
    private final List<FunctionStatement> staticFunctions;
    private final List<LetStatement> fields;

    public ClassStatement(Token name, List<LetStatement> fields, List<FunctionStatement> methods, List<FunctionStatement> functions) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
        this.staticFunctions = functions;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitClassStatement(this);
    }

    public List<LetStatement> getFields() {
        return fields;
    }

    public Token getName() {
        return name;
    }

    public List<FunctionStatement> getMethods() {
        return methods;
    }

    public List<FunctionStatement> getStaticFunctions() {
        return staticFunctions;
    }

}
