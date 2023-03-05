package epsilon.core;

import epsilon.syntax.Kind;
import epsilon.syntax.Syntax;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BindingManager {

    private static final Map<String, Kind> keywords = new HashMap<>();
    static {
        try {
            for (Field field : Syntax.Word.class.getDeclaredFields()) {
                keywords.put((String) field.get(null), field.getAnnotation(Bind.class).kind());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Kind> getKeywords() {
        return keywords;
    }

}
