package org.epsilon;

public class Epsilon {

    static String text = """
            func main() {
            
                class Point {
                
                    x: int;
                    y: int;
                    
                    method sum() -> int {
                        return x + y;
                    }
                    
                }
                
                print "Hello World!";
            
            }
            """ ;

    static String code = """
                print "Hello World!";
                print( 1 + 1 + 9 - 4 / 5);
            """;

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        Parser parser = Parser.createParser(code);
        interpreter.interpret(parser.parse());
    }


}
