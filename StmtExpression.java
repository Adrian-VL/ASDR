public class StmtExpression extends Statement {
    final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
    }
    //Agregado
    //Esta clase representa una declaración de expresión, donde la expresión se evalúa en el contexto de la tabla de símbolos.
    @Override
    public String toString(){
        return expression.toString();
    }
    @Override
    void solve(TablaSimbolos tabla){
        expression.solve(tabla);
    }
}
