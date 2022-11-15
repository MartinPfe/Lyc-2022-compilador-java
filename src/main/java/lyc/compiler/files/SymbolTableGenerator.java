package lyc.compiler.files;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class SymbolTableGenerator implements FileGenerator{

    public static enum Tipo {
      TIPO_X, //indica que no tiene tipo asignado
      TIPO_ID,
      TIPO_INT,
      TIPO_FLOAT,
      TIPO_STRING
    };

    public static class Simbolo{
      public String nombre;
      public Tipo tipo;
      public String valor;
      public String longitud;
    }

    //Longitud maxima de cada campo
    private static int str_nro        = 0;
    private static int MAX_NOMBRE     = 25;
    private static int MAX_TIPO       = 10;
    private static int MAX_VALOR      = 35;
    private static int MAX_LONGITUD   = 4;
    private static List<Simbolo> lista = new ArrayList<Simbolo>();

    /* Funcion que se encarga de almacenar el valor recibido en la lista de simbolos ( siempre y cuando el valor no se encuentre duplicado ) */
    /* La funcion no genera el archivo de salida, sino que almacena todos los simbolos en una lista para que despues se puedan grabar en el archivo */
    public static Simbolo almacenar_en_tabla(Tipo tipo, String lexema){
        /* Variable que uso para almacenar el contenido que se va a guardar en la ts */
        Simbolo s = new Simbolo();

        switch(tipo){
            case TIPO_ID:
                s.nombre    = lexema;
                s.tipo      = tipo;
                s.valor     = "";
                s.longitud  = "";
            break;

      		case TIPO_INT:
                /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
                s.nombre    = "_" + lexema;
                s.tipo      = tipo;
                s.valor     = lexema;
                s.longitud  = String.valueOf(lexema.length());
      		break;

      		case TIPO_FLOAT:
                /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
                s.nombre    = "_" + lexema.replace(".","_");
                s.tipo      = tipo;
                s.valor     = lexema;
                s.longitud  = String.valueOf(lexema.length());
      		break;

      		case TIPO_STRING:
                /* El valor recibido tiene las comillas incluidas, entonces length() retorna la longitud de la string + 2 */
                /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _str */
                /* A el valor de lexema necesito sacarle las comillas */
                s.nombre    = "_str" + String.valueOf(str_nro);
                s.tipo      = tipo;
                s.valor     = lexema.substring(1,lexema.length() - 1);
                s.longitud  = String.valueOf(lexema.length() - 2);
                ++str_nro;
      		break;

      		default:
                System.out.println("\nTipo de dato invalido\nFinalizando programa");
                System.exit(0);
      		break;
      	}

        for(Simbolo simbolo : lista){
            if(simbolo.nombre.equals(s.nombre)){
                return simbolo;
            }
        }

        lista.add(s);
        return s;
    }

    public static Simbolo obtener_simbolo_de_tabla(Object lexema){
        String str_lexema = lexema.toString();

        for(Simbolo s: lista){
            if(s.nombre.equals(str_lexema) || s.nombre.equals(str_lexema.replace(".","_"))){
                return s;
            }
        }
        return null;
    }

    public static void actualizar_tipo_de_dato(List<Object> lista_var, Tipo tipo_var){
        boolean encontrado = false;
        for(Object o : lista_var){
            String o_str = o.toString();

            for(Simbolo s: lista){
                if(s.nombre.equals(o_str)){
                    switch(tipo_var){
                        case TIPO_INT:
                                      s.tipo = Tipo.TIPO_INT;
                                      encontrado = true;
                        break;
                        case TIPO_FLOAT:
                                        s.tipo = Tipo.TIPO_FLOAT;
                                        encontrado = true;
                        break;
                        case TIPO_STRING:
                                          s.tipo = Tipo.TIPO_STRING;
                                          encontrado = true;
                        break;
                        default:
                          System.out.println("\nTipo de dato invalido\nFinalizando programa");
                          System.exit(0);
                    }
                }
                if(encontrado){
                    continue;
                }
            }
        }
    }

    public static boolean comparar_tipo(Simbolo simbolo, Tipo tipo){
        return simbolo.tipo == tipo;
    }

    public static List<Simbolo> obtener_tabla_de_simbolos(){
        return lista;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        String res = "";

        res += String.format("%"+ (-MAX_NOMBRE) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s","Nombre","Tipo","Valor","Longitud");
        res += "\n";

        for(Simbolo s : lista){
            switch(s.tipo){
              case TIPO_ID:
                    res += String.format("%" + (-MAX_NOMBRE)   + "s|",s.nombre);
                    res += String.format("%" + (-MAX_TIPO)     + "s|","");
                    res += String.format("%" + (-MAX_VALOR)    + "s|",s.valor);
                    res += String.format("%" + (-MAX_LONGITUD) + "s","");
                break;

          		case TIPO_INT:
                    /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
                    res += String.format("%" + (-MAX_NOMBRE)    + "s|",s.nombre);
                    res += String.format("%" + (-MAX_TIPO)      + "s|","Int");
                    res += String.format("%" + (-MAX_VALOR)     + "s|",s.valor);
                    res += String.format("%" + (-MAX_LONGITUD)  + "s",s.longitud);
          		break;

          		case TIPO_FLOAT:
                    /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
                    res += String.format("%" + (-MAX_NOMBRE)    + "s|",s.nombre);
                    res += String.format("%" + (-MAX_TIPO)      + "s|","Float");
                    res += String.format("%" + (-MAX_VALOR)     + "s|",s.valor);
                    res += String.format("%" + (-MAX_LONGITUD)  + "s",s.longitud);
          		break;

          		case TIPO_STRING:
                    /* El valor recibido tiene las comillas incluidas, entonces length() retorna la longitud de la string + 2 */
                    /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _str */
                    /* A el valor de lexema necesito sacarle las comillas */
                    res += String.format("%" + (-MAX_NOMBRE)    + "s|",s.nombre);
                    res += String.format("%" + (-MAX_TIPO)      + "s|","String");
                    res += String.format("%" + (-MAX_VALOR)     + "s|",s.valor);
                    res += String.format("%" + (-MAX_LONGITUD)  + "s",s.longitud);
                    ++str_nro;
          		break;

          		default:
                    System.out.println("\nTipo de dato invalido\nFinalizando programa");
                    System.exit(0);
          		break;
          	}

            res += "\n";
        }

        /* Elimino el ultimo salto de linea */
        fileWriter.write(res.substring(0,res.length() - "\n".length()));
    }
}
