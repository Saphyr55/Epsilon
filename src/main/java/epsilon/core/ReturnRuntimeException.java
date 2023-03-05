package epsilon.core;

import epsilon.object.Value;

public class ReturnRuntimeException extends RuntimeException {

    private final Value value;

    public ReturnRuntimeException(Value value) {
        super(null, null, false, false);
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

}
