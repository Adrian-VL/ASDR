public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "return " + value +";\n";
    }

    @Override
    void solve(TablaSimbolos tabla){
        TablaSimbolos tablaReturn;
        if(tabla.existeIdentificador("return")){
            tablaReturn = tabla.obtenerTabla("return", tabla);
            tablaReturn.asignar("return", value.solve(tablaReturn));
        }
        else
            throw new RuntimeException("Error semantico: uso incorrecto de return");
    }
}
