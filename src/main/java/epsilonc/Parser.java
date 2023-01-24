package epsilonc;

import epsilonc.core.ParseException;
import epsilonc.expression.*;
import epsilonc.statement.*;

import java.util.*;

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

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) statements.add(createDeclaration());
        return statements;
    }

    private Statement createDeclaration() {
        try {
            if (match(Kind.ClassKw)) return createClassStatement();
            if (match(Kind.FuncKw)) return createFunction();
            if (match(Kind.LetKw)) return createLetDeclaration();
            if (match(Kind.TypeKw)) return createTypeDeclaration();

            return statement();
        } catch (ParseException e) {
            synchronize();
            return null;
        }
    }

    private Statement createTypeDeclaration() {
        Token name = consume(Kind.Identifier, "Expected a name for declaring type.");
        consume(Kind.Assign, "Expected a '=' after naming type.");
        consume(Kind.OpenBracket, "Expected open bracket before declaring type body.");
        List<LetStatement> properties = new ArrayList<>();
        while (!check(Kind.CloseBracket) && !isAtEnd())  {
            properties.add(createPropertyDeclaration());
        }
        consume(Kind.CloseBracket, "Expected close bracket after declaring type body.");
        return new TypeStatement(name, properties);
    }

    private Statement createClassStatement() {

        Token name = consume(Kind.Identifier, "Expected a name for declaring class.");
        consume(Kind.OpenBracket, "Expected '{' before class body.");

        List<FunctionStatement> methods = new ArrayList<>();
        List<FunctionStatement> functions = new ArrayList<>();
        List<LetStatement> fields = new ArrayList<>();

        while (!check(Kind.CloseBracket) && !isAtEnd()) {

            if (match(Kind.Identifier) && previous().text().equals(Syntax.Contextual.Method))
                methods.add(createFunction());
            else if (match(Kind.FuncKw))
                functions.add(createFunction());
            else if (match(Kind.LetKw))
                fields.add((LetStatement) createLetDeclaration());

        }

        consume(Kind.CloseBracket, "Expected '}' after class body.");
        return new ClassStatement(name, fields, methods, functions);
    }

    private FunctionStatement createFunction() {
        return (FunctionStatement) createFunction(false);
    }

    private FunctionStatement createAnonymousFunction() {
        return (FunctionStatement) createFunction(true);
    }

    private Statement createFunction(boolean isAnonymous) {

        Token name = null;
        if (!isAnonymous)
            name = consume(Kind.Identifier, "Expected a function name.");

        consume(Kind.OpenParenthesis, "Expect '(' after function name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(Kind.CloseParenthesis)) {
            do {
                if (parameters.size() >= 255)
                    throw report(peek(), "Can't have more than 255 parameters.");

                parameters.add(consume(Kind.Identifier, "Expect parameter name."));
            } while (match(Kind.CommaSymbol));
        }

        consume(Kind.CloseParenthesis, "Expect ')' after parameters.");
        consume(Kind.OpenBracket, "Expect '{' before function body.");

        List<Statement> body = createBlock();

        return new FunctionStatement(name, parameters, body);
    }

    private Statement createLetDeclaration() {

        boolean mutable = match(Kind.MutKw);
        Token name = consume(Kind.Identifier, "Expect a name before let assignment.");
        Token type = null;
        Expression initializer = null;
        if (match(Kind.Colon)) {
            consume(Kind.Identifier, "Expected declaring type after ':'");
            type = previous();
        }
        if (match(Kind.Assign)) initializer = expression();
        consume(Kind.Semicolon, "Expect ';' after let declaration.");
        return new LetStatement(name, type, initializer, mutable);
    }

    private LetStatement createPropertyDeclaration() {
        boolean mutable = match(Kind.MutKw);
        Token name = consume(Kind.Identifier, "Expected a name for a property");
        consume(Kind.Colon, "Expected declaring type after naming it");
        consume(Kind.Identifier, "Expected declaring type after naming it");
        Token type = previous();
        consume(Kind.Semicolon, "Expect ';' after property declaration.");
        return new LetStatement(name, type, new LiteralExpression(null), mutable);
    }

    private Statement statement() {
        if (match(Kind.ForKw)) return createForStatement();
        if (match(Kind.WhileKw)) return createWhileStatement();
        if (match(Kind.IfKw)) return createIfStatement();
        if (match(Kind.ReturnKw)) return createReturnStatement();
        if (match(Kind.OpenBracket)) return new BlockStatement(createBlock());
        return createExpressionStatement();
    }

    private Statement createReturnStatement() {
        Token kw = previous();
        Expression value = check(Kind.Semicolon) ? null : expression();
        consume(Kind.Semicolon, "Expected ';' after return value.");
        return new ReturnStatement(kw, value);
    }

    private Statement createForStatement() {

        consume(Kind.OpenParenthesis, "Expected '(' after 'for' loop");

        Statement init;
        if (match(Kind.Semicolon)) init = null;
        else if (match(Kind.LetKw)) init = createLetDeclaration();
        else init = createExpressionStatement();

        Expression condition = check(Kind.Semicolon) ? null : expression();
        consume(Kind.Semicolon, "Expected ';' after loop condition");

        Expression increment = check(Kind.CloseParenthesis) ? null : expression();
        consume(Kind.CloseParenthesis, "Expected ')' to close for loop");

        Statement body = statement();

        if (increment != null)
            body = new BlockStatement(Arrays.asList(body, new ExpressionStatement(increment)));

        if (condition == null)
            condition = new LiteralExpression(true);

        body = new WhileStatement(condition, body);

        if (init != null)
            body = new BlockStatement(Arrays.asList(init, body));

        return body;
    }

    private Statement createWhileStatement() {
        consume(Kind.OpenParenthesis, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(Kind.CloseParenthesis, "Expect ')' after condition.");
        return new WhileStatement(condition, statement());
    }

    private Statement createIfStatement() {

        consume(Kind.OpenParenthesis, "Expected '(' after if.");
        Expression condition = expression();
        consume(Kind.CloseParenthesis, "Expected ')' after if condition.");

        Statement thenBranch = statement();
        Statement elseBranch = match(Kind.ElseKw) ? statement() : null;

        return new IfStatement(condition, thenBranch, elseBranch);
    }

    private List<Statement> createBlock() {
        List<Statement> statements = new ArrayList<>();
        while (!check(Kind.CloseBracket) && !isAtEnd()) {
            statements.add(createDeclaration());
        }
        consume(Kind.CloseBracket,  "Expect '}' after block.");
        return statements;
    }

    private Statement createExpressionStatement() {
        return new ExpressionStatement(expressionConsumeStatement());
    }

    private Expression expressionConsumeStatement() {
        Expression value = expression();
        consume(Kind.Semicolon, "Expect ';' after value.");
        return value;
    }

    private Expression expression() {
        return assignment();
    }

    private Expression assignment() {
        Expression expression = or();

        if (match(Kind.Assign)) {

            Token equals = previous();
            Expression value = or();

            if (expression instanceof LetExpression le)
                return new AssignExpression(le.getName(), value);
            else if (expression instanceof GetterExpression ge)
                return new SetterExpression(ge.getObjet(), ge.getName(), value);
            throw report(equals, "Invalid assignment target.");
        }

        return expression;
    }

    private Expression or() {
        Expression expression = and();

        while (match(Kind.LogicalOr)) {
            Token operator = previous();
            Expression right = and();
            expression = new LogicalExpression(expression, operator, right);
        }
        return expression;
    }

    private Expression and() {
        Expression expression = equality();

        while (match(Kind.LogicalAnd)) {
            Token operator = previous();
            Expression right = equality();
            expression = new LogicalExpression(expression, operator, right);
        }
        return expression;
    }

    private Expression equality() {

        Expression expr = comparison();

        while (match(Kind.NotEqual, Kind.Equal)) {
            Token operator = previous();
            Expression right = comparison();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression comparison() {
        Expression expr = term();

        while (match(Kind.Greater, Kind.GreaterEqual, Kind.Less, Kind.LessEqual)) {
            Token operator = previous();
            Expression right = term();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression term() {
        Expression expr = factor();

        while (match(Kind.Minus, Kind.Plus)) {
            Token operator = previous();
            Expression right = factor();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression factor() {
        Expression expr = unary();

        while (match(Kind.Slash, Kind.Star)) {
            Token operator = previous();
            Expression right = unary();
            expr = new BinaryExpression(expr, operator, right);
        }

        return expr;
    }

    private Expression unary() {
        if (match(Kind.Not, Kind.Minus)) {
            Token operator = previous();
            Expression right = unary();
            return new UnaryExpression(operator, right);
        }
        return call();
    }

    private Expression call() {
        Expression expression = primary();
        boolean search = true;
        while (search) {
            if (match(Kind.OpenParenthesis)) expression = finishCall(expression);
            else if (match(Kind.Dot)) {
                Token name = consume(Kind.Identifier, "Expect property name after '.'.");
                expression = new GetterExpression(expression, name);
            }
            else search = false;
        }
        return expression;
    }

    private Expression finishCall(Expression callee) {
        List<Expression> arguments = new ArrayList<>();
        if (!check(Kind.CloseParenthesis)) {
            do {
                if (arguments.size() >= 255) {
                    throw report(peek(), "Can't have more than 255 arguments.");
                }
                arguments.add(expression());
            } while (match(Kind.CommaSymbol));
        }

        Token paren = consume(Kind.CloseParenthesis, "Expect ')' after arguments.");

        return new CallExpression(callee, paren, arguments);
    }

    private Expression primary() {
        if (match(Kind.FalseKw)) return new LiteralExpression(false);
        if (match(Kind.TrueKw)) return new LiteralExpression(true);
        if (match(Kind.NullKw)) return new LiteralExpression(null);
        if (match(Kind.Number, Kind.String)) return new LiteralExpression(previous().value());
        if (match(Kind.Identifier)) return new LetExpression(previous());
        if (match(Kind.FuncKw)) return new AnonymousFuncExpression(createAnonymousFunction());
        if (match(Kind.OpenBracket)) return new BlockExpression(createBlock());
        if (match(Kind.OpenParenthesis)) {
            Expression expr = expression();
            consume(Kind.CloseParenthesis, "Expect ')' after expression.");
            return new GroupingExpression(expr);
        }
        throw report(peek(), "Expect expression.");
    }

    public void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().kind() == Kind.Semicolon) return;
            switch (peek().kind()) {
                case ClassKw, FuncKw, LetKw, ForKw,
                        IfKw, MutKw, WhileKw, TypeKw, ReturnKw ->
                { return; }
            }
            advance();
        }
    }

    public Token consume(Kind type, String message) {
        if (check(type)) return advance();
        throw report(peek(), message);
    }

    public static ParseException report(Token tk, String msg) {
        System.err.println(diagnostic(tk, msg));
        return new ParseException();
    }

    public static String diagnostic(Token t, String msg) {
        if (t.kind() == Kind.EndToken)
            return "Error" + ": " + msg + " at end";
        return "Error" + ": '" + t.text() + "' | " + msg + " at "+ "[" + t.line() + ","+ t.col() + "]";
    }

    public boolean match(Kind... kinds) {
        for (Kind kind : kinds)
            if (check(kind)) {
                advance();
                return true;
            }
        return false;
    }

    public boolean check(Kind kind) {
        if (isAtEnd()) return false;
        return peek().kind() == kind;
    }

    public Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    public boolean isAtEnd() {
        return peek().kind() == Kind.EndToken;
    }

    public Token peek() {
        return tokens.get(current);
    }

    public Token previous() {
        return previous(1);
    }

    public Token previous(int offset) {
        return tokens.get(current - offset);
    }

}
