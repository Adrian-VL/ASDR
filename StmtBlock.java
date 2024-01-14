import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }
    //Agregado
    //diseñado para manejar bloques de declaraciones
    @Override
    public String toString() {
        String stmt="";
        for(Statement statement : statements){
            stmt += statement.toString();
        }
        return "{\n" + stmt + "}\n";
    }

    @Override
    void solve(TablaSimbolos tabla){
        TablaSimbolos tablaSiguiente = new TablaSimbolos(tabla);
        for (Statement statement : statements){
                statement.solve(tablaSiguiente);
        } 
    }
}
