package epsilonc;

import epsilonc.core.ParseException;
import epsilonc.expression.*;
import epsilonc.object.Value;
import epsilonc.statement.*;
import epsilonc.syntax.Kind;
import epsilonc.syntax.Lexer;
import epsilonc.syntax.Syntax;
import epsilonc.syntax.Token;

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
            if (match(Kind.StructKw)) return createStructStatement();

            return statement();
        } catch (ParseException e) {
            synchronize();
            return null;
        }
    }

    /**
     * Create a class statement
     * rule:
     * ->   struct ::= "struct" Identifier "{" property* "}"
     *
     * @return struct statement
     */
    private Statement createStructStatement() {
        Token name = consume(Kind.Identifier, "Expect a name for declaring type.");
        consume(Kind.OpenBracket, "Expect to open bracket before declaring type body.");
        List<LetStatement> properties = new ArrayList<>();
        while (!check(Kind.CloseBracket) && !isAtEnd())  {
            properties.add(createPropertyDeclaration());
        }
        consume(Kind.CloseBracket, "Expect to close bracket after declaring type body.");
        return new StructStatement(name, properties);
    }

    /**
     * Create a class statement
     * utility:
     * ->   method ::= "method" Identifier "(" parameters ")" block
     * rule:
     * ->   class ::= "class" Identifier "{" (method* | let* | function*) "}"
     *
     * @return class statement
     */
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
            else throw report(peek(), "Don't authorized this syntax in class");

        }

        consume(Kind.CloseBracket, "Expected '}' after class body.");
        return new ClassStatement(name, fields, methods, functions);
    }

    private FunctionStatement createFunction() {
        return createFunction(false);
    }

    private FunctionStatement createAnonymousFunction() {
        return createFunction(true);
    }

    /**
     * Create a function statement
     * utility:
     * ->   parameters    ::= Identifier ( "," IDENTIFIER )*
     * ->   arguments     ::= expression ( "," expression )*
     * rule:
     * ->    function ::= "func" Identifier? "(" parameters? ")" block
     *
     * @param isAnonymous check if anonymous
     * @return function statement
     */
    private FunctionStatement createFunction(boolean isAnonymous) {

        Token name = null;
        Map<Token, Token> parameters = new HashMap<>();
        Token returnType = null;
        if (!isAnonymous) name = consume(Kind.Identifier, "Expected a function name.");
        consume(Kind.OpenParenthesis, "Expect '(' after function name.");
        if (!check(Kind.CloseParenthesis)) {
            do {
                if (parameters.size() >= 255)
                    throw report(peek(), "Can't have more than 255 parameters.");
                Token id = consume(Kind.Identifier, "Expect parameter name.");
                consume(Kind.Colon, "Expect ':' to precise the type.");
                Token type = consume(Kind.Identifier, "Expect a type for '" + id.text() + "'.");
                parameters.put(id, type);
            } while (match(Kind.CommaSymbol));
        }

        consume(Kind.CloseParenthesis, "Expect ')' after parameters.");

        if (match(Kind.Arrow))
            returnType = consume(Kind.Identifier, "Expected a return type after '->'.");

        consume(Kind.OpenBracket, "Expect '{' before function body.");

        List<Statement> body = createBlock();

        return new FunctionStatement(name, parameters, body, returnType);
    }

    /**
     * Create a let statement for a property declaration
     * rule:
     * ->   let ::= let "mut"? Identifier
     *              (":" Identifier)
     *              ("=" expression)? ";"
     *
     * @return let statement
     */
    private Statement createLetDeclaration() {
        boolean mutable = match(Kind.MutKw);
        Token name = consume(Kind.Identifier, "Expect a name before let assignment.");
        Expression initializer = null;
        consume(Kind.Colon, "Expect ':' for declaring type");
        Token type = consume(Kind.Identifier, "Expected declaring type after ':'.");
        if (match(Kind.Assign)) initializer = expression();
        if (initializer instanceof InitSructExpression be) {
            if (type == null) throw report(name, "Expected explicit type before initialize it.");
            be.setType(type);
        }
        consume(Kind.Semicolon, "Expect ';' after let declaration.");
        return new LetStatement(name, type, initializer, mutable);
    }

    /**
     * Create a let statement for a property declaration
     * rule:
     * ->   property ::= "mut"? Identifier
     *              (":" Identifier)?
     *              ("=" expression)? ";"
     *
     * @return let statement
     */
    private LetStatement createPropertyDeclaration() {
        boolean mutable = match(Kind.MutKw);
        Token name = consume(Kind.Identifier, "Expected a name for a property");
        consume(Kind.Colon, "Expected declaring type after naming it");
        consume(Kind.Identifier, "Expected declaring type after naming it");
        Token type = previous();
        consume(Kind.Semicolon, "Expect ';' after property declaration.");
        return new LetStatement(name, type, new LiteralExpression(null), mutable);
    }

    /**
     * statement ::= expressionStatement
     *                | forStmt
     *                | whileStmt
     *                | ifStmt
     *                | returnStmt
     *                | block
     * @return statement
     */
    private Statement statement() {
        if (match(Kind.ForKw)) return createForStatement();
        if (match(Kind.WhileKw)) return createWhileStatement();
        if (match(Kind.IfKw)) return createIfStatement();
        if (match(Kind.ReturnKw)) return createReturnStatement();
        if (match(Kind.OpenBracket)) return new BlockStatement(createBlock());
        return createExpressionStatement();
    }

    /**
     * Create a return statement
     * rule:
     * -> return ::= "return" expression? ";"
     *
     * @return return statement
     */
    private Statement createReturnStatement() {
        Token kw = previous();
        Expression value = check(Kind.Semicolon) ? null : expression();
        consume(Kind.Semicolon, "Expected ';' after return value.");
        return new ReturnStatement(kw, value);
    }

    /**
     * Create a for statement
     * rule:
     * -> for ::= "for" "(" expression?
     *            ";" expression?
     *            ";" expression? ")" statement
     *
     * @return for statement
     */
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
            condition = new LiteralExpression(Value.ofTrue());

        body = new WhileStatement(condition, body);

        if (init != null)
            body = new BlockStatement(Arrays.asList(init, body));

        return body;
    }

    /**
     * Create while statement
     * rule:
     * ->   while ::= "while" "(" expression ")" statement
     *
     * @return while statement
     */
    private Statement createWhileStatement() {
        consume(Kind.OpenParenthesis, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(Kind.CloseParenthesis, "Expect ')' after condition.");
        return new WhileStatement(condition, statement());
    }

    /**
     * Create a if statement
     * rule:
     * ->   if ::= "if" "(" expression ")" statement ("else" statement)?
     *
     * @return if statement
     */
    private Statement createIfStatement() {

        consume(Kind.OpenParenthesis, "Expected '(' after if.");
        Expression condition = expression();
        consume(Kind.CloseParenthesis, "Expected ')' after if condition.");

        Statement thenBranch = statement();
        Statement elseBranch = match(Kind.ElseKw) ? statement() : null;

        return new IfStatement(condition, thenBranch, elseBranch);
    }

    /**
     * Create a list of statement representing a block
     * rule:
     * -> block ::= "{" declaration "}"
     *
     * @return list statement (block)
     */
    private List<Statement> createBlock() {
        List<Statement> statements = new ArrayList<>();
        while (!check(Kind.CloseBracket) && !isAtEnd()) {
            statements.add(createDeclaration());
        }
        consume(Kind.CloseBracket,  "Expect '}' after block.");
        return statements;
    }

    private List<Statement> createStructInitializer() {
        advance();
        List<Statement> statements = new ArrayList<>();
        while (!check(Kind.CloseBracket) && !isAtEnd()) {
            consume(Kind.Identifier, "Expected a name");
            Token name = previous();
            consume(Kind.Assign, "Expected '=' to assign a value");
            Expression value = expression();
            consume(Kind.Semicolon, "Expect ';' after a declaration.");
            statements.add(new InitStatement(name, value));
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
            if (match(Kind.OpenParenthesis)) {
                expression = finishCall(expression);
            } else if (match(Kind.Dot)) {
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
        if (match(Kind.FalseKw)) return new LiteralExpression(Value.ofFalse());
        if (match(Kind.TrueKw)) return new LiteralExpression(Value.ofTrue());
        if (match(Kind.NullKw)) return new LiteralExpression(Value.ofNull());
        if (match(Kind.Number)) return new LiteralExpression(Value.ofNumber(previous().value()));
        if (match(Kind.String)) return new LiteralExpression(Value.ofString(previous().value()));
        if (match(Kind.Identifier)) {
            if (check(Kind.OpenBracket)) return new InitSructExpression(previous(), createStructInitializer());
            return new LetExpression(previous());
        }
        if (match(Kind.FuncKw)) return new AnonymousFuncExpression(createAnonymousFunction());
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
                        IfKw, MutKw, WhileKw, StructKw, ReturnKw ->
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
        if (t.kind() == Kind.EndToken) return "Error" + ": " + msg + " at end";
        return "Error" + ": " + " To the "+ "line " + ( t.line() + 1 ) + " for '" + t.text() + "' | " + msg;
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
