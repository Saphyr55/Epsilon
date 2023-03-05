package epsilon.object;

import epsilon.type.NativeType;
import epsilon.type.Type;

public class Value {

    private Type type;
    private Object value;

    public Value(Type type, Object value) {
        if (value instanceof Value) throw new RuntimeException();
        this.type = type;
        this.value = value;
    }

    public Value move() {
        return new Value(type, value);
    }

    public static Value of(Type type, Object value) {
        return new Value(type, value);
    }

    public static Value ofType(Type value) {
        return Value.of(value, value);
    }

    public static Value of(Type type) {
        return new Value(type, null);
    }

    public static Value ofBool(Object value) {
        return Value.of(NativeType.Bool, value);
    }

    public static Value ofTrue() {
        return Value.of(NativeType.Bool, true);
    }

    public static Value ofFalse() {
        return Value.of(NativeType.Bool, false);
    }

    public static Value ofFunc(Object value) {
        return Value.of(NativeType.Func, value);
    }

    public static Value ofNumber(Object value) {
        return Value.of(NativeType.Number, value);
    }

    public static Value ofString(Object value) {
        return Value.of(NativeType.String, value);
    }

    public static Value ofVoid() {
        return Value.of(NativeType.Void, null);
    }

    public Object get() {
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Value{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
