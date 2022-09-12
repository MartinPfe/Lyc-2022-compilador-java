package lyc.compiler.constants;
import java.lang.Math;
import java.lang.Float;
import java.lang.Integer;
import lyc.compiler.files.SymbolTableGenerator;

public final class Constants {
    public static final int MAX_ID        = 10;
    public static final int MIN_ID        = 1;
    public static final int MAX_CINT      = 32767;
    public static final int MIN_CINT      = -32768;
    public static final int MAX_CSTRING   = 40;
    public static final int MIN_CSTRING   = 0;
    public static final double MAX_CFLOAT = Math.pow(3.4,38);
    public static final double MIN_CFLOAT = Math.pow(1.18,-38);

    private Constants(){}

    public static boolean verificar_rango(SymbolTableGenerator.Tipo tipo, String valor){
        //Dejo la parte del catch vacia intencionalmente
        switch(tipo){
          case TIPO_ID:
          if (valor.length() >= MIN_ID && valor.length() <= MAX_ID) {
            return true;
          }
          break;
          case TIPO_CINT:
            try {
              if (Integer.valueOf(valor) >= MIN_CINT && Integer.valueOf(valor) <= MAX_CINT) {
                return true;
              }
            } catch(Exception e) {
            }
          break;
          case TIPO_CFLOAT:
            try {
              if((Float.valueOf(valor) >= MIN_CFLOAT && Float.valueOf(valor) <= MAX_CFLOAT) ||
              (Float.valueOf(valor) >= -MAX_CFLOAT && Float.valueOf(valor) <= -MIN_CFLOAT)){
                return true;
              }
            } catch(Exception e) {
            }
          break;
          case TIPO_CSTRING:
            /* El valor recibido tiene las comillas incluidas, entonces length() retorna la longitud de la string + 2 */
            if ((valor.length() - 2) >= MIN_CSTRING && (valor.length() - 2) <= MAX_CSTRING) {
              return true;
            }
          break;
          default:
            System.out.println("\nTipo de dato invalido\nFinalizando programa");
            System.exit(0);
          break;
        }

        return false;
    }
}
