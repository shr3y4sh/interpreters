package com.interpreter.lox;

import java.util.List;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private Environment env = new Environment();

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(env));
        return null;
    }

    private void executeBlock(List<Stmt> statements, Environment environment) {
        Environment prev = this.env;

        try {
            this.env = environment;

            for (var st : statements) {
                execute(st);
            }
        } finally {
            this.env = prev;
        }
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        env.assign(expr.name, expr.value);
        return value;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return env.get(expr.name);
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;

        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        env.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        var left = evaluate(expr.left);
        var right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                throw new RuntimeError("Operands must be two numbers or a string", expr.operator);

            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;

            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;

            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double) left / (double) right;

            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;

            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;

            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;

            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;

            case BANG_EQUAL:
                return !isEqual(left, right);

            case EQUAL_EQUAL:
                return isEqual(left, right);

            default:
                break;
        }

        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        var right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            default:
                break;
        }

        return null;
    }

    void interpret(List<Stmt> statements) {
        try {
            for (var stmt : statements) {
                execute(stmt);
            }
        } catch (RuntimeError e) {
            Lox.runtimeError(e);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    private String stringify(Object value) {
        if (value == null) return "nil";

        if (value instanceof Double) {
            String text = value.toString();

            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return value.toString();
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError("Operands must be numbers", operator);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;

        throw new RuntimeError("Operand must be a number", operator);
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;

        if (a == null) return false;

        return a.equals(b);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;

        if (object instanceof Boolean) return (boolean) object;

        return true;
    }
}
