package org.epsilon;

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

    public static boolean isAlphaNumeric(String s) {
        return s != null && s.chars().allMatch(Character::isLetterOrDigit);
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
