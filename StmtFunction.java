import java.util.ArrayList;
import java.util.List;

public class StmtFunction extends Statement {
    final Token name;
    final List<Token> params;
    final StmtBlock body;

    StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }
    @Override
    public String toString()  {
        String parametros = "";
        for (Token token : params) {
            parametros += token.lexema;
            parametros += ", ";
        }
        parametros = parametros.substring(0, parametros.lenght() - 2)
        return "fun " +name.lexema + "(" + parametros + ")" + body;
    }
    @Override
    void solve(TablaSimbolos tabla) {
        List<Object> funcion = new ArrayList<Object>();
        TablaSimbolos tablaFuncion = new TablaSimbolos(tabla);
        for (Token elemento : params) {
            tablaFuncion.asignar(elemento.lexema, null);
        }
        tablaFuncion.asignar("return", null);
        funcion.add(tablaFuncion);
        funcion.add(body);
        funcion.add(params);
        tabla.asignar(name.lexema, funcion);
        
    }
}
