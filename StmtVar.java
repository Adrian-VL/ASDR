public class StmtVar extends Statement {
    Token name;
    final Expression initializer;

    StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
    @Override
    public String toString() {
        return "var " + name.lexema + " = " + initializer + ";\n";
    }

    @Override
    void solve(TablaSimbolos tabla){
        if (initializer.solve(tabla) instanceof Integer || initializer.solve(tabla) instanceof Float || initializer.solve(tabla) instanceof Double || initializer.solve(tabla) instanceof String || initializer.solve(tabla) instanceof Boolean){
            if(tabla.existeIdentificador(name.lexema))
                throw new RuntimeException("Error: " + name.lexema + "se declaró previamente.");
            Object value = initializer.solve(tabla);
            tabla.asignar(name.lexema, value);
        }
        else
            throw new RuntimeException("Error semantico: Asignación incompatible");
    }
}
