package epsilon;

import epsilon.statement.Statement;
import epsilon.syntax.Token;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    private static final Map<String, String> modulesFile = new HashMap<>();

    public static void add(String id, String file) {
        modulesFile.put(id, file);
    }

    public static List<Statement> load(String id) throws IOException {
        Path p = Path.of(modulesFile.get(id));
        System.out.println(p);
        Parser parser = Parser.createParser(Files.readString(p, StandardCharsets.UTF_8));
        return parser.parse();
    }

}
