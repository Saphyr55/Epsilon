package epsilon;

import epsilon.resolver.Interpreter;
import epsilon.resolver.ScopeResolver;
import epsilon.statement.Statement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Epsilon {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        Parser parser = Parser.createParser(Files.readString(Path.of("epsilon/main.epsl"), StandardCharsets.UTF_8));
        List<Statement> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ScopeResolver scopeResolver = new ScopeResolver(interpreter);
        scopeResolver.resolve(statements);

        System.out.println("Interpreting :");
        interpreter.interpret(statements);
    }

}
