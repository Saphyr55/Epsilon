package epsilonc;

import epsilonc.core.Bind;
import epsilonc.core.Matching;

public final class Syntax {

    public final static class Word {
        @Bind(kind = Kind.True)
        public static final String True = "true";
        @Bind(kind = Kind.False)
        public static final String False = "false";
        @Bind(kind = Kind.LetSymbol)
        public static final String Let = "let";
        @Bind(kind = Kind.MutSymbol)
        public static final String Mut = "mut";
        @Bind(kind = Kind.FuncSymbol)
        public static final String Func = "func";
        @Bind(kind = Kind.ReturnSymbol)
        public static final String Return = "return";
        @Bind(kind = Kind.ClassSymbol)
        public static final String Class = "class";
        @Bind(kind = Kind.ElseSymbol)
        public static final String Else = "else";
        @Bind(kind = Kind.IfSymbol)
        public static final String If = "if";
        @Bind(kind = Kind.NullSymbol)
        public static final String Null = "null";
        @Bind(kind = Kind.ForSymbol)
        public static final String For = "for";
        @Bind(kind = Kind.ThisSymbol)
        public static final String This = "this";
        @Bind(kind = Kind.WhileSymbol)
        public static final String While = "while";
        @Bind(kind = Kind.BreakSymbol)
        public static final String Break = "break";
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
