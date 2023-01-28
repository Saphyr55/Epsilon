package epsilonc.core;

import epsilonc.syntax.Kind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

    Kind kind();

}
