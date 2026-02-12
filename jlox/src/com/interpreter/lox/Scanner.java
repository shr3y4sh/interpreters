package com.interpreter.lox;

import java.util.ArrayList;
import java.util.List;

import static com.interpreter.lox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }
}
