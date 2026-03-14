program - declaration* EOF ;

declaration - varDecl | statement ;

statement - exprStmt 
        | ifStmt
        | printStmt 
        | block ;

block - "{" declaration "}" ;

ifStmt - "if" "(" expression ")" statement
            ( "else" statement )? ;

varDecl - "var" IDENTIFIER ( "=" expression )? ";" ;

exprStmt - expression ";" ;

printStmt - "print" expression ";" ;



expression - assignment ;

assignment - IDENTIFIER "=" assignment 
        | equality ;

logic_or - logic_and ( "or" logic_and )* ;

logic_and - equality ( "and" equality )* ;

equality - comparison ("!=" | "==") comparison 
        | comparison;

comparison - term (">" | ">=" | "<" | "<=") term 
        | term;

term - factor ("-" | "+") factor 
        | factor;

factor - unary ("/" | "*") unary 
        | unary;

unary - ("!" | "-") unary 
        | primary ;

primary - false | true | nil
          | NUMBER | STRING
          | "(" expression ")"
          | IDENTIFIER ;

