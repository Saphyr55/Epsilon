package epsilon.core;

import epsilon.syntax.Kind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

    Kind kind();

    String pred() default "";
}
