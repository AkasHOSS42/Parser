%%
%public
%class Lexer
%unicode
%type Token
%line
%column


%yylexthrow LexerException


blank = [\n\r \t]
motbis = {alphanum}|[,?;./:!#]
mot={motbis}+
newline = \\linebreak
beginenum = \\beginenum
endenum = \\endenum
item = \\item
begindoc = \\begindoc
enddoc = \\enddoc
bf = \\bf
it = \\it
set = \\set
color = \\couleur
abb = \\abb
alphanum=[a-z]|[A-Z]|[0-9]
variable=\\{alphanum}+
hex = [A-F]|[0-9]
/* Un code hexadécimal suivi sans espace d'un élément de {motbis} doit être interprété comme un grand mot.
   Sans le dernier caractère de {constcolor}, il serait interprété comme une constcolor suivie d'un mot.*/
constcolor = #{hex}{hex}{hex}{hex}{hex}{hex}[^a-zA-Z0-9,?;./:!]

%{
public String getPos(){
  return "ligne "+yyline+", position "+yycolumn+".";
  }
%}
%%
{abb}           {return new Token(Sym.ABB);}
{constcolor}    {yypushback(1);// On remet dans le flot le dernier caractère qui n'est pas hexadécimal.
return new ColorToken(yytext());}
{mot}           {return new MotToken(yytext());}
{newline}       {return new Token(Sym.NEWLINE);}


"{"             {return new Token(Sym.LACC);}
"}"             {return new Token(Sym.RACC);}

{beginenum}     {return new Token(Sym.BEGINENUM);}
{endenum}       {return new Token(Sym.ENDENUM);}
{item}          {return new Token(Sym.ITEM);}

{bf}            {return new Token(Sym.BF);}
{it}            {return new Token(Sym.IT);}

{set}           {return new Token(Sym.SET);}
{color}         {return new Token(Sym.COLOR);}

{begindoc}      {return new Token(Sym.BEGINDOC);}
{enddoc}        {return new Token(Sym.ENDDOC);}
{variable}      {return new VarToken(yytext().substring(1, yytext().length()));}

{blank}		{}
<<EOF>>		{return new Token(Sym.EOF);}
[^]		{throw new LexerException(yyline, yycolumn);}



