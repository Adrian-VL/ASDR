import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {

    private final Map<String, Object> values = new HashMap<>();
    final TablaSimbolos anterior;

    TablaSimbolos(TablaSimbolos anterior){
        this.anterior = anterior;
    }

    boolean existeIdentificador(String identificador){
        if(!values.containsKey(identificador)){
            return buscarEnAnteriores(identificador, this.anterior);
        }
        else
            return true;
    }

    Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        return obtenerDeAnteriores(identificador, this.anterior);
    }


    void asignar(String identificador, Object valor){
        values.put(identificador, valor);
    }

    private boolean buscarEnAnteriores(String identificador, TablaSimbolos tablaAnterior){
        if(tablaAnterior == null) 
            return false;
        if(!tablaAnterior.values.containsKey(identificador))
            return buscarEnAnteriores(identificador, tablaAnterior.anterior);
        else
            return true;
    }

    private Object obtenerDeAnteriores(String identificador, TablaSimbolos tablaAnterior){
        if(tablaAnterior == null)
            throw new RuntimeException("Variable no definida '" + identificador + "'.");
        if(!tablaAnterior.values.containsKey(identificador))
            return obtenerDeAnteriores(identificador, tablaAnterior.anterior);
        else
            return tablaAnterior.values.get(identificador);
    }

    public TablaSimbolos obtenerTabla(String identificador, TablaSimbolos tablaAnterior){
        if(tablaAnterior == null)
            throw new RuntimeException("Variable no definida '" + identificador + "'.");
        if(!tablaAnterior.values.containsKey(identificador))
            return obtenerTabla(identificador, tablaAnterior.anterior);
        else
            return tablaAnterior;
    }
}

