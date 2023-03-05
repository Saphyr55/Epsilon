package epsilon;

import epsilon.statement.Statement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ModuleManager {

    private static final Map<String, Module> modulesFile = new HashMap<>();

    public static void add(String file) {
        String id = file.split("\\.")[0].replaceAll("/", ".");
        modulesFile.put(id, new Module(id, file));
    }

    public static void load(String from) {
        try (Stream<Path> walk = Files.walk(Paths.get(from))) {
            var result = walk
                    .filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith("epsl"))
                    .toList();
            result.forEach(ModuleManager::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Statement> parse(String id) throws IOException {
        Module module = modulesFile.get(id);
        if (!module.isLoaded()) {
            module.setLoaded(true);
            Path p = Path.of(module.getPath());
            Parser parser = Parser.createParser(Files.readString(p, StandardCharsets.UTF_8));
            return parser.parse();
        }
        return List.of();
    }

}
