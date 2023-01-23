package epsilonc.statement;

import epsilonc.Token;

public class BreakStatement implements Statement{

    private Statement loop;

    public BreakStatement() {
        this.loop = loop;
    }

    public Statement getLoop() {
        return loop;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitBreakStatement(this);
    }


}
