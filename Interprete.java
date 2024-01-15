import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interprete {

    static boolean existenErrores = false;

    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Uso correcto: interprete [archivo.txt]");

            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            ejecutarArchivo(args[0]);
        } else{
            ejecutarPrompt();
        }
    }

    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));

        // Se indica que existe un error
        if(existenErrores) System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }

    private static void ejecutar(String source) {
        try{
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scan();
            // for(Token token : tokens){
            //     System.out.println(token);
            // }
            //****Agregado****
            Parser parser = new ASDR(tokens);//: Crea una instancia del parser ASDR utilizando una lista de tokens llamada token
            List<Statement> dclrtns = parser.parse();//Utiliza el parser para analizar la lista de tokens y generar una lista de declaraciones (Statement). La lista resultante se almacena en la variable dclrtns.
            TablaSimbolos tabla = new TablaSimbolos(null);//Crea una instancia de la clase TablaSimbolos llamada tabla. La inicializa con un contexto padre null. Esto parece indicar que esta será la tabla de símbolos principal.
            for (Statement statement : dclrtns){// Itera sobre cada declaración en la lista dclrtns y resuelve cada declaración en el contexto de la tabla de símbolos tabla utilizando el método solve.
                statement.solve(tablaSimbolos);
            }
            //****Agregado****
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /*
    El método error se puede usar desde las distintas clases
    para reportar los errores:
    Interprete.error(....);
     */
    static void error(int linea, String posicion, String mensaje){
        reportar(linea, posicion, mensaje);
    }

    public static void reportar(int linea, String posicion, String mensaje){
        System.err.println(
                "[linea " + linea + "] Error " + posicion + ": " + mensaje
        );
        existenErrores = true;
    }

}
