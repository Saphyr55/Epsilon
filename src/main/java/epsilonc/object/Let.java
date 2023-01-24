package epsilonc.object;

public class Let {

    private Object value;
    private final boolean isMutable;

    public Let(Object value, boolean isMutable) {
        this.value = value;
        this.isMutable = isMutable;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isMutable() {
        return isMutable;
    }

    @Override
    public String toString() {
        return "Let{" +
                "value=" + value +
                ", isMutable=" + isMutable +
                '}';
    }
}
