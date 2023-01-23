package epsilonc;

public class Epsilon {

    static String text = """
            class Point {
                    
            }
            
            func main() {
                
                print "Hello World!";
            
            }
            """ ;

    static String code = """
                let name = "Andy";
                {
                    let bonjour = "Bonjour";
                    print bonjour + " " + name + "\n";
                }
                
                print( 1 + 1 + 9 - 4 / 5);
            """;

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        Parser parser = Parser.createParser(code);
        parser.showTokens();
        interpreter.interpret(parser.parse());
    }

}
