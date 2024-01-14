public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
//Agregado
    /*  esta clase esta diseñada para representar y evaluar expresiones lógicas o de comparación, con manejo de tipos y verificación semántica*/
    @Override
    public String toString(){
        return left + " " + operator.lexema + " " + right;
    }

    @Override
    Object solve(TablaSimbolos tabla){
        try{
            switch (operatos.tipo){
            case OR:
                return ((boolean) left.solve(tabla)) || ((boolean) right.solve(tabla));
            case AND:
                return ((boolean) left.solve(tabla)) && ((boolean) right.solve(tabla));
            case BANG_EQUAL:
                return (left.solve(tabla) != right.solve(tabla));
            case EQUAL_EQUAL:
                return (left.solve(tabla) == right.solve(tabla));
            case GREATER:
                if(left.solve(tabla) instanceof Integer)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Integer) left.solve(tabla)) > ((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return Float.valueOf((Integer) left.solve(tabla)) > ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Integer) left.solve(tabla)) > ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Float)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Float) left.solve(tabla)) > Float.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Float) left.solve(tabla)) > ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Float) left.solve(tabla)) > ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Double)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Double) left.solve(tabla)) > Double.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Double) left.solve(tabla)) > Double.valueOf((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return ((Double) left.solve(tabla)) > ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                else
                    throw new RuntimeException("Error semantico: Los operandos incompatibles.");
            case GREATER_EQUAL:
                if(left.solve(tabla) instanceof Integer)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Integer) left.solve(tabla)) >= ((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return Float.valueOf((Integer) left.solve(tabla)) >= ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Integer) left.solve(tabla)) >= ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Float)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Float) left.solve(tabla)) >= Float.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Float) left.solve(tabla)) >= ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Float) left.solve(tabla)) >= ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Double)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Double) left.solve(tabla)) >= Double.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Double) left.solve(tabla)) >= Double.valueOf((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return ((Double) left.solve(tabla)) >= ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                else
                    throw new RuntimeException("Error semantico: Los operandos incompatibles.");
            case LESS:
                if(left.solve(tabla) instanceof Integer)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Integer) left.solve(tabla)) < ((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return Float.valueOf((Integer) left.solve(tabla)) < ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Integer) left.solve(tabla)) < ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Float)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Float) left.solve(tabla)) < Float.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Float) left.solve(tabla)) < ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Float) left.solve(tabla)) < ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Double)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Double) left.solve(tabla)) < Double.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Double) left.solve(tabla)) < Double.valueOf((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return ((Double) left.solve(tabla)) < ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                else
                    throw new RuntimeException("Error semantico: Los operandos incompatibles.");
            case LESS_EQUAL:
                if(left.solve(tabla) instanceof Integer)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Integer) left.solve(tabla)) <= ((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return Float.valueOf((Integer) left.solve(tabla)) <= ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Integer) left.solve(tabla)) <= ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Float)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Float) left.solve(tabla)) <= Float.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Float) left.solve(tabla)) <= ((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return Double.valueOf((Float) left.solve(tabla)) <= ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                if(left.solve(tabla) instanceof Double)
                    if(right.solve(tabla) instanceof Integer)
                        return ((Double) left.solve(tabla)) <= Double.valueOf((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return ((Double) left.solve(tabla)) <= Double.valueOf((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return ((Double) left.solve(tabla)) <= ((Double) right.solve(tabla));
                    else
                        throw new RuntimeException("Error semantico: Operandos incompatibles");
                else
                    throw new RuntimeException("Error semantico: Los operandos incompatibles.");
            default:
                break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

