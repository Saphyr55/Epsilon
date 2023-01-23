package epsilonc;

public enum Kind {

    // Tokens
    UnknownToken,
    EndToken,
    LiteralToken,
    WhitespaceToken,
    Number,

    // Symbols
    ReturnSymbol,
    FuncSymbol,
    ArrowSymbol,
    LetSymbol,
    Assign,
    CloseBracket,
    OpenBracket,
    Semicolon,
    Colon,
    OpenParenthesis,
    CloseParenthesis,
    DoubleQuoteSymbol,
    ReturnLineSymbol,
    QuoteSymbol,
    Minus,
    Plus,
    Slash,
    Star,
    CommaSymbol,
    Period,
    Equal,
    LessEqual,
    GreaterEqual,
    NotEqual,
    Not,

    // Expressions
    String,
    ParenthesizedExpression,
    EmptyExpression,
    BinaryExpression,
    UnknownExpression,
    NullSymbol,
    IfSymbol,
    ElseSymbol,
    ThisSymbol,
    ForSymbol,
    ClassSymbol,
    WhileSymbol,
    Less,
    Greater, Identifier, MethodSymbol, Unmatch, True, False, PrintSymbol, MutSymbol, BinaryAnd, LogicalAnd, BinaryOr, LogicalOr, BreakSymbol,

}
