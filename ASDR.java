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

    private void EXPR_STMT(){
        if (hayErrores)
            return;
        
        EXPRESSION();
        matchErrores(TipoToken.SEMICOLON);
    }

    private void FOR_STMT(){
        if(hayErrores){
            return;
        }
        matchErrores(TipoToken.FOR);
        matchErrores(TipoToken.LEFT_PAREN);
        FOR_STMT_1();
        FOR_STMT_2();
        FOR_STMT_3();
        matchErrores(TipoToken.RIGHT_PAREN);
        STATEMENT();
    }
    
    private void FOR_STMT_1(){
        if (hayErrores) {
            return;
        }

        if(preanalisis.tipo == TipoToken.VAR)
            VAR_DECL();
        else if (preanalisis.tipo == TipoToken.SEMICOLON)
            match(TipoToken.SEMICOLON);
        else
            EXPR_STMT();
    }
    
    private void FOR_STMT_2(){
        if (hayErrores) {
            return;
        }

        if(preanalisis.tipo == TipoToken.SEMICOLON)
            match(TipoToken.SEMICOLON);
        else{
            EXPRESSION();
            matchErrores(TipoToken.SEMICOLON);
        }
    }

    private void FOR_STMT_3(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            EXPRESSION();
        }
    }
    private void IF_STMT(){
        if (hayErrores) {
            return;
        }

        matchErrores(TipoToken.IF);
        matchErrores(TipoToken.LEFT_PAREN);
        EXPRESSION();
        matchErrores(TipoToken.RIGHT_PAREN);
        STATEMENT();
        ELSE_STATEMENT();
    }

    private void ELSE_STATEMENT(){
        if (hayErrores) {
            return;
        }

        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            STATEMENT();
        }
    }

    private void PRINT_STMT(){
        if (hayErrores) {
            return;
        }
        matchErrores(TipoToken.PRINT);
        EXPRESSION();
        matchErrores(TipoToken.SEMICOLON);
    }

    private void RETURN_STMT(){
        if (hayErrores) {
            return;
        }

        matchErrores(TipoToken.RETURN);
        RETURN_EXP_OPC();
        matchErrores(TipoToken.SEMICOLON);
    }

    private void RETURN_EXP_OPC(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        }
    }

    private void WHILE_STMT(){
        if (hayErrores) {
            return;
        }

        matchErrores(TipoToken.WHILE);
        matchErrores(TipoToken.LEFT_PAREN);
        EXPRESSION();
        matchErrores(TipoToken.RIGHT_PAREN);
        STATEMENT();
    }

    private void BLOCK(){
        if (hayErrores) {
            return;
        }

        matchErrores(TipoToken.LEFT_BRACE);
        DECLARATION();
        matchErrores(TipoToken.RIGHT_BRACE);
    }
    

    /******** Expresiones  *********/
    
    private void EXPRESSION(){
        if (hayErrores) {
            return;
        }
        ASSIGNMENT();
    }

    private void ASSIGNMENT(){
        if (hayErrores) {
            return;
        }
        LOGIC_OR();
        ASSIGNMENT_OPC();
    }

    private void ASSIGNMENT_OPC(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
    }

    private void LOGIC_OR(){
        if (hayErrores) {
            return;
        }
        LOGIC_AND();
        LOGIC_OR_2();
    }

    private void LOGIC_OR_2(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.OR){
            match(TipoToken.OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

  private void EQUALITY_2(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
            EQUALITY_2();
        }
    }

    private void COMPARISON(){
        if (hayErrores) {
            return;
        }
        TERM();
        COMPARISON_2();
    }

    private void COMPARISON_2(){
        if (hayErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.GREATER) {
            match(TipoToken.GREATER);
            TERM();
            COMPARISON_2();
        } 
        else if (preanalisis.tipo == TipoToken.GREATER_EQUAL) {
            match(TipoToken.GREATER_EQUAL);
            TERM();
            COMPARISON_2();
        }
        else if (preanalisis.tipo == TipoToken.LESS) {
            match(TipoToken.LESS);
            TERM();
            COMPARISON_2();
        }
        else if (preanalisis.tipo == TipoToken.LESS_EQUAL) {
            match(TipoToken.LESS_EQUAL);
            TERM();
            COMPARISON_2();
        }
    }

    private void TERM(){
        if (hayErrores) {
            return;
        }
        FACTOR();
        TERM_2();
    }

    private void TERM_2(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            FACTOR();
            TERM_2();
        }
        else if(preanalisis.tipo == TipoToken.PLUS){
            match(TipoToken.PLUS);
            FACTOR();
            TERM_2();
        }
    }

    private void FACTOR(){
        if (hayErrores) {
            return;
        }
        UNARY();
        FACTOR_2();
    }

    private void FACTOR_2(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.SLASH){
            match(TipoToken.SLASH);
            UNARY();
            FACTOR_2();
        }
        else if(preanalisis.tipo == TipoToken.STAR){
            match(TipoToken.STAR);
            UNARY();
            FACTOR_2();
        }
    }

    private void UNARY(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.BANG){
            match(TipoToken.BANG);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            UNARY();
        }
        else
            CALL();
    }

    private void CALL(){
        if (hayErrores) {
            return;
        }
        PRIMARY();
        CALL_2();
    }

    private void CALL_2(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            ARGUMENTS_OPC();
            matchErrores(TipoToken.RIGHT_PAREN);
            CALL_2();
        }
    }

    private void PRIMARY(){
        if (hayErrores) {
            return;
        }
        if(preanalisis.tipo == TipoToken.TRUE)
            match(TipoToken.TRUE);
        else if(preanalisis.tipo == TipoToken.FALSE)
            match(TipoToken.FALSE);
        else if(preanalisis.tipo == TipoToken.NULL)
            match(TipoToken.NULL);
        else if(preanalisis.tipo == TipoToken.NUMBER)
            match(TipoToken.NUMBER);
        else if(preanalisis.tipo == TipoToken.STRING)
            match(TipoToken.STRING);
        else if(preanalisis.tipo == TipoToken.IDENTIFIER)
            match(TipoToken.IDENTIFIER);
        else if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            matchErrores(TipoToken.RIGHT_PAREN);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba TRUE, FALSE, NULL, NUMBER, STRING, IDENTIFIER, LEFT_PAREN");
        }
    }
