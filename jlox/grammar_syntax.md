program - declaration* EOF ;

declaration - varDecl | statement ;

statement - exprStmt 
        | printStmt 
        | block ;

block - "{" declaration "}" ;


varDecl - "var" IDENTIFIER ( "=" expression )? ";" ;

exprStmt - expression ";" ;

printStmt - "print" expression ";" ;



expression - assignment ;

assignment - IDENTIFIER "=" assignment 
        | equality ;

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

