package epsilonc;

import epsilonc.resolver.Interpreter;
import epsilonc.resolver.ScopeResolver;
import epsilonc.resolver.TypeResolver;
import epsilonc.statement.Statement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Epsilon {

    public static void main(String[] args) throws IOException {
        Interpreter interpreter = new Interpreter();
        Parser parser = Parser.createParser(Files.readString(Path.of("epsilon/main.epsl"), StandardCharsets.UTF_8));
        List<Statement> statements = parser.parse();
        ScopeResolver scopeResolver = new ScopeResolver(interpreter);
        scopeResolver.resolve(statements);
        TypeResolver typeResolver = new TypeResolver(scopeResolver, interpreter);
        typeResolver.resolve(statements);
        interpreter.interpret(statements);
    }

}
