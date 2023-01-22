package org.epsilon.core;

import org.epsilon.Kind;

public @interface Matching {
    String match();
    Kind kind();
}
