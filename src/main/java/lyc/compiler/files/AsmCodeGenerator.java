package lyc.compiler.files;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.files.SymbolTableGenerator;
import lyc.compiler.files.IntermediateCodeGenerator;
import lyc.compiler.files.SymbolTableGenerator.Simbolo;

public class AsmCodeGenerator implements FileGenerator {
      private boolean __logmsg = false;
      private static String aux_sum = "__aux_sum"; //aux que uso para almacenar resultados de operaciones aritmeticas de suma
      private static String aux_res = "__aux_res"; //aux que uso para almacenar resultados de operaciones aritmeticas de resta
      private static String aux_div = "__aux_div"; //aux que uso para almacenar resultados de operaciones aritmeticas de division
      private static String aux_mul = "__aux_mul"; //aux que uso para almacenar resultados de operaciones aritmeticas de multiplicacion
      private static String aux_dec_int = "0";     //aux que uso para indicar la cantidad de decimales de las variables de tipo int
      private static String aux_dec_float = "3";   //aux que uso para indicar la cantidad de decimales de las variables de tipo float

      private String cabecera = "include macros.asm\n"  +
                                "include number.asm\n"  +
                                "\n.MODEL LARGE\n"      +
                                ".386\n"                +
                                ".STACK 200h\n";

      private static String data =  "\n.DATA" +
                                    "\n" + aux_sum + " dd ?" +
                                    "\n" + aux_res + " dd ?" +
                                    "\n" + aux_mul + " dd ?" +
                                    "\n" + aux_div + " dd ?" +
                                    "\n";

      private static String codigo =  "\n.CODE\n"       +
                                      "main:\n"         +
                                      "mov ax,@DATA\n"  +
                                      "mov ds,ax\n"     +
                                      "mov es,ax\n";

      private String fin =  "\n\nmov ax,4C00h\n" +
                            "int 21h\n" +
                            "END main";

      private void traducir_tabla_de_simbolos(){
          List<Simbolo> ts = SymbolTableGenerator.obtener_tabla_de_simbolos();

          for(Simbolo s: ts){
              if(s.tipo == SymbolTableGenerator.Tipo.TIPO_STRING){
                  data += s.nombre + " db ";
              }
              else{
                  data += s.nombre + " dd ";
              }
              //cte
              if(s.nombre.startsWith("_")){
                  if(s.tipo == SymbolTableGenerator.Tipo.TIPO_STRING){
                      data += "\"" + s.valor + "\",\"$\"";
                  }
                  else if(s.tipo == SymbolTableGenerator.Tipo.TIPO_INT){
                      data += s.valor + ".0";
                  }
                  else{
                      data += s.valor;
                  }
              }
              else{
                  data += "?";
              }
              data += "\n";
          }
      }

      public void traducir_codigo_intermedio(){
          traducir_tabla_de_simbolos();

          boolean asig = false;
          boolean read = false;
          boolean write = false;
          List<String> polaca = IntermediateCodeGenerator.obtener_notacion_polaca();
          Stack<String> stack = new Stack<>();

          if(__logmsg){
              System.out.println(polaca);
              System.out.println("*****************");
          }

          for(String s: polaca){
              switch(s){
                  case "=":
                      asig = true;
                      if(__logmsg){
                          System.out.println("Desapilando por =");
                          System.out.println(stack);
                          System.out.println("*****************");
                      }
                  break;

                  case "+":
                      if(__logmsg){
                          System.out.println("Desapilando por +");
                          System.out.println(stack);
                          System.out.println("*****************");
                      }
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfadd";
                      codigo += "\nfstp " + aux_sum;
                      stack.push(aux_sum);
                  break;

                  case "-":
                      if(__logmsg){
                          System.out.println("Desapilando por -");
                          System.out.println(stack);
                          System.out.println("*****************");
                      }
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfxch";
                      codigo += "\nfsub";
                      codigo += "\nfstp " + aux_res;
                      stack.push(aux_res);
                  break;

                  case "*":
                      if(__logmsg){
                          System.out.println("Desapilando por *");
                          System.out.println(stack);
                          System.out.println("*****************");
                      }
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfmul";
                      codigo += "\nfstp " + aux_mul;
                      stack.push(aux_mul);
                  break;

                  case "/":
                      if(__logmsg){
                          System.out.println("Desapilando por /");
                          System.out.println(stack);
                          System.out.println("*****************");
                      }
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfxch";
                      codigo += "\nfdiv";
                      codigo += "\nfstp " + aux_div;
                      stack.push(aux_div);
                  break;

                  case "READ":
                      read = true;
                  break;

                  case "WRITE":
                      write = true;
                  break;

                  default:
                      //deberia verificar que tipo de asignacion es, cint, cfloat, cstring ??
                      if(asig){
                          codigo += "\nfld " + stack.pop();
                          codigo += "\nfstp " + s;
                          asig = false;
                          continue;
                      }
                      if(read){
                        read = false;
                        continue;
                      }
                      if(write){
                          Simbolo simbolo = SymbolTableGenerator.obtener_simbolo_de_tabla(s);
                          if(simbolo == null){
                              System.out.println("ERROR");
                              System.exit(0);
                          }
                          if(simbolo.tipo == SymbolTableGenerator.Tipo.TIPO_STRING){
                              codigo += "\ndisplayString " + s;
                          }
                          else if(simbolo.tipo == SymbolTableGenerator.Tipo.TIPO_INT){
                              codigo += "\nDisplayFloat " + s + "," + aux_dec_int;
                          }
                          else if(simbolo.tipo == SymbolTableGenerator.Tipo.TIPO_FLOAT){
                              codigo += "\nDisplayFloat " + s + "," + aux_dec_float;
                          }
                          codigo += "\nnewline";
                          write = false;
                          continue;
                      }
                      stack.push(s);
                  break;
              }
          }
      }

      @Override
      public void generate(FileWriter fileWriter) throws IOException {
          String asm = cabecera + data + codigo + fin;
          fileWriter.write(asm);
      }
}
