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

    public static void apilar(){
        if(__logmsg){ System.out.println("Apilando: | contenido: " + polaca.size()); }
        stack.push(polaca.size());
        ++celda_actual;
    }

    public static void desapilar(){
        Integer pos = stack.pop();
        Integer val = celda_actual + 1;
        if(__logmsg){ System.out.println("Desapilando | posicion: " + pos + " valor: " + val); }
        if(pos > polaca.size()){
            polaca.add(val.toString());
        }
        else{
            polaca.add(pos,val.toString());
        }
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
