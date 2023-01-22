package org.epsilon;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public static Parser createParser(String text) {
        return new Parser(new Lexer(text));
    }

    public Parser(Lexer lexer) {
        this.tokens = lexer.scan();
    }

    public void showTokens() {
        tokens.forEach(System.out::println);
    }

    public Expression parse() {
        try {
            return expression();
        } catch (ParseException error) {
            return null;
        }
    }

    private Expression expression() {
        return equality();
    }

    private Expression equality() {
        Expression expr = comparison();

        while (match(Kind.NotEqual, Kind.Equal)) {
            Token operator = previous();
            Expression right = comparison();
            expr = new Expression.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expression comparison() {
        Expression expr = term();
        while (match(Kind.Greater, Kind.GreaterEqual, Kind.Less, Kind.LessEqual)) {
            Token operator = previous();
            Expression right = term();
            expr = new Expression.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expression term() {
        Expression expr = factor();

        while (match(Kind.Minus, Kind.Plus)) {
            Token operator = previous();
            Expression right = factor();
            expr = new Expression.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expression factor() {
        Expression expr = unary();

        while (match(Kind.Slash, Kind.Star)) {
            Token operator = previous();
            Expression right = unary();
            expr = new Expression.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expression unary() {
        if (match(Kind.Not, Kind.Minus)) {
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator, right);
        }

        return primary();
    }

    private Expression primary() {
        if (match(Kind.False)) return new Expression.Literal(false);
        if (match(Kind.True)) return new Expression.Literal(true);
        if (match(Kind.Nil)) return new Expression.Literal(null);

        if (match(Kind.Number, Kind.String)) {
            return new Expression.Literal(previous().value());
        }

        if (match(Kind.OpenParenthesis)) {
            Expression expr = expression();
            consume(Kind.CloseParenthesis, "Expect ')' after expression.");
            return new Expression.Grouping(expr);
        }
        throw error(peek(), "Expect expression.");
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().kind() == Kind.Semicolon) return;
            switch (peek().kind()) {
                case ClassSymbol,
                     FuncSymbol,
                     LetSymbol,
                     ForSymbol,
                     IfSymbol,
                     WhileSymbol,
                     ReturnSymbol -> { return; }
            }
            advance();
        }
    }

    private Token consume(Kind type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseException error(Token token, String message) {
        if (token.kind() == Kind.EndToken)
            System.err.println("Error" + ": " + message + " at end");
        else
            System.err.println("Error" + ": '" + token.text() + "' | " + message + " at "+ "[" + token.line() + ","+ token.col() + "]");

        return new ParseException();
    }


    private boolean match(Kind... kinds) {
        for (Kind kind : kinds)
            if (check(kind)) {
                advance();
                return true;
            }
        return false;
    }

    private boolean check(Kind kind) {
        if (isAtEnd()) return false;
        return peek().kind() == kind;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().kind() == Kind.EndToken;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

}
