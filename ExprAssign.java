// Define la clase ExprAssign que extiende de Expression, usada para representar una asignación de expresión.
public class ExprAssign extends Expression{
    // Declara una variable de tipo Token para almacenar el nombre de la variable en la asignación.
    Token name;
    // Declara una variable final de tipo Expression para almacenar el valor a asignar.
    final Expression value;

    // Constructor de la clase que inicializa los campos name y value.
    ExprAssign(Token name, Expression value) {
        this.name = name; // Asigna el nombre.
        this.value = value; // Asigna el valor de la expresión.
    }

    // Sobrescribe el método toString para representar la asignación como una cadena de texto.
    @Override
    public String toString() {
        // Devuelve la cadena que representa la asignación en formato "nombre = valor;\n".
        return name.lexema + " = " + value + ";\n";
    }

    // Sobrescribe el método solve para ejecutar la lógica de la asignación.
    @Override
    Object solve(TablaSimbolos tabla) {
        // Verifica si el identificador (nombre de la variable) existe en la tabla de símbolos.
        if(tabla.existeIdentificador(name.lexema)){
            // Obtiene la tabla de símbolos correspondiente al alcance de la variable.
            TablaSimbolos tablaSimbolos = tabla.obtenerTabla(name.lexema, tabla.anterior);
            // Si la tabla de símbolos es nula, lanza una excepción indicando que la variable no ha sido declarada.
            if(tablaSimbolos == null)
                throw new RuntimeException("La variable " + name.lexema + " no ha sido declarada.");
            else
                // Si la tabla de símbolos existe, asigna el valor resuelto de la expresión a la variable.
                tablaSimbolos.asignar(name.lexema, value.solve(tabla));
        }
        else
            // Lanza una excepción si el identificador no existe en la tabla de símbolos.
            throw new RuntimeException("La variable " + name.lexema + " no ha sido declarada.");
            //tabla.asignar(name.lexema, value.solve(tabla)); // Código comentado.
        // Retorna null, indicando que no hay un valor concreto a retornar por esta operación.
        return null;
    }
}
