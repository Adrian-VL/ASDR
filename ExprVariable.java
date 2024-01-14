class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }
//Agregado
/* sta clase representa una expresión que simplemente consiste en una variable y proporciona métodos para obtener su representación como cadena (toString())
    y para resolver su valor en el contexto de una tabla de símbolos (solve).
    Además, realiza una verificación semántica para asegurarse de que la variable esté declarada antes de intentar obtener su valor. */
    @Override
    public String toString(){
        return name.lexema;
    }

    @Override
    object solve(TablaSimbolos tabla){
        if(!tabla.existeIdentificador(name.lexema))
            throw new RuntimeException("La variable" + name.lexema + "n ha sido declarada");
        else
            return tabla.obtener(name.lexema);
    }
}
