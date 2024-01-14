// Define la clase ExprLiteral que extiende de Expression. Esta clase se utiliza para representar una expresión literal en un programa.
class ExprLiteral extends Expression {
    // Variable para almacenar el valor del literal.
    final Object value;

    // Constructor de ExprLiteral. Toma un objeto como valor y lo almacena.
    ExprLiteral(Object value) {
        this.value = value; // Asigna el valor pasado al constructor a la variable de la clase.
    }

    // Sobrescribe el método toString para convertir el valor literal en una cadena de texto.
    @Override
    public String toString() {
        // Devuelve la representación en cadena del valor.
        // Utiliza el método toString del objeto valor para obtener su representación en cadena.
        return value.toString();
    }

    // Sobrescribe el método solve para resolver la expresión literal.
    @Override
    Object solve(TablaSimbolos tabla) {
        // Devuelve el valor literal directamente.
        // Dado que es un literal, no requiere ninguna operación adicional o búsqueda en la tabla de símbolos.
        return this.value;
    }
}
