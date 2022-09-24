package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import lyc.compiler.files.SymbolTableGenerator;
import static lyc.compiler.constants.Constants.*;
%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private boolean logmsg = true;

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

/* Palabras reservadas */
t_prif      = "if"
t_prelse    = "else"
t_prinit    = "init"
t_prread    = "read"
t_prwrite   = "write"
t_prwhile   = "while"
t_print     = "Int"
t_prfloat   = "Float"
t_prstring  = "String"
t_prrepeat  = "REPEAT"
t_priguales = "#Iguales"

t_or    = "||"
t_and   = "&"
t_not   = "not"
t_sum   = "+"
t_res   = "-"
t_mul   = "*"
t_div   = "/"
t_asig  = "="
t_coma  = ","
t_dosp  = ":"
t_para  = "("
t_parc  = ")"
t_cora  = "["
t_corc  = "]"
t_llava = "{"
t_llavc = "}"
t_punto = "."
t_mayor = ">"
t_menor = "<"

t_comi  = "/*"
t_comf  = "*/"

t_digcc = [0-9]
t_digsc = [1-9]
t_letra = [a-zA-Z]

t_com     = {t_comi}(.)*{t_comf}
t_id      = {t_letra}({t_letra}|{t_digcc})*
t_cint    = 0|{t_res}?{t_digsc}{t_digcc}*
t_cfloat  = {t_res}?{t_punto}{t_digcc}+|{t_res}?{t_digsc}+{t_punto}{t_digcc}*
t_cstring = \"[^\n\"]*\"

/* Caracteres especiales */
LineTerminator  = \r|\n|\r\n
InputCharacter  = [^\r\n]
Identation      =  [ \t\f]
blanco          = {LineTerminator} | {Identation}

Caracter = [a-z,A-Z,0-9,=,>,<,!,:,+,\,*,/,@,.,?]
Contenido = ({Caracter}+|{blanco})*
Comentario = {t_comi}{Contenido}{t_comf}


SALTO_LINEA			=	"\n|\r\n|\r"
MENOS_ASTERISCO		=	[^*]
COMENTARIO_MULTI    =   "/*" ({SALTO_LINEA}* {MENOS_ASTERISCO}* {SALTO_LINEA}*)* "*/"
CUALQUIER_CARACTER 	= 	[^\r\n]
COMENTARIO_SINGLE   =   "//" {CUALQUIER_CARACTER}* {SALTO_LINEA}?
%%

/* keywords */

