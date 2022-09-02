package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
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
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}


/*OTROS CARACTERES*/
Llave_A = "{"
Llave_C = "}"
OpenBracket = "("
CloseBracket = ")"
Dospuntos = ":"
Comillas = \"
Comentario_Inicio = "/*"
Comentario_Fin = "*/"


/*DECLARACIONES*/
Letter = [a-zA-Z]
Digit = [0-9]
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]
WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+
Cte_Float = {Digit}*"."{Digit}*|0
Caracter = [a-z,A-Z,0-9,=,>,<,!,:,+,\,*,/,@,.,?]
Cte_String =  {Comillas}({Caracter}+|{WhiteSpace})*{Comillas}
Contenido = ({Caracter}+|{WhiteSpace})*
Comentario = {Comentario_Inicio}{Contenido}{Comentario_Fin}


/*PALABRAS RESERVADAS*/
If = "if"|"IF"|"If"
Else = "else"|"ELSE"|"Else"
While =	"while"|"WHILE"|"While"
Int = "int"|"INT"|"Int"
Float = "float"|"FLOAT"|"Float"
String = "string"|"STRING"|"String"
Write =	"write"|"WRITE"|"Write"
Read =	"read"|"READ"|"Read"
Init =	"init"|"INIT"|"Init"

/*OPERADORES*/
Plus = "+"
Mult = "*"
Sub = "-"
Div = "/"
Assig = "="
Op_Mayor = ">"
Op_Menor =	"<"
Op_MayorIgual = ">="
Op_MenorIgual =	"<="
Op_Igual = "=="
Op_Not = "not"|"NOT"|"Not"
Op_And = "&"
Op_Or = "||"



%%


/* keywords */

<YYINITIAL> {

  {WhiteSpace}                             { /* ignore */ }
  {Identifier}                             { return symbol(ParserSym.IDENTIFIER, yytext()); }
  {IntegerConstant}                        { return symbol(ParserSym.INTEGER_CONSTANT, yytext()); }
  {Cte_Float}                              { return symbol(ParserSym.CTE_FLOAT, yytext()); }
  {Cte_String}                             { return symbol(ParserSym.CTE_STRING, yytext()); }
  {Comentario}                             { /* ignore */ }

  /*PALABRAS RESERVADAS*/
  {If}                                      { return symbol(ParserSym.IF, yytext());}
  {Else}                                    { return symbol(ParserSym.ELSE, yytext()); }
  {While}                                   { return symbol(ParserSym.WHILE, yytext());}
  {Int}                                     { return symbol(ParserSym.INT, yytext());}
  {Float}                                   { return symbol(ParserSym.FLOAT, yytext());}
  {String}                                  { return symbol(ParserSym.STRING, yytext());}
  {Write}                                   { return symbol(ParserSym.WRITE, yytext());}
  {Read}                                    { return symbol(ParserSym.READ, yytext());}
  {Init}                                    { return symbol(ParserSym.INIT, yytext());}

  /*OPERADORES*/
  {Plus}                                    { return symbol(ParserSym.PLUS, yytext());}
  {Sub}                                     { return symbol(ParserSym.SUB, yytext());}
  {Mult}                                    { return symbol(ParserSym.MULT, yytext());}
  {Div}                                     { return symbol(ParserSym.DIV, yytext());}
  {Assig}                                   { return symbol(ParserSym.ASSIG, yytext());}
  {Op_Mayor}                                { return symbol(ParserSym.OP_MAYOR, yytext());}
  {Op_Menor}                                { return symbol(ParserSym.OP_MENOR, yytext());}
  {Op_MayorIgual}                           { return symbol(ParserSym.OP_MAYORIGUAL, yytext());}
  {Op_MenorIgual}                           { return symbol(ParserSym.OP_MENORIGUAL, yytext());}
  {Op_Igual}                                { return symbol(ParserSym.OP_IGUAL, yytext());}
  {Op_Not}                                  { return symbol(ParserSym.OP_NOT, yytext());}
  {Op_And}                                  { return symbol(ParserSym.OP_AND, yytext());}
  {Op_Or}                                   { return symbol(ParserSym.OP_OR, yytext());}

  /*OTROS CARACTERES*/
  {Llave_A}                                 { return symbol(ParserSym.LLAVE_A, yytext());}
  {Llave_C}                                 { return symbol(ParserSym.LLAVE_C, yytext());}
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET, yytext());}
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET, yytext());}
  {Dospuntos}                               { return symbol(ParserSym.DOSPUNTOS, yytext());}

}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
