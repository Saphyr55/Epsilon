package epsilonc.object;

public class Let {

    private Object value;
    private final String type;
    private final boolean isMutable;

    public Let(Object value, String type, boolean isMutable) {
        this.value = value;
        this.isMutable = isMutable;
        this.type = type;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Let{" +
                "value=" + value +
                ", isMutable=" + isMutable +
                '}';
    }
}
