package com.interpreter.lox;

import com.interpreter.lox.Expr.Binary;
import com.interpreter.lox.Expr.Grouping;
import com.interpreter.lox.Expr.Literal;
import com.interpreter.lox.Expr.Unary;

class Interpreter implements Expr.Visitor<Object> {

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
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

                break;

            case MINUS:
                return (double) left - (double) right;

            case STAR:
                return (double) left * (double) right;

            case SLASH:
                return (double) left / (double) right;

            case GREATER:
                return (double) left > (double) right;

            case GREATER_EQUAL:
                return (double) left >= (double) right;

            case LESS:
                return (double) left < (double) right;

            case LESS_EQUAL:
                return (double) left <= (double) right;

            default:
                break;
        }

        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        var right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            default:
                break;
        }

        return null;
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;

        if (object instanceof Boolean) return (boolean) object;

        return true;
    }
}
