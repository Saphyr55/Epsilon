package epsilonc;

import epsilonc.core.Key;
import epsilonc.core.Matching;

public final class Syntax {

    public final static class Word {
        @Key(kind = Kind.True)
        public static final String True = "true";
        @Key(kind = Kind.False)
        public static final String False = "false";
        @Key(kind = Kind.LetSymbol)
        public static final String Let = "let";
        @Key(kind = Kind.MutSymbol)
        public static final String Mut = "mut";
        @Key(kind = Kind.PrintSymbol)
        public static final String Print = "print";
        @Key(kind = Kind.FuncSymbol)
        public static final String Func = "func";
        @Key(kind = Kind.ReturnSymbol)
        public static final String Return = "return";
        @Key(kind = Kind.ClassSymbol)
        public static final String Class = "class";
        @Key(kind = Kind.ElseSymbol)
        public static final String Else = "else";
        @Key(kind = Kind.IfSymbol)
        public static final String If = "if";
        @Key(kind = Kind.NullSymbol)
        public static final String Null = "null";
        @Key(kind = Kind.ForSymbol)
        public static final String For = "for";
        @Key(kind = Kind.ThisSymbol)
        public static final String This = "this";
        @Key(kind = Kind.WhileSymbol)
        public static final String While = "while";
        @Key(kind = Kind.BreakSymbol)
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
        @Key(kind = Kind.Assign)
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
