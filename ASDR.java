import java.util.List;

public class ASDR implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
  
     public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

   @Override
    public boolean parse() {
        PROGRAM();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("La sintaxis es correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }
// Función principal del programa
private void PROGRAM(){
    DECLARATION(); // Inicia el análisis con la declaración
}

// Gestiona las declaraciones generales
private void DECLARATION(){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return;

    // Comprueba el tipo de declaración y llama a la función correspondiente
    if(preanalisis.tipo == TipoToken.FUN){
        FUN_DECL(); // Gestiona la declaración de una función
        DECLARATION(); // Llama recursivamente para seguir analizando
    }
    else if (preanalisis.tipo == TipoToken.VAR){
        VAR_DECL(); // Gestiona la declaración de una variable
        DECLARATION(); // Llama recursivamente para seguir analizando
    }
    // Comprueba si el tipo de token corresponde a una sentencia
    else if (esTipoSentencia(preanalisis.tipo)){
        STATEMENT(); // Gestiona la sentencia
        DECLARATION(); // Llama recursivamente para seguir analizando
    }
}

// Función específica para la declaración de funciones
private void FUN_DECL(){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return;
    
    matchErrores(TipoToken.FUN); // Verifica que el token actual sea 'FUN'
    FUNCTION(); // Llama a la función que maneja la estructura de una función
}

// Función específica para la declaración de variables
private void VAR_DECL(){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return;
    
    matchErrores(TipoToken.VAR); // Verifica que el token actual sea 'VAR'
    matchErrores(TipoToken.IDENTIFIER); // Verifica que haya un identificador
    VAR_INIT(); // Inicializa la variable, si es necesario
    matchErrores(TipoToken.SEMICOLON); // Espera un punto y coma al final de la declaración
}

// Función para la inicialización de variables
private void VAR_INIT(){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return;

    if(preanalisis.tipo == TipoToken.EQUAL){
        match(TipoToken.EQUAL); // Verifica que el token actual sea un signo igual
        EXPRESSION(); // Analiza la expresión de inicialización
    }
}



    
    /*Sentencias*/
    private void STATEMENT(){
        if(hayErrores){
            return;
        }
        if(preanalisis.tipo == TipoToken.FOR)
            FOR_STMT();
        else if(preanalisis.tipo == TipoToken.IF)
            IF_STMT();
        else if(preanalisis.tipo == TipoToken.WHILE)
            WHILE_STMT();
        else if(preanalisis.tipo == TipoToken.PRINT)
            PRINT_STMT();
        else if(preanalisis.tipo == TipoToken.LEFT_BRACE)
            BLOCK();
        else if(preanalisis.tipo == TipoToken.RETURN)
            RETURN_STMT();
        else
            EXPR_STMT();
    }
}
