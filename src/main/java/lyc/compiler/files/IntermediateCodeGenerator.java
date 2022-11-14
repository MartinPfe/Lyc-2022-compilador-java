package lyc.compiler.files;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class IntermediateCodeGenerator implements FileGenerator {

    private static boolean __logmsg = false;

    private static Integer celda_actual = 0;
    private static Stack<Integer> stack = new Stack<>();
    private static List<String> polaca = new ArrayList<String>();

    public static void insertar(Object o){
        if(__logmsg){ System.out.println("Insertar | contenido: " + o.toString() + " celda_actual: " + celda_actual); }

        polaca.add(o.toString());
        celda_actual++;

        if(__logmsg){
          System.out.print("Contenido de polaca:" );
          for (String s: polaca) {
              System.out.print(" " + s);
          }
          System.out.println("");
        }
    }

    //Solamente apila en el stack la posicion actual
    public static void apilar(){
        if(__logmsg){ System.out.println("Apilando: | contenido: " + polaca.size()); }
        stack.push(polaca.size());
    }

    //Apila en el stack y deja un lugar vacio
    public static void apilar_y_avanzar(){
        if(__logmsg){ System.out.println("Apilando: | contenido: " + polaca.size()); }
        stack.push(polaca.size());
        ++celda_actual;
    }

    //El offset indica cuantas celdas tengo que sumar a la celda_actual para hacer el brach de manera correcta
    public static void desapilar_e_insertar(int offset){
        Integer pos = stack.pop();
        Integer val = celda_actual + offset;
        if(__logmsg){ System.out.println("Desapilando | posicion: " + pos + " valor: " + val); }
        if(pos > polaca.size()){
            polaca.add(val.toString());
        }
        else{
            polaca.add(pos,val.toString());
        }
    }

    public static Integer obtener_tope_de_pila(){
        return stack.pop();
    }

    //cuantas veces quiero repetir, y desde donde
    public static void repetir_codigo(Integer cant, Integer inicio){
        Integer longitud = polaca.size();
        List<String> codigo = new ArrayList<String>();

        for(Integer i = inicio; i < longitud; ++i){
            codigo.add(polaca.get(i));
        }

        for(Integer i = cant - 1; i > 0; --i) {
            polaca.addAll(codigo);
        }
    }

    public static List<String> obtener_notacion_polaca(){
        return polaca;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        String res = "";

        if(__logmsg){
          System.out.print("Contenido de polaca:" );
          for (String s: polaca) {
              System.out.print(" " + s);
          }
          System.out.println("");
        }

        for(String valor : polaca){
          res += valor;
          res += "\n";
        }

        fileWriter.write(res);
    }
}
