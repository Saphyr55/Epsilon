package org.epsilon.core;

import org.epsilon.Kind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Key {

    Kind kind();

}
