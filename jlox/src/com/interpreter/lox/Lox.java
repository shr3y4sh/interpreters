package com.interpreter.lox;

import static com.interpreter.lox.TokenType.EOF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    static boolean parseError = false;
    static boolean hadRuntimeError = false;

    private static Interpreter interpreter = new Interpreter();

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runfile(args[0]);

        } else {
            runprompt();
        }
    }

    static void error(Token token, String message) {
        if (token.type == EOF) {
            report(token.line, " at the end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError e) {
        System.err.println(e.getMessage() + "\n[line: " + e.token.line + "]");
        hadRuntimeError = true;
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void runprompt() throws IOException {
        var input = new InputStreamReader(System.in);
        var reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            parseError = false;
        }
    }

    private static void runfile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (parseError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void run(String source) {
        List<Token> tokens = new Scanner(source).scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();

        if (parseError) return;

        interpreter.interpret(stmts);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[ line " + line + "] Error " + where + ":" + message);
        parseError = true;
    }
}
