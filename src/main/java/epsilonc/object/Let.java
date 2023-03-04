package epsilonc.object;

import epsilonc.type.Type;

public class Let {

    private Value value;
    private final boolean isMutable;

    public Let(Value value, boolean isMutable) {
        this.value = value;
        this.isMutable = isMutable;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isMutable() {
        return isMutable;
    }

    public Type getType() {
        return value.getType();
    }

    @Override
    public String toString() {
        return "Let{" +
                "value=" + value.get() +
                "type=" + getType() +
                ", isMutable=" + isMutable +
                '}';
    }
}
