package epsilon.utils;

import epsilon.object.Value;
import epsilon.type.NativeType;

public class Utils {

    public static String unescapeString(String s){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            switch (s.charAt(i)) {
                case '\n' -> sb.append("\\n");
                case '\t' -> sb.append("\\t");
                default -> sb.append(s.charAt(i));
            }
        return sb.toString();
    }

    public static Value isEqual(Value a, Value b) {
        if (a.get() == null && b.get() == null) return Value.of(NativeType.Bool, true);
        if (a.get() == null) return Value.of(NativeType.Bool, false);
        return Value.of(NativeType.Bool, a.get().equals(b.get()));
    }

    public static Value isNotEqual(Value a, Value b) {
        return Value.ofBool(!(boolean) isEqual(a, b).get());
    }

    public static boolean isTruthy(Value value) {
        if (value.get() == null) return false;
        if (value.getType() == NativeType.Bool) return (boolean) value.get();
        return true;
    }

    public static boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    public static boolean isAlphaNumeric(char c) {
        return Character.isDigit(c) || isAlpha(c);
    }
}
