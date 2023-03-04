package epsilonc.object;

import epsilonc.syntax.Token;

public interface Instance {

    void set(Token name, Value value);

    Value get(Token name);

}
