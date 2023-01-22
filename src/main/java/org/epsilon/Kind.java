package org.epsilon;

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
    Affect,
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
    Nil,
    IfSymbol,
    ElseSymbol,
    ThisSymbol,
    ForSymbol,
    ClassSymbol,
    WhileSymbol,
    Less,
    Greater, Identifier, MethodSymbol, Unmatch, True, False,

}
