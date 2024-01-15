public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
    /* Agregado */
    /*Este fragmento de código define una clase llamada ExprUnary que extiende la clase Expression. 
    Esta clase representa expresiones unarias, es decir, expresiones que involucran un único operando y
    un operador unario, como el operador de negación (!) o el operador de negativo numérico (-).*/
    @Override
    public String toString(){
        return operator.lexema + right;
    }

    @Override
    Object solve(TablaSimbolos tabla){
        try{
            switch (operator.tipo) {
                case BANG:
                    if(right.solve(tabla) instaneof Boolean)
                        return !((boolean) right.solve(tabla));
                    else
                        throw new RuntimeException( "Error semantico: Operando no compatible");
                case MINUS:
                    if(right.solve(tabla) instanceof Integer)
                        return -((Integer) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Float)
                        return -((Float) right.solve(tabla));
                    else if(right.solve(tabla) instanceof Double)
                        return -((Double) right.solve(tabla));
                    else 
                        throw new RuntimeException("Error semantico: Operando no compatible");
                default:
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
