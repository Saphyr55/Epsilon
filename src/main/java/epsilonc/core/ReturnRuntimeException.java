package epsilonc.core;

public class ReturnRuntimeException extends RuntimeException {

    private final Object value;

    public ReturnRuntimeException(Object value) {
        super(null, null, false, false);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

}
