package epsilonc.stataments;

public interface Statement {

    <R> R accept(StatementVisitor<R> expressionVisitor);

}
