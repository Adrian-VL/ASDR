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
private List<Statement>DECLARATION(List<Statenment>declarations){
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
            preanalisis.tipo == TipoToekn.FOR || preanaislis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.PRINT ||
            preanalisis.tipo == TipoTonken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRECE){
        declarations.add(STATEMENT()); // Gestiona la sentencia
        DECLARATION(declarations); // Llama recursivamente para seguir analizando
    }
    return declarations;
}
// FUN_DECL -> fun FUNCTION
// Función específica para la declaración de funciones
private Stmfuntion FUN_DECL(){
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
    Token name = previus();
    Expression initializer = VAR_INIT();
    matchErrores(TipoToken.SEMICOLON); // Espera un punto y coma al final de la declaración
    return new StmtVar(name, initializer);
}
//VAR_INIT -> = EXPRESSION
//         -> Ɛ  
// Función para la inicialización de variables
private Expression VAR_INIT(){
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
    private Statement STATEMENT(){
        if(hayErrores){
            return null;
        switch(preanalisis.tipo){
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
            default:
                return EXPR_STMT();
        }
    }

    // EXPR_STMT -> EXPRESSION ;
    private Statement EXPR_STMT(){
        Statement stmt = new StmtExpression(EXPRESSION());
        matchErrores(TipoToken.SEMICOLON);
        return stmt;
    }

    // FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT(){
        if(hayErrores){
            return null;
        }
        matchErrores(TipoToken.FOR);
        matchErrores(TipoToken.LEFT_PAREN);
        Statement initializer =FOR_STMT_1();
        Expression condition =FOR_STMT_2();
        Expression increment =FOR_STMT_3();
        matchErrores(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();
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
        listafor.add(initializer);
        listaFor.add(whileFor);
        StmtBlock forBlock = new StmtBlock(listaFor);

        // Expression whileCondition = new ExpLiteral(true);
        // Expression forCondition = condition;
        // whileCondition = new ExprLogical (whileCondition), new Token(TipoToken.AND, "and"), forCondition);
        // return new StmtLoop(whileCondition, new StmtBlock(Arrays.asList(initializer, new StmtLoop(whileCondition, new StmtBlock(Arrays.asList(body,new StmtExpression(increment)))))))
        return forBlock;
    }


    //FOR_STMT_1 -> VAR_DECL
    //           -> EXPRE_STMT
    //           -> ;
    private Statement FOR_STMT_1(){
        if (hayErrores) {
            return;
        }

        if(preanalisis.tipo == TipoToken.VAR)
            return VAR_DECL();
        else if (preanalisis.tipo == TipoToken.SEMICOLON)
            match(TipoToken.SEMICOLON);
            return null;
        else
            return EXPR_STMT();
    }
    // FOR_STMT_2 -> EXPRESSION;
    //            -> ;
    private Expression FOR_STMT_2(){
        if (hayErrores) {
            return null;
        }

        if(preanalisis.tipo == TipoToken.SEMICOLON)
            match(TipoToken.SEMICOLON);
            return null;
        else{
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
    
    private Expression EXPRESSION(){
        if (hayErrores) {
            return null;
        }
       return ASSIGNMENT();
    }

    private Expression ASSIGNMENT(){
        if(hayErrores)
            return null;
        Expression expr = LOGIC_OR();
        return ASSIGNMENT_OPC(expr);
    }

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

    private Expression LOGIC_OR(){
        if(hayErrores)
            return null;
        Expression expr = LOGIC_AND();
        return LOGIC_OR_2(expr);
    }
    
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
    
    private Expression LOGIC_AND(){
        if(hayErrores)
            return null;
        Expression expr = EQUALITY();
        return LOGIC_AND_2(expr);
    }

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
    
     private Expression EQUALITY(){
        if(hayErrores)
            return null;
        Expression expr = COMPARISON();
        return EQUALITY_2(expr);
    }
    
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

     private Expression COMPARISON(){
        if(hayErrores)
            return null;
        Expression expr = TERM();
        return COMPARISON_2(expr);
    }
    
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

   private Expression TERM(){
        if(hayErrores)
            return null;
        Expression expr = FACTOR();
        expr = TERM_2(expr);
        return expr;
    }
    
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

    private Expression FACTOR(){
        if(hayErrores)
            return null;
        Expression expr = UNARY();
        expr = FACTOR_2(expr);
        return expr;
    }

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

   private Expression CALL(){
        if(hayErrores)
            return null;
        Expression expr = PRIMARY();
        expr = CALL_2(expr);
        return expr;
    }

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
    
private Stmtfunction FUNCTION(){
    if(hayErrores) // Si hay errores previos, se retorna inmediatamente.
        return null;

        matchErrores(TipoToken.IDENTIFIER); // Se verifica y consume el identificador.
        Token id = previous();
        matchErrores(TipoToken.LEFT_PAREN); // Se verifica y consume el paréntesis izquierdo.
        List<Token> lstParametList = PARAMETERS_OPC();// Se llama al método para procesar los parámetros opcionales.
        matchErrores(TipoToken.RIGHT_PAREN); // Se verifica y consume el paréntesis derecho.
        StmtBlock blk = BLOCK(); // Se llama al método para procesar el bloque de código de la función.
        return new StmtFunction(id, lsParametlist, blk);
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
private void ARGUMENTS_OPC(){
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
        ARGUMENTS(expression); // Se llama recursivamente para procesar más argumentos si existen.
    }
}

// Método para verificar y consumir un token específico.
private void match(TipoToken tt) throws RutimeException {
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
