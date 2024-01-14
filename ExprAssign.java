public class ExprAssign extends Expression{
    Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name.lexema + " = " + value + ";\n";
    }

    @Override
    Object solve(TablaSimbolos tabla) {
        if(tabla.existeIdentificador(name.lexema)){
            TablaSimbolos tablaSimbolos = tabla.obtenerTabla(name.lexema, tabla.anterior);
            if(tablaSimbolos == null)
                throw new RuntimeException("La variable " + name.lexema + " no ha sido declarada.");
            else
                tablaSimbolos.asignar(name.lexema, value.solve(tabla));
        }
        else
            throw new RuntimeException("La variable " + name.lexema + " no ha sido declarada.");
            //tabla.asignar(name.lexema, value.solve(tabla));
        return null;
    }
}
