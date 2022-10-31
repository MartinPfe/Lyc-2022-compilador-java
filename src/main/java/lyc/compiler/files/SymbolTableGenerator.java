package lyc.compiler.files;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class SymbolTableGenerator implements FileGenerator{

    public static class Simbolo{
      public String nombre;
      public String tipo;
      public String valor;
      public String longitud;
    }

    public static enum Tipo {
      TIPO_ID,
      TIPO_INT,
      TIPO_FLOAT,
      TIPO_STRING
    };

    //Longitud maxima de cada campo
    private static int MAX_NOMBRE     = 25;
    private static int MAX_TIPO       = 10;
    private static int MAX_VALOR      = 35;
    private static int MAX_LONGITUD   = 4;
    private static int str_nro        = 0;
    private static List<Simbolo> lista = new ArrayList<Simbolo>();

    /* Funcion que se encarga de almacenar el valor recibido en la lista de simbolos ( siempre y cuando el valor no se encuentre duplicado ) */
    /* La funcion no genera el archivo de salida, sino que almacena todos los simbolos en una lista para que despues se puedan grabar en el archivo */
    public static void almacenarEnTabla(Tipo tipo, String lexema){
        /* Variable que uso para almacenar el contenido que se va a guardar en la ts */
        Simbolo s = new Simbolo();

        switch(tipo){
            case TIPO_ID:
                s.nombre    = String.format("%" + (-MAX_NOMBRE)   + "s",lexema);
                s.tipo      = String.format("%" + (-MAX_TIPO)     + "s","");
                s.valor     = String.format("%" + (-MAX_VALOR)    + "s","");
                s.longitud  = String.format("%" + (-MAX_LONGITUD) + "s","");

                //linea_a += String.format("%"+ (-MAX_NOMBRE) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s",lexema,"","", "");
            break;

      		case TIPO_INT:
                /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
                s.nombre    = String.format("_%" + (-(MAX_NOMBRE - 1))  + "s",lexema);
                s.tipo      = String.format("%"  + (-MAX_TIPO)          + "s","Int");
                s.valor     = String.format("%"  + (-MAX_VALOR)         + "s",lexema);
                s.longitud  = String.format("%"  + (-MAX_LONGITUD)      + "s",lexema.length());

                //linea_a += String.format("_%"+ (-(MAX_NOMBRE - 1)) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s",lexema,"cint",lexema, lexema.length());
      		break;

      		case TIPO_FLOAT:
                /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _ */
                s.nombre    = String.format("_%" + (-(MAX_NOMBRE - 1))  + "s",lexema);
                s.tipo      = String.format("%"  + (-MAX_TIPO)          + "s","Float");
                s.valor     = String.format("%"  + (-MAX_VALOR)         + "s",lexema);
                s.longitud  = String.format("%"  + (-MAX_LONGITUD)      + "s",lexema.length());

                //linea_a += String.format("_%"+ (-(MAX_NOMBRE - 1)) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s",lexema,"cfloat",lexema, lexema.length() );
      		break;

      		case TIPO_STRING:
                /* El valor recibido tiene las comillas incluidas, entonces length() retorna la longitud de la string + 2 */
                /* A el valor de MAX_NOMBRE necesito sacarle la longitud de _str */
                /* A el valor de lexema necesito sacarle las comillas */
                s.nombre    = String.format("_str%" + (-(MAX_NOMBRE - 4)) + "d",str_nro);
                s.tipo      = String.format("%"     + (-MAX_TIPO)         + "s","String");
                s.valor     = String.format("%"     + (-MAX_VALOR)        + "s",lexema.substring(1,lexema.length() - 1));
                s.longitud  = String.format("%"     + (-MAX_LONGITUD)     + "d",(lexema.length() - 2));

                //linea_a += String.format("_str%"+ (-(MAX_NOMBRE - 4)) + "d|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "d",str_nro,"cstring",lexema.substring(1,lexema.length() - 1),(lexema.length() - 2));
                ++str_nro;
      		break;

      		default:
                System.out.println("\nTipo de dato invalido\nFinalizando programa");
                System.exit(0);
      		break;
      	}

        for(Simbolo simbolo : lista){
            if(simbolo.nombre.equals(s.nombre)){
                //System.out.println("*****Ignorando: " + s.nombre + " " + s.tipo + " " + s.valor + " " + s.longitud);
                return;
            }
        }

        lista.add(s);
    }

    public static Simbolo obtener_simbolo_de_tabla(Object lexema){
        String str_lexema = lexema.toString();

        for(Simbolo s: lista){

            if(s.nombre.trim().equals(str_lexema)){
                return s;
            }
        }
        return null;
    }

    public static void actualizar_tipo_de_dato(List<Object> lista_var, Tipo tipo_var){
        boolean encontrado = false;
        for(Object o : lista_var){
            for(Simbolo s: lista){
                if(s.nombre.trim().equals(o.toString())){
                    switch(tipo_var){
                        case TIPO_INT:
                                      s.tipo = String.format("%"  + (-MAX_TIPO) + "s","Int");
                                      encontrado = true;
                        break;
                        case TIPO_FLOAT:
                                        s.tipo = String.format("%"  + (-MAX_TIPO) + "s","Float");
                                        encontrado = true;
                        break;
                        case TIPO_STRING:
                                          s.tipo = String.format("%"     + (-MAX_TIPO) + "s","String");
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
        String str_tipo = "";

        switch(tipo){
            case TIPO_INT: str_tipo = "Int";
            break;
            case TIPO_FLOAT: str_tipo = "Float";
            break;
            case TIPO_STRING: str_tipo = "String";
            break;
            default:
              System.out.println("\nTipo de dato invalido\nFinalizando programa");
              System.exit(0);
        }

        return simbolo.tipo.equals(str_tipo);
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        String res = "";

        res += String.format("%"+ (-MAX_NOMBRE) + "s|" + "%" + (-MAX_TIPO) + "s|" + "%" + (-MAX_VALOR) + "s|" + "%" + (-MAX_LONGITUD) + "s","Nombre","Tipo","Valor","Longitud");
        res += "\n";

        for(Simbolo s : lista){
            res += s.nombre + "|" + s.tipo + "|" + s.valor + "|" + s.longitud;
            res += "\n";
        }

        /* Elimino el ultimo salto de linea */
        fileWriter.write(res.substring(0,res.length() - "\n".length()));
    }
}
