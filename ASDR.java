import java.util.List;
import java.util.ArrayList;

public class ASDR implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<Statement> dclrtns= new ArrayList<>();
  
     public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

   @Override
    public List<Statement> parse() {
        PROGRAM();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println(x:"La sintaxis es correcta");
            return  dclrtns;
        }else {
            System.out.println(x:"Se encontraron errores");
        }
        return null;
    }

// PROGRAM -> DECLARATION
// Función principal del programa
 public List<Statement> PROGRAM() {
        if(hayErrores)
            return null;
        else
            return DECLARATION(dclrtns);
    }
// DECLARATION -> FUN_DECL DECLARATION
//             -> VAR_DECL DECLARATION
//             -> STATEMENT DECLARATION
//             -> Ɛ
// Gestiona las declaraciones generales
private List<Statement>DECLARATION(List<Statement>declarations){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return null;

    // Comprueba el tipo de declaración y llama a la función correspondiente
    if(preanalisis.tipo == TipoToken.FUN){
        declarations.add(FUN_DECL()); // Gestiona la declaración de una función
        DECLARATION(declarations); // Llama recursivamente para seguir analizando
    }
    else if (preanalisis.tipo == TipoToken.VAR){
        declarations.add(VAR_DECL()); // Gestiona la declaración de una variable
        DECLARATION(declarations); // Llama recursivamente para seguir analizando
    }
    // Comprueba si el tipo de token corresponde a una sentencia
    else if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS ||  preanalisis.tipo == TipoToken.TRUE ||
            preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER ||
            preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN ||
            preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT ||
            preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
        declarations.add(STATEMENT()); // Gestiona la sentencia
        DECLARATION(declarations); // Llama recursivamente para seguir analizando
    }
    return declarations;
}
// FUN_DECL -> fun FUNCTION
// Función específica para la declaración de funciones
private StmtFunction FUN_DECL(){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return null;
    
    matchErrores(TipoToken.FUN); // Verifica que el token actual sea 'FUN'
    return FUNCTION(); // Llama a la función que maneja la estructura de una función
}
//VAR_DECL -> var id VAR_INIT
// Función específica para la declaración de variables
private StmtVar VAR_DECL(){
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return null;
    
    matchErrores(TipoToken.VAR); // Verifica que el token actual sea 'VAR'
    matchErrores(TipoToken.IDENTIFIER); // Verifica que haya un identificador
    Token name = previous();
    Expression initializer = VAR_INIT();
    matchErrores(TipoToken.SEMICOLON); // Espera un punto y coma al final de la declaración
    return new StmtVar(name, initializer);
}
//VAR_INIT -> = EXPRESSION
//         -> Ɛ  
// Función para la inicialización de variables
private Expression VAR_INIT(){ // Esta línea declara una función llamada VAR_INIT.
    if(hayErrores) // Si hay errores, termina la ejecución de esta función
        return null;

    if(preanalisis.tipo == TipoToken.EQUAL){
        matchErrores(TipoToken.EQUAL); // Verifica que el token actual sea un signo igual
        return EXPRESSION(); // Analiza la expresión de inicialización
    } else {
        return null;
    }
}



    
    /*Sentencias*/
    //STATEMET -> EXPR_STMT Sentencia que contiene una expresion
    //         -> FOR_STMT Identifica los for
    //         -> IF_STMT Identifica los if
    //         -> PRINT_STMT
    //         -> RETURN_STMT
    //         -> WHILE_STMT
    //         -> BLOCK Identifica todo lo que estre entre llaves {} como un bloque
    /*Analiza y gestiona las diferentes sentencias*/
    private Statement STATEMENT(){

        if(hayErrores)
            return null;
        switch(preanalisis.tipo){
            /*El token es FOR, IF, WHILE, PRINT, LEFT_BRACE, RETURN*/
            case FOR:
                return FOR_STMT();
            case IF:
                return IF_STMT();
            case WHILE:
                return WHILE_STMT();
            case PRINT:
                return PRINT_STMT();
            case LEFT_BRACE:
                return BLOCK();
            case RETURN:
                return RETURN_STMT();
            /*En cualquier otro caso*/
            default:
                return EXPR_STMT();
        }
    }

    // EXPR_STMT -> EXPRESSION ;
    private Statement EXPR_STMT(){
        Statement stmt = new StmtExpression(EXPRESSION());
        /* Asegura que la expresion termina en punto y coma */
        matchErrores(TipoToken.SEMICOLON);
        /* retorna la expresion */
        return stmt;
    }

    // FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT(){
        /*Verifica si hay errores*/
        if(hayErrores){
            return null;
        }
        /*Verifica la validacion del tipo de token*/
        matchErrores(TipoToken.FOR);
        matchErrores(TipoToken.LEFT_PAREN); 
        /* Se inicializan los metodos para analizar la sintaxis del bucle FOR*/
        Statement initializer =FOR_STMT_1(); 
        Expression condition =FOR_STMT_2();
        Expression increment =FOR_STMT_3();
        matchErrores(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();

        /*Creacion de la estrcutura*/
        /*
        {
          inicializacion

          while(condicion){
            body
            incremento
          }
        }
        */
        List<Statement> listaCuerpo = new ArrayList<Statement>();
        listaCuerpo.add(body);
        listaCuerpo.add(new StmtExpression(increment));
        StmtBlock cuerpo = new StmtBlock(listaCuerpo);
        StmtLoop whileFor = new StmtLoop(condition, cuerpo);

        List<Statement> listaFor = new ArrayList<Statement>();
        listaFor.add(initializer);
        listaFor.add(whileFor);
        StmtBlock forBlock = new StmtBlock(listaFor);

        return forBlock;
    }


    //FOR_STMT_1 -> VAR_DECL
    //           -> EXPRE_STMT
    //           -> ;
    private Statement FOR_STMT_1(){
        if (hayErrores) {
            return null;
        }
        // Si el tipo token es del tipo VAR
        if(preanalisis.tipo == TipoToken.VAR){
            return VAR_DECL(); // Hay una delcaracion de variable
        // Si el tipo token es ;
        } else if (preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return null;
        } else
            return EXPR_STMT(); // Hay una expresion
    }
    // FOR_STMT_2 -> EXPRESSION;
    //            -> ;
    private Expression FOR_STMT_2(){
        if (hayErrores) {
            return null;
        }

        if(preanalisis.tipo == TipoToken.SEMICOLON){
            matchErrores(TipoToken.SEMICOLON);
            return null;
        }else{
            Expression expr = EXPRESSION();
            matchErrores(TipoToken.SEMICOLON);
            return expr;
        }
    }

        
    // FOR_STMT_3 -> EXPRESSION
    //            -> Ɛ
    private Expression FOR_STMT_3(){
        if (hayErrores) {
            return null;
        }
        if (preanalisis.tipo == TipoToken.RIGHT_PAREN) {
            return null;
        }
        else
            return EXPRESSION();
    }


    // IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMENT
    private StmtIf IF_STMT(){
        if (hayErrores) {
            return null;
        }

        matchErrores(TipoToken.IF);
        matchErrores(TipoToken.LEFT_PAREN);
        Expression condition = EXPRESSION();
        matchErrores(TipoToken.RIGHT_PAREN);
        Statement ifBranch = STATEMENT();
        Statement elseBranch = ELSE_STATEMENT();
        return new StmtIf(condition, ifBranch, elseBranch);
    }

    // ELSE_STATEMENT -> else STATEMENT
    //                -> Ɛ
    private Statement ELSE_STATEMENT(){
        if (hayErrores) {
            return null;
        }

        if(preanalisis.tipo == TipoToken.ELSE){
            matchErrores(TipoToken.ELSE);
            return STATEMENT();
        }
        else
            return null;
    }
        
    // PRINT_STMT -> print EXPRESSION ;
    private StmtPrint PRINT_STMT(){
        if (hayErrores) {
            return null;
        }
        matchErrores(TipoToken.PRINT);
        Expression expr = EXPRESSION();
        matchErrores(TipoToken.SEMICOLON);
        return new StmtPrint(expr);
    }

    // RETURN_STMT -> return RETURN_EXP_OPC ;
    private StmtReturn RETURN_STMT(){
        if (hayErrores) {
            return null;
        }

        matchErrores(TipoToken.RETURN);
        Expression expr = RETURN_EXP_OPC();
        matchErrores(TipoToken.SEMICOLON);
        return new StmtReturn(expr);
    }

    // RETURN_EXP_OPC -> EXPRESSION
    //                -> Ɛ
    private Expression RETURN_EXP_OPC(){
        if (hayErrores) {
            return null;
        }
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            return EXPRESSION();
        }
        else
            return null;
    }


    // WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private StmtLoop WHILE_STMT(){
        if (hayErrores) {
            return null;
        }

        matchErrores(TipoToken.WHILE);
        matchErrores(TipoToken.LEFT_PAREN);
        Expression condition = EXPRESSION();
        matchErrores(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();
        return new StmtLoop(condition, body);
    }

    // BLOCK -> { DECLARATION }
    private StmtBlock BLOCK(){
        if (hayErrores) {
            return null;
        }

        matchErrores(TipoToken.LEFT_BRACE);
        List<Statement> declarations = new ArrayList<>();
        DECLARATION(declarations);
        matchErrores(TipoToken.RIGHT_BRACE);
        return new StmtBlock(declarations);
    }
    

    /******** Expresiones  *********/
        
    //EXPRESSION -> ASSIGNMENT
    private Expression EXPRESSION(){
        if (hayErrores) {
            return null;
        }
       return ASSIGNMENT();
    }
        
    //ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private Expression ASSIGNMENT(){
        if(hayErrores)
            return null;
        Expression expr = LOGIC_OR();
        return ASSIGNMENT_OPC(expr);
    }
        
    //ASSIGNMENT_OPC -> = EXPRESSION
    //               -> Ɛ
     private Expression ASSIGNMENT_OPC(Expression expr){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo) {
            case EQUAL:
                matchErrores(TipoToken.EQUAL);
                return new ExprAssign(((ExprVariable) expr).name, EXPRESSION());
            default:
                return expr;
        }
    }
    
    //LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private Expression LOGIC_OR(){
        if(hayErrores)
            return null;
        Expression expr = LOGIC_AND();
        return LOGIC_OR_2(expr);
    }
    
    //LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2
    //           -> Ɛ
    private Expression LOGIC_OR_2(Expression expr){
        if(hayErrores)
            return null;
            
        switch (preanalisis.tipo) {
            case OR:
                Token operador = preanalisis;
                matchErrores(preanalisis.tipo);
                Expression right = LOGIC_AND();
                expr = new ExprLogical(expr, operador, right);
                return LOGIC_OR_2(expr);
            default:
                return expr; 
        }
    }
    
    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND(){
        if(hayErrores)
            return null;
        Expression expr = EQUALITY();
        return LOGIC_AND_2(expr);
    }
    
    //LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2
    //            -> Ɛ
    private Expression LOGIC_AND_2(Expression expr){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo) {
            case AND:
                Token operador = preanalisis;
                matchErrores(preanalisis.tipo);
                Expression right = EQUALITY();
                expr = new ExprLogical(expr, operador, right);
                return LOGIC_AND_2(expr);
            default:
                return expr; 
        }
    }
    
    //EQUALITY -> COMPARISON EQUALITY_2
     private Expression EQUALITY(){
        if(hayErrores)
            return null;
        Expression expr = COMPARISON();
        return EQUALITY_2(expr);
    }
    
    //EQUALITY_2 -> != COMPARISON EQUALITY_2
    //           -> == COMPARISON EQUALITY_2
    //           -> Ɛ
    private Expression EQUALITY_2(Expression expr){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo) {
            case BANG_EQUAL:
            case EQUAL_EQUAL:
                Token operador = preanalisis;
                matchErrores(preanalisis.tipo);
                Expression right = COMPARISON();
                expr = new ExprLogical(expr, operador, right);
                return EQUALITY_2(expr);
            default:
                return expr; 
        }
    }
    
    //COMPARISON -> TERM COMPARISON_2
     private Expression COMPARISON(){
        if(hayErrores)
            return null;
        Expression expr = TERM();
        return COMPARISON_2(expr);
    }
    
    //COMPARISON_2 -> > TERM COMPARISON_2
    //             -> >= TERM COMPARISON_2
    //             -> < TERM COMPARISON_2
    //             -> <= TERM COMPARISON_2
    //             -> Ɛ
    private Expression COMPARISON_2(Expression expr){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo) {
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
                Token operador = preanalisis;
                matchErrores(preanalisis.tipo);
                Expression right = TERM();
                expr = new ExprLogical(expr, operador, right);
                return COMPARISON_2(expr);
            default:
                return expr; 
        }
    }
    
    //TERM -> FACTOR TERM_2
   private Expression TERM(){
        if(hayErrores)
            return null;
        Expression expr = FACTOR();
        expr = TERM_2(expr);
        return expr;
    }
    
    //TERM_2 -> - FACTOR TERM_2
    //       -> + FACTOR TERM_2
    //       -> Ɛ
    private Expression TERM_2(Expression expr){
        if(hayErrores)
            return null;

        switch (preanalisis.tipo){
            case MINUS:
                matchErrores(TipoToken.MINUS);
                Token operador = previous();
                Expression expr2 = FACTOR();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return TERM_2(expb);
            case PLUS:
                matchErrores(TipoToken.PLUS);
                operador = previous();
                expr2 = FACTOR();
                expb = new ExprBinary(expr, operador, expr2);
                return TERM_2(expb);
            default:
                break;
        }
        return expr;
    }

    //FACTOR -> UNARY FACTOR_2
    private Expression FACTOR(){
        if(hayErrores)
            return null;
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }

    //FACTOR_2 -> / UNARY FACTOR_2
    //         -> * UNARY FACTOR_2
    //         -> Ɛ
    private Expression FACTOR_2(Expression expr){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo){
            case SLASH:
                matchErrores(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = UNARY();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expb);
            case STAR:
                matchErrores(TipoToken.STAR);
                operador = previous();
                expr2 = UNARY();
                expb = new ExprBinary(expr, operador, expr2);
                return FACTOR_2(expb);
            default:
                break;
        }
        return expr;
    }

    //UNARY -> ! UNARY
    //      -> - UNARY
    //      -> CALL
     private Expression UNARY(){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo){
            case BANG:
                matchErrores(TipoToken.BANG);
                Token operador = previous();
                Expression expr = UNARY();
                return new ExprUnary(operador, expr);
            case MINUS:
                matchErrores(TipoToken.MINUS);
                operador = previous();
                expr = UNARY();
                return new ExprUnary(operador, expr);
            default:
                return CALL();
        }
    }

    //CALL -> PRIMARY CALL_2
   private Expression CALL(){
        if(hayErrores)
            return null;
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }

    //CALL_2 -> ( ARGUMENTS_OPC ) CALL_2
    //       -> Ɛ
    private Expression CALL_2(Expression expr){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo){
            case LEFT_PAREN:
                matchErrores(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = ARGUMENTS_OPC();
                matchErrores(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return CALL_2(ecf);
            default:
                break;
        }
        return expr;
    }
    
    //PRIMARY -> true
    //        -> false
    //        -> null
    //        -> number
    //        -> string
    //        -> id
    //        -> ( EXPRESSION )
    private Expression PRIMARY(){
        if(hayErrores)
            return null;
        switch (preanalisis.tipo){
            case TRUE:
                matchErrores(TipoToken.TRUE);
                return new ExprLiteral(true);
            case FALSE:
                matchErrores(TipoToken.FALSE);
                return new ExprLiteral(false);
            case NULL:
                matchErrores(TipoToken.NULL);
                return new ExprLiteral(null);
            case NUMBER:
                matchErrores(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(numero.literal);
            case STRING:
                matchErrores(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.literal);
            case IDENTIFIER:
                matchErrores(TipoToken.IDENTIFIER);
                Token id = previous();
                return new ExprVariable(id);
            case LEFT_PAREN:
                matchErrores(TipoToken.LEFT_PAREN);
                Expression expr = EXPRESSION();
                matchErrores(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
            default:
                hayErrores = true;
                System.out.println("Se esperaba TRUE, FALSE, NULL, NUMBER, STRING, IDENTIFIER, LEFT_PAREN");
                break;
        }
        return null;
    }

/*********Otros ***********/
    
private StmtFunction FUNCTION(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return null;

        matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el identificador.
        Token id = previous();
        matchErrores(TipoToken.LEFT_PAREN); // Se verifica y consume el paréntesis izquierdo.
        List<Token> lstParametList = PARAMETERS_OPC();// Se llama al método para procesar los parámetros opcionales.
        matchErrores(TipoToken.RIGHT_PAREN); // Se verifica y consume el paréntesis derecho.
        StmtBlock blk = BLOCK(); // Se llama al método para procesar el bloque de código de la función.
        return new StmtFunction(id, lstParametList, blk);
    }
}

// PARAMETERS_OPC -> PARAMETERS
//                -> Ɛ
// Método para procesar parámetros opcionales.
private List<Token> PARAMETERS_OPC(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return null;
    if(preanalisis.tipo == TipoToken.IDENTIFIER) { // Si hay un identificador, se procesan los parámetros.
       return PARAMETERS();
    }
    return null;
}

// PARAMETERS -> id PARAMETERS_2
private List<Token> PARAMETERS(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return null;

    List<Token> parameters = new ArrayList<Token>();
    matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el identificador del parámetro.
    parameters.add(previous());
    PARAMETERS_2(parameters);
    return parameters;// Se llama al método para procesar más parámetros si existen.
}
//PARAMETERS_2 -> , id PARAMETERS_2
//             -> // Ɛ
// Método para continuar procesando parámetros adicionales.
private void PARAMETERS_2(List<Token> parameters){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;

    // Si hay una coma, indica que hay más parámetros.
    if(preanalisis.tipo == TipoToken.COMMA){
        matchErrores(TipoToken.COMMA); // Se verifica y consume la coma.
        matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el siguiente identificador.
        parameters.add(previous());
        PARAMETERS_2(parameters); // Se llama recursivamente para procesar más parámetros si existen.
    }
}
//ARGUMENTS_OPC -> EXPRESSION ARGUMENTS
//              -> Ɛ
// Método para procesar argumentos opcionales.
private List<Expression> ARGUMENTS_OPC(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return null;
    List<Expression> expressions = new ArrayList<Expression>();
    expressions.add(EXPRESSION());
    // Si el token actual es un tipo de dato o un identificador, se procesa la expresión.
    if(preanalisis.tipo == TipoToken.COMMA) {
        ARGUMENTS(expressions); // Se llama al método para procesar más argumentos si existen.
        return expressions;
    } else {
        return expressions;
    }
}
//ARGUMENTS -> , EXPRESSION ARGUMENTS
//          -> Ɛ
// Método para continuar procesando argumentos adicionales.
private void ARGUMENTS(List<Expression> expressions) {
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return;
    
    // Si hay una coma, indica que hay más argumentos.
    if(preanalisis.tipo == TipoToken.COMMA){
        match(TipoToken.COMMA); // Se verifica y consume la coma.
        expressions.add(EXPRESSION()); // Se llama al método para procesar la siguiente expresión.
        ARGUMENTS(expressions); // Se llama recursivamente para procesar más argumentos si existen.
    }
}

// Método para verificar y consumir un token específico.
private void match(TipoToken tt) throws RuntimeException {
    if(preanalisis.tipo == tt){ // Si el tipo de token actual coincide con el esperado.
        i++; // Se avanza al siguiente token.
        preanalisis = tokens.get(i); // Se actualiza el token de preanálisis.
    }
    else{
        String Message = "Se esperaba " + tt +
            "pero se encontro " + preanalisis.tipo + "Atn: " + this.tokens.get(i -1);
       throw new RuntimeException (message);
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
  private Token previous() {
      return this.tokens.get(i - 1);
  }

}
