package lyc.compiler.constants;
import java.lang.Math;
import java.lang.Float;
import java.lang.Integer;
import lyc.compiler.files.SymbolTableGenerator;

public final class Constants {

    //public static final int MAX_LENGTH = 30;

    /* Rangos para cint ( 16 bits ), y cfloat ( 32 bits ) */
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
        switch(tipo){
          case TIPO_ID:
          if (valor.length() >= MIN_ID && valor.length() <= MAX_ID) {
            return true;
          }
          break;
          case TIPO_CINT:
            if (Integer.valueOf(valor) >= MIN_CINT && Integer.valueOf(valor) <= MAX_CINT) {
              return true;
            }
          break;
          case TIPO_CFLOAT:
            if((Float.valueOf(valor) >= MIN_CFLOAT && Float.valueOf(valor) <= MAX_CFLOAT) ||
               (Float.valueOf(valor) >= -MAX_CFLOAT && Float.valueOf(valor) <= -MIN_CFLOAT)){
              return true;
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

/* Funcion que verifica que el valor se encuentre dentro del rango del tipo de dato al que pertenece */
/*
void verificar_rango(Tipo tipo, char *valor, float rango_min, float rango_max){
	int ival;
	float fval;

	switch(tipo){
		case TIPO_ID:
			if((strlen(valor)) > (int)rango_max){
				printf(	"\n------------\n"
						"El id '%s' supera el rango maximo permitido ( %d caracteres )"
						"\n------------\n",
						valor,(int)rango_max);
				exit(1);
			}
		break;
		case TIPO_CINT:
			ival = atoi(valor);
			if(!(ival >= rango_min && ival <= rango_max)){
				printf(	"\n------------\n"
						"La constante '%s' se encuentra fuera del rango permitido ( %d ; %d )"
						"\n------------\n",
						valor,(int)rango_min,(int)rango_max);
				exit(1);
			}
		break;
		case TIPO_CFLOAT:
			fval = atof(valor);
			if(!(fval >= rango_min && fval <= rango_max) && !(fval >= -rango_max && fval <= -rango_min)){
				printf(	"\n------------\n"
						"La constante '%s' se encuentra fuera del rango permitido (+-)( %f ; %f )"
						"\n------------\n",
						valor,rango_min,rango_max);
				exit(1);
			}
		break;
		case TIPO_CSTRING:
			//El valor recibido tiene las comillas incluidas, entonces strlen(valor) retorna la longitud de la string + 2
			if((strlen(valor) - 2) > (int)rango_max){
				printf(	"\n------------\n"
						"La constante %s se encuentra fuera del rango permitido ( %d ; %d )"
						"\n------------\n",
						valor,rango_min,rango_max);
				exit(1);
			}
		break;
		default:
			printf("%s","\nTipo de dato invalido\nFinalizando programa");
			exit(1);
		break;
	}
}
*/
}
