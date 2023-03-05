package epsilon.object;

import epsilon.syntax.Token;

public interface Instance {

    void set(Token name, Value value);

    Value get(Token name);

}
