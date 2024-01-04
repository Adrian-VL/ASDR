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
    
    /*Sentencias*/
    private void STATEMENT(){
        
    }
}
