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

    static String calcul = """
                "parse" + 1
            """;

    public static void main(String[] args) {
        Parser parser = Parser.createParser(calcul);
        System.out.println(parser.parse());
    }


}
