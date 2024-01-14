// Define la clase ExprGrouping que extiende de Expression. Esta clase se usa para representar una expresión entre paréntesis.
public class ExprGrouping extends Expression {
    // Variable para almacenar la expresión interna del agrupamiento.
    final Expression expression;

    // Constructor de ExprGrouping. Toma una expresión como argumento y la almacena.
    ExprGrouping(Expression expression) {
        this.expression = expression; // Asigna la expresión pasada al constructor a la variable de la clase.
    }

    // Sobrescribe el método toString para convertir la expresión de agrupamiento en una cadena de texto.
    @Override
    public String toString() {
        // Devuelve la representación en cadena de la expresión, encerrada entre paréntesis.
        return "(" + expression + ")";
    }

    // Sobrescribe el método solve para resolver la expresión de agrupamiento.
    @Override
    Object solve(TablaSimbolos tabla) {
        // Llama al método solve de la expresión contenida y devuelve su resultado.
        // La tabla de símbolos es pasada como argumento para permitir la resolución de la expresión.
        return expression.solve(tabla);
    }
}
