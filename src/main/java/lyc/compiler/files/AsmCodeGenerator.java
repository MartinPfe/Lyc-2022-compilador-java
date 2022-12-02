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
      private static String aux_uno = "__aux_uno"; //variable que uso para representar 1
      private static String aux_figuales_pivot = "__figuales_pivot";
      private static String aux_figuales_cont = "__figuales_cont";
      private static String aux_figuales_exp = "__figuales_exp";

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
                                    "\n" + aux_uno + " dd 1.0" +
                                    "\n" + aux_figuales_pivot + " dd ?" +
                                    "\n" + aux_figuales_cont + " dd ?" +
                                    "\n" + aux_figuales_exp + " dd ?" +
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

          boolean cmp = false;
          boolean asig = false;
          boolean read = false;
          boolean write = false;
          List<String> polaca = IntermediateCodeGenerator.obtener_notacion_polaca();
          Stack<String> stack = new Stack<>();

          if(__logmsg){
              System.out.println("*****************");
              //System.out.println(polaca);
          }

          for(String s: polaca){
              //System.out.println("Valor: " + s);
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

                  case "CMP":
                      if(__logmsg){
                          System.out.println("cmp");
                          System.out.println("*****************");
                      }

                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfld " + stack.pop();
                      codigo += "\nfcom";
                      codigo += "\nfstsw ax";
                      codigo += "\nsahf";
                      cmp = true;
                  break;

                  case "BI":
                      if(__logmsg){
                          System.out.println("bi");
                          System.out.println("*****************");
                      }
                      codigo += "\njmp ";
                  break;

                  case "READ":
                      if(__logmsg){
                          System.out.println("read");
                          System.out.println("*****************");
                      }
                      read = true;
                  break;

                  case "WRITE":
                      if(__logmsg){
                          System.out.println("write");
                          System.out.println("*****************");
                      }
                      write = true;
                  break;

                  case "WHILE":
                      if(__logmsg){
                          System.out.println("while");
                          System.out.println("*****************");
                      }
                      //dummy
                  break;

                  case "INC_+1":
                      //codigo += "\nfld " + aux_uno;
                      stack.push(aux_uno);
                  break;

                  case "NOT":
                      //dummy
                  break;

                  default:
                      if(cmp){
                          //System.out.println(s);
                          if(s.equals("BLE")){
                              codigo += "\njna ";
                          }
                          else if(s.equals("BGT")){
                              codigo += "\nja ";
                          }
                          else if(s.equals("BGE")){
                              codigo += "\njae ";
                          }
                          else if(s.equals("BLT")){
                              codigo += "\njb ";
                          }
                          else if(s.equals("BNE")){
                              codigo += "\njne ";
                          }
                          cmp = false;
                          continue;
                      }
                      if(s.startsWith("__etiqueta")){
                          if(__logmsg){
                              System.out.println("etiqueta");
                              System.out.println("*****************");
                          }
                          if(s.contains(":")){
                              codigo += "\n" + s;
                              continue;
                          }
                          codigo += s;
                          continue;
                      }
                      if(asig){
                          String val = stack.pop();
                          Simbolo simbolo = SymbolTableGenerator.obtener_simbolo_de_tabla(val);
                          if(simbolo != null && (val.startsWith("_str") || simbolo.tipo == SymbolTableGenerator.Tipo.TIPO_STRING)){
                              codigo += "STRCPY " + s + "," + val;
                              asig = false;
                              continue;
                          }
                          else{
                              codigo += "\nfld " + val;
                              codigo += "\nfstp " + s;
                              asig = false;
                              continue;
                          }
                      }
                      if(read){
                        codigo += "\ngetString " + s;
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
                          codigo += "\nnewLine\n";
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
