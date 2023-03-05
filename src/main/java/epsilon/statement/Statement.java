package epsilon.statement;

public interface Statement {

    <R> R accept(StatementVisitor<R> visitor);

}
