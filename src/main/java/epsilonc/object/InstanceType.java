package epsilonc.object;

import epsilonc.Token;

public class InstanceType implements Instance {

    private Type type;

    public InstanceType(Type type) {
        this.type = type;
    }

    @Override
    public void set(Token name, Object value) {

    }

    @Override
    public Object get(Token name) {
        return null;
    }

}
