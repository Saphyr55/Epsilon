package epsilonc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Epsilon {

    public static void main(String[] args) throws IOException {
        Interpreter interpreter = new Interpreter();
        Parser parser = Parser.createParser(Files.readString(Path.of("main.epsilon"), StandardCharsets.UTF_8));
        interpreter.interpret(parser.parse());
    }

}
