package org.epsilon.core;

import org.epsilon.Kind;
import org.epsilon.Syntax;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private static final Map<String, Kind> keywords = new HashMap<>();
    static {
        try {
            for (Field declaredField : Syntax.Word.class.getDeclaredFields()) {
                keywords.put((String) declaredField.get(null),
                        declaredField.getAnnotation(Key.class).kind());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Kind> getKeywords() {
        return keywords;
    }

}