private void FUNCTION(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;

    // Si el token actual es un identificador, se procesa la función.
    if(preanalisis.tipo == TipoToken.IDENTIFIER){
        matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el identificador.
        matchErrores(TipoToken.LEFT_PAREN); // Se verifica y consume el paréntesis izquierdo.
        PARAMETERS_OPC(); // Se llama al método para procesar los parámetros opcionales.
        matchErrores(TipoToken.RIGHT_PAREN); // Se verifica y consume el paréntesis derecho.
        BLOCK(); // Se llama al método para procesar el bloque de código de la función.
    }
}

// Método para procesar parámetros opcionales.
private void PARAMETERS_OPC(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;
    if(preanalisis.tipo == TipoToken.IDENTIFIER) // Si hay un identificador, se procesan los parámetros.
        PARAMETERS();
}

// Método para procesar parámetros.
private void PARAMETERS(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;

    matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el identificador del parámetro.
    PARAMETERS_2(); // Se llama al método para procesar más parámetros si existen.
}

// Método para continuar procesando parámetros adicionales.
private void PARAMETERS_2(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;

    // Si hay una coma, indica que hay más parámetros.
    if(preanalisis.tipo == TipoToken.COMMA){
        match(TipoToken.COMMA); // Se verifica y consume la coma.
        matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el siguiente identificador.
        PARAMETERS_2(); // Se llama recursivamente para procesar más parámetros si existen.
    }
}

// Método para procesar argumentos opcionales.
private void ARGUMENTS_OPC(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;

    // Si el token actual es un tipo de dato o un identificador, se procesa la expresión.
    if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
        EXPRESSION(); // Se llama al método para procesar la expresión.
        ARGUMENTS(); // Se llama al método para procesar más argumentos si existen.
    }
}

// Método para continuar procesando argumentos adicionales.
private void ARGUMENTS(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;
    
    // Si hay una coma, indica que hay más argumentos.
    if(preanalisis.tipo == TipoToken.COMMA){
        match(TipoToken.COMMA); // Se verifica y consume la coma.
        EXPRESSION(); // Se llama al método para procesar la siguiente expresión.
        ARGUMENTS(); // Se llama recursivamente para procesar más argumentos si existen.
    }
}

// Método para verificar y consumir un token específico.
private void match(TipoToken tt){
    if(preanalisis.tipo == tt){ // Si el tipo de token actual coincide con el esperado.
        i++; // Se avanza al siguiente token.
        preanalisis = tokens.get(i); // Se actualiza el token de preanálisis.
    }
    else{
        hayErrores = true; // Si no coincide, se marca un error.
    }
}

// Método para verificar y consumir un token, manejando errores.
private void matchErrores(TipoToken tt){
    if(!hayErrores){ // Si no hay errores previos, se intenta hacer match.
        match(tt);
        if (hayErrores) { // Si se encuentra un error durante el match.
            System.out.println("Error de sintaxis: Se esperaba " + tt); // Se imprime un mensaje de error.
        }
    }
}

}
