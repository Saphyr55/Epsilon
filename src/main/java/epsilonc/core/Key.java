package epsilonc.core;

import epsilonc.Kind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Key {

    Kind kind();

}