<YYINITIAL> {
  {t_or}        { if(logmsg){ System.out.println("t_or");     } return symbol(ParserSym.T_OR);    }
  {t_and}       { if(logmsg){ System.out.println("t_and");    } return symbol(ParserSym.T_AND);   }
  {t_not}       { if(logmsg){ System.out.println("t_not");    } return symbol(ParserSym.T_NOT);   }
  {t_sum}       { if(logmsg){ System.out.println("t_sum");    } return symbol(ParserSym.T_SUM);   }
  {t_res}       { if(logmsg){ System.out.println("t_res");    } return symbol(ParserSym.T_RES);   }
  {t_mul}       { if(logmsg){ System.out.println("t_mul");    } return symbol(ParserSym.T_MUL);   }
  {t_div}       { if(logmsg){ System.out.println("t_div");    } return symbol(ParserSym.T_DIV);   }
  {t_asig}      { if(logmsg){ System.out.println("t_asig");   } return symbol(ParserSym.T_ASIG);  }
  {t_coma}      { if(logmsg){ System.out.println("t_coma");   } return symbol(ParserSym.T_COMA);  }
  {t_dosp}      { if(logmsg){ System.out.println("t_dosp");   } return symbol(ParserSym.T_DOSP);  }
  {t_para}      { if(logmsg){ System.out.println("t_para");   } return symbol(ParserSym.T_PARA);  }
  {t_parc}      { if(logmsg){ System.out.println("t_parc");   } return symbol(ParserSym.T_PARC);  }
  {t_cora}      { if(logmsg){ System.out.println("t_cora");   } return symbol(ParserSym.T_CORA);  }
  {t_corc}      { if(logmsg){ System.out.println("t_corc");   } return symbol(ParserSym.T_CORC);  }
  {t_llava}     { if(logmsg){ System.out.println("t_llava");  } return symbol(ParserSym.T_LLAVA); }
  {t_llavc}     { if(logmsg){ System.out.println("t_llavc");  } return symbol(ParserSym.T_LLAVC); }
  {t_mayor}     { if(logmsg){ System.out.println("t_mayor");  } return symbol(ParserSym.T_MAYOR); }
  {t_menor}     { if(logmsg){ System.out.println("t_menor");  } return symbol(ParserSym.T_MENOR); }

  {t_prif}      { if(logmsg){ System.out.println("t_prif");     } return symbol(ParserSym.T_PRIF);      }
  {t_prelse}    { if(logmsg){ System.out.println("t_prelse");   } return symbol(ParserSym.T_PRELSE);    }
  {t_prinit}    { if(logmsg){ System.out.println("t_prinit");   } return symbol(ParserSym.T_PRINIT);    }
  {t_prread}    { if(logmsg){ System.out.println("t_prread");   } return symbol(ParserSym.T_PRREAD);    }
  {t_prwrite}   { if(logmsg){ System.out.println("t_prwrite");  } return symbol(ParserSym.T_PRWRITE);   }
  {t_prwhile}   { if(logmsg){ System.out.println("t_prwhile");  } return symbol(ParserSym.T_PRWHILE);   }
  {t_print}     { if(logmsg){ System.out.println("t_print");    } return symbol(ParserSym.T_PRINT);     }
  {t_prfloat}   { if(logmsg){ System.out.println("t_prfloat");  } return symbol(ParserSym.T_PRFLOAT);   }
  {t_prstring}  { if(logmsg){ System.out.println("t_prstring"); } return symbol(ParserSym.T_PRSTRING);  }
  {t_prrepeat}  { if(logmsg){ System.out.println("t_prrepeat"); } return symbol(ParserSym.T_PRREPEAT);  }
  {t_priguales} { if(logmsg){ System.out.println("t_priguales");} return symbol(ParserSym.T_PRIGUALES); }

  {t_id}        {
                  if(logmsg){ System.out.println("t_id"); }

                  if(!verificar_rango(SymbolTableGenerator.Tipo.TIPO_ID,yytext())){
                    throw new InvalidLengthException("\n------------\n" +
                                                     "El id ( " + yytext() + " )" +
                                                     " se encuentra fuera del rango permitido ( " + MIN_ID + " ; " + MAX_ID + " )" +
                                                     "\n------------\n"
                                                    );
                  }

                  SymbolTableGenerator.almacenarEnTabla(SymbolTableGenerator.Tipo.TIPO_ID,yytext());
                  return symbol(ParserSym.T_ID, yytext());
                }

  {t_cint}      {
                  if(logmsg){ System.out.println("t_cint"); }

                  if(!verificar_rango(SymbolTableGenerator.Tipo.TIPO_CINT,yytext())){
                    throw new InvalidIntegerException("\n------------\n" +
                                                      "La constante int ( " + yytext() + " )" +
                                                      " se encuentra fuera del rango permitido ( " + MIN_CINT + " ; " + MAX_CINT + " )" +
                                                      "\n------------\n"
                                                     );
                  }

                  SymbolTableGenerator.almacenarEnTabla(SymbolTableGenerator.Tipo.TIPO_CINT,yytext());
                  return symbol(ParserSym.T_CINT, yytext());
                }

  {t_cfloat}    {
                  if(logmsg){ System.out.println("t_cfloat"); }

                  if(!verificar_rango(SymbolTableGenerator.Tipo.TIPO_CFLOAT,yytext())){
                    System.out.println("\n------------\n" +
                                       "La constante float ( " + yytext() + " )" +
                                       " se encuentra fuera del rango permitido (+-)( " + MIN_CFLOAT + " ; " + MAX_CFLOAT + " )" +
                                       "\n------------\n"
                                      );
                    System.exit(0);
                  }

                  SymbolTableGenerator.almacenarEnTabla(SymbolTableGenerator.Tipo.TIPO_CFLOAT,yytext());
                  return symbol(ParserSym.T_CFLOAT, yytext());
                }

  {t_cstring}   {
                  if(logmsg){ System.out.println("t_cstring " + yytext());  }

                  if(!verificar_rango(SymbolTableGenerator.Tipo.TIPO_CSTRING,yytext())){
                    throw new InvalidLengthException("\n------------\n" +
                                                     "La constante string " + yytext() +
                                                     " se encuentra fuera del rango permitido ( " + MIN_CSTRING + " ; " + MAX_CSTRING + " )" +
                                                     "\n------------\n"
                                                    );
                  }

                  SymbolTableGenerator.almacenarEnTabla(SymbolTableGenerator.Tipo.TIPO_CSTRING,yytext());
                  return symbol(ParserSym.T_CSTRING, yytext());
                }

  {Comentario}       {  if(logmsg){ System.out.println("COMENTARIO DETECTADO");  } }
  {blanco}      { /* do nothing */ }
{COMENTARIO_SINGLE} {	/*ignorar*/										}
{COMENTARIO_MULTI}  {	/*ignorar*/										}
  "\n"				{}
  "\t"				{}
  "\n\t"				{}
  " "					{}
  "\r\n"				{}
}

/* error fallback */
[^]             { if(logmsg){ System.out.println("FALLBACK");  }; throw new UnknownCharacterException(yytext()); }
