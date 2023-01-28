package epsilonc.object;

import epsilonc.syntax.Token;

public interface Instance {

    void set(Token name, Object value);

    Object get(Token name);

}
