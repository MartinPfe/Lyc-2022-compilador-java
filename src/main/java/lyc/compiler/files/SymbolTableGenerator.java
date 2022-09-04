package lyc.compiler.files;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class SymbolTableGenerator implements FileGenerator{
    //Longitud maxima de cada campo
    private static int MAX_NOMBRE     = 25;
    private static int MAX_TIPO       = 10;
    private static int MAX_VALOR      = 35;
    private static int MAX_LONGITUD   = 4;
    private static int str_nro        = 0;
    private static List<String> lista = new ArrayList<String>();

    public static enum Tipo {
      TIPO_ID,
      TIPO_CINT,
      TIPO_CFLOAT,
      TIPO_CSTRING
    };

    /* Funcion que se encarga de almacenar el valor recibido en la lista de simbolos ( siempre y cuando el valor no se encuentre duplicado ) */
    /* La funcion no genera el archivo de salida, sino que almacena todos los simbolos en una lista para que despues se puedan grabar en el archivo */
    public static void almacenarEnTabla(Tipo tipo, String lexema){
        /* Variable que uso para almacenar el contenido que se va a guardar en la ts */
        String linea_a = "";

        switch(tipo){
          case TIPO_ID:
            linea_a += String.format("%"+ (-MAX_NOMBRE) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s",lexema,"","","");
          break;
      		case TIPO_CINT:
            /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
            linea_a += String.format("_%"+ (-(MAX_NOMBRE - 1)) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s",lexema,"cint",lexema,"");
      		break;
      		case TIPO_CFLOAT:
            /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
            linea_a += String.format("_%"+ (-(MAX_NOMBRE - 1)) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s",lexema,"cfloat",lexema,"");
      		break;
      		case TIPO_CSTRING:
            /* El valor recibido tiene las comillas incluidas, entonces length() retorna la longitud de la string + 2 */
            /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _str */
            /* A el valor de lexema necesito sacarle las comillas */
            linea_a += String.format("_str%"+ (-(MAX_NOMBRE - 4)) + "d|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "d",str_nro,"cstring",lexema.substring(1,lexema.length() - 1),(lexema.length() - 2));
      			++str_nro;
      		break;
      		default:
            System.out.println("\nTipo de dato invalido\nFinalizando programa");
            System.exit(0);
      		break;
      	}

        /* Verifico que la entrada no este duplicada */
        if(!lista.contains(linea_a)){
          lista.add(linea_a);
        }
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        String res = "";

        res += String.format("%"+ (-MAX_NOMBRE) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s","Nombre","Valor","Tipo","Longitud");
        res += "\n";

        for(String valor : lista){
          res += valor;
          res += "\n";
        }

        /* Elimino el ultimo salto de linea */
        fileWriter.write(res.substring(0,res.length() - "\n".length()));
    }
}
