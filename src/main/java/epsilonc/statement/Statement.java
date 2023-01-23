package epsilonc.statement;

public interface Statement {

    <R> R accept(StatementVisitor<R> expressionVisitor);

}
