package org.epsilon;

import static java.text.MessageFormat.format;

public record Token(Kind kind, Object value, String text, int line, int col) {

    public boolean isEmpty() {
        return kind == Kind.WhitespaceToken;
    }

    public boolean isEnd() {
        return kind == Kind.EndToken;
    }

    @Override
    public String toString() {
        return Utils.unescapeString(format("Token(kind=''{0}'' text=''{1}'' pos=[{2},{3}])", kind, text, line, col));
    }

}
