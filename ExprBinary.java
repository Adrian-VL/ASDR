public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " " +  operator.lexema + " " + right;
    }

    @Override
    Object solve(TablaSimbolos tabla) {
        try {
            switch (operator.tipo) {
                case MINUS:
                    if(left.solve(tabla) instanceof Integer){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Integer) left.solve(tabla)) - ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Integer) left.solve(tabla)) - ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Integer) left.solve(tabla)) - ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Float){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Float) left.solve(tabla)) - ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Float) left.solve(tabla)) - ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Float) left.solve(tabla)) - ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Double){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Double) left.solve(tabla)) - ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Double) left.solve(tabla)) - ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Double) left.solve(tabla)) - ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else 
                        throw new RuntimeException("Error semantico: Operando no compatible");
                case PLUS:
                    if(left.solve(tabla) instanceof Integer){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Integer) left.solve(tabla)) + ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Integer) left.solve(tabla)) + ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Integer) left.solve(tabla)) + ((Double) right.solve(tabla));
                        else if(right.solve(tabla) instanceof String)
                            return left.solve(tabla).toString() + ((String) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Float){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Float) left.solve(tabla)) + ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Float) left.solve(tabla)) + ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Float) left.solve(tabla)) + ((Double) right.solve(tabla));
                        else if(right.solve(tabla) instanceof String)
                            return left.solve(tabla).toString() + ((String) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Double){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Double) left.solve(tabla)) + ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Double) left.solve(tabla)) + ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Double) left.solve(tabla)) + ((Double) right.solve(tabla));
                        else if(right.solve(tabla) instanceof String)
                            return left.solve(tabla).toString() + ((String) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof String){
                        if(right.solve(tabla) instanceof String)
                            return ((String) left.solve(tabla)) + ((String) right.solve(tabla));
                        else
                            return ((String) left.solve(tabla)) + (right.solve(tabla)).toString();
                    }
                    else 
                        throw new RuntimeException("Error semantico: Operando no compatible");
                case STAR:
                    if(left.solve(tabla) instanceof Integer){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Integer) left.solve(tabla)) * ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Integer) left.solve(tabla)) * ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Integer) left.solve(tabla)) * ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Float){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Float) left.solve(tabla)) * ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Float) left.solve(tabla)) * ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Float) left.solve(tabla)) * ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Double){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Double) left.solve(tabla)) * ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Double) left.solve(tabla)) * ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Double) left.solve(tabla)) * ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else 
                        throw new RuntimeException("Error semantico: Operando no compatible");
                case SLASH:
                    if(left.solve(tabla) instanceof Integer){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Integer) left.solve(tabla)) / ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Integer) left.solve(tabla)) / ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Integer) left.solve(tabla)) / ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Float){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Float) left.solve(tabla)) / ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Float) left.solve(tabla)) / ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Float) left.solve(tabla)) / ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else if(left.solve(tabla) instanceof Double){
                        if(right.solve(tabla) instanceof Integer)
                            return ((Double) left.solve(tabla)) / ((Integer) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Float)
                            return ((Double) left.solve(tabla)) / ((Float) right.solve(tabla));
                        else if(right.solve(tabla) instanceof Double)
                            return ((Double) left.solve(tabla)) / ((Double) right.solve(tabla));
                        else
                            throw new RuntimeException("Operandos incompatibles");
                    }
                    else 
                        throw new RuntimeException("Error semantico: Operando no compatible");    
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
