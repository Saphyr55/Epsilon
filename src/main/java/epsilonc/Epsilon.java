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
                    let msg = "Bonjour " + name;
                    print msg + "\n";
                }
                
                for (let i = 0; i < 10; i = i + 1) {
                    if (i != 0) {
                        print name + "\n";
                    }
                }
                              
            """;

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        Parser parser = Parser.createParser(code);
        parser.showTokens();
        interpreter.interpret(parser.parse());
    }

}
