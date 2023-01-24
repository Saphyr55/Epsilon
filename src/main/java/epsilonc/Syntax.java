package epsilonc;

import epsilonc.core.Bind;
import epsilonc.core.Matching;

public final class Syntax {

    public final static class Word {

        @Bind(kind = Kind.TypeKw)
        public static final String Type = "type";
        @Bind(kind = Kind.TrueKw)
        public static final String True = "true";
        @Bind(kind = Kind.FalseKw)
        public static final String False = "false";
        @Bind(kind = Kind.LetKw)
        public static final String Let = "let";
        @Bind(kind = Kind.MutKw)
        public static final String Mut = "mut";
        @Bind(kind = Kind.FuncKw)
        public static final String Func = "func";
        @Bind(kind = Kind.ReturnKw)
        public static final String Return = "return";
        @Bind(kind = Kind.ClassKw)
        public static final String Class = "class";
        @Bind(kind = Kind.ElseKw)
        public static final String Else = "else";
        @Bind(kind = Kind.IfKw)
        public static final String If = "if";
        @Bind(kind = Kind.NullKw)
        public static final String Null = "null";
        @Bind(kind = Kind.ForKw)
        public static final String For = "for";
        @Bind(kind = Kind.ThisKw)
        public static final String This = "this";
        @Bind(kind = Kind.WhileKw)
        public static final String While = "while";
        @Bind(kind = Kind.NamespaceKw)
        public static final String Namespace = "namespace";
    }

    public static final class Contextual {

        public static final String Method = "method";

    }

    public final static class Symbol {
        public static final String Space = " ";
        public static final String Ampersand = "&";
        public static final String Pipeline = "|";
        public static final String BackslashN = "\n";
        public static final String BackslashT = "\t";
        public static final String BackslashR = "\r";
        public static final String PlusOp = "+";
        public static final String MinusOp = "-";
        public static final String StartOp = "*";
        public static final String SlashOp = "/";
        public static final String SingleQuote = "'";
        public static final String DoubleQuote = "\"";
        public static final String BackQuote = "`";
        public static final String OpenParenthesis = "(";
        public static final String CloseParenthesis = ")";
        public static final String OpenBrackets = "{";
        public static final String CloseBrackets = "}";
        public static final String Colon = ":";
        @Bind(kind = Kind.Assign)
        @Matching(match = "=", kind = Kind.Equal)
        public static final String Equal = "=";
        public static final String Semicolon = ";";
        public static final String Comma = ",";
        public static final String Dot = ".";
        public static final String Bang = "!";
        public static final String Greater = ">";
        public static final String Less = "<";
    }


}
