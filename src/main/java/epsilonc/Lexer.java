package epsilonc;

import epsilonc.core.BindingManager;
import epsilonc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static epsilonc.Syntax.Symbol;

public class Lexer {

    static boolean hadError = false;
    private final String text;
    private int position;
    private int start;
    private int line;
    private int col;
    private final List<Token> tokens;

    public Lexer(final String text) {
        this.tokens = new ArrayList<>();
        this.text = text;
    }

    public List<Token> scan() {
        while (!isAtEnd()) {

            start = position;
            nextToken();
        }
        tokens.add(new Token(Kind.EndToken, null, "\0", line, col));
        return tokens;
    }

    private void nextToken() {
        final String s = String.valueOf(advance());
        switch (s) {
            case Symbol.Less -> addToken(match(Symbol.Equal) ? Kind.LessEqual : Kind.Less);
            case Symbol.Greater -> addToken(match(Symbol.Equal) ? Kind.GreaterEqual : Kind.Greater);
            case Symbol.Comma -> addToken(Kind.CommaSymbol);
            case Symbol.BackQuote -> createStringToken(Symbol.BackQuote);
            case Symbol.DoubleQuote -> createStringToken(Symbol.DoubleQuote);
            case Symbol.OpenParenthesis -> addToken(Kind.OpenParenthesis);
            case Symbol.CloseParenthesis -> addToken(Kind.CloseParenthesis);
            case Symbol.Bang -> addToken(match(Symbol.Equal) ? Kind.NotEqual : Kind.Not);
            case Symbol.Equal -> addToken(match(Symbol.Equal) ? Kind.Equal : Kind.Assign);
            case Symbol.Ampersand -> addToken(match(Symbol.Ampersand) ? Kind.LogicalAnd : Kind.BinaryAnd);
            case Symbol.Pipeline -> addToken(match(Symbol.Pipeline) ? Kind.LogicalOr : Kind.BinaryOr);
            case Symbol.OpenBrackets -> addToken(Kind.OpenBracket);
            case Symbol.CloseBrackets -> addToken(Kind.CloseBracket);
            case Symbol.Dot -> addToken(Kind.Period);
            case Symbol.Colon -> addToken(Kind.Colon);
            case Symbol.Semicolon -> addToken(Kind.Semicolon);
            case Symbol.MinusOp -> addToken(match(Symbol.Greater) ? Kind.ArrowSymbol : Kind.Minus);
            case Symbol.PlusOp -> addToken(Kind.Plus);
            case Symbol.StartOp -> addToken(Kind.Star);
            case Symbol.SlashOp -> addSlashToken();
            case Symbol.BackslashN -> { line++; col = 0; }
            case Symbol.Space, Symbol.BackslashR, Symbol.BackslashT -> { }
            default -> addDefaultToken(s);
        }
    }

    private void addDefaultToken(String c) {
        if (Character.isDigit(c.charAt(0))) addNumberToken();
        else if (Utils.isAlpha(c.charAt(0))) addIdentifierToken();
        else report("Unexpected character.");
    }

    private void addNumberToken() {
        while (Character.isDigit(peek())) next();
        if (peek() == Symbol.Dot.charAt(0) && Character.isDigit(peekNext())) {
            advance();
            while (Character.isDigit(peek())) advance();
        }
        addToken(Kind.Number, Double.parseDouble(text.substring(start, position)));
    }

    private void addIdentifierToken() {
        while (Utils.isAlphaNumeric(peek())) next();
        Kind type = BindingManager.getKeywords().get(text.substring(start, position));
        addToken(type == null ? Kind.Identifier : type);
    }

    private void createStringToken(String q) {
        while (peek() != q.charAt(0) && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            report("Unterminated string.");
            return;
        }
        next();
        String value = text.substring(start + 1, position - 1);
        addToken(Kind.String, value);
    }

    private void addSlashToken() {
        if (match(Symbol.SlashOp)) {
            while (peek() != '\n' && !isAtEnd()) next();
        } else
            addToken(Kind.Slash);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return text.charAt(position);
    }

    private char peekNext() {
        if (position + 1 >= text.length()) return '\0';
        return text.charAt(position + 1);
    }

    private char advance() {
        return text.charAt(next());
    }

    private int next() {
        col++;
        return position++;
    }

    private int preNext() {
        return ++position;
    }

    private char currentChar() {
        return position >= text.length() ? '\0' : text.charAt(position);
    }

    private boolean isAtEnd() {
        return position >= text.length();
    }

    private void report(String message) {
        System.err.println("Error" + ": " + message + " at "+ "[" + line + ","+ col+ "]");
        hadError = true;
    }

    private void addToken(Kind kind) {
        addToken(kind, null);
    }

    private void addToken(Kind kind, Object value) {
        String text_ = text.substring(start, position);
        tokens.add(new Token(kind, value, text_, line, col));
    }

    private boolean match(String expected) {
        if (isAtEnd()) return false;
        if (!String.valueOf(text.charAt(position)).equals(expected)) return false;
        next();
        return true;
    }

}
