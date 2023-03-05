package epsilon.type;

public interface Type {

    String name();

    default boolean isInstance(Type type) {
        return this.getClass().isInstance(type);
    }

}
