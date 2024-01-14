/*Esta clase abstracta Statement sirve como base para otras clases que representarán diferentes tipos de declaraciones en un lenguaje de programación o sistema de interpretación.
  Las clases que herede de esta deben proporcionar una implementación concreta para el método solve, 
  definiendo cómo se deben ejecutar las instrucciones específicas en el contexto de una tabla de símbolos.*/
public abstract class Statement {
  abstract void solve(TablaSimbolos tabla);
}
