import java.io.*;

class LookAhead1  {
    /* Le reader qui mange le flot de tokens. */

    private Token current;
    private Lexer lexer;

    public LookAhead1(Lexer l) throws Exception {
	lexer=l;
	current=lexer.yylex();
    }

    public boolean check(Sym s)
	throws ParserException {
	if(current==null)
	    throw new ParserException("Le fichier se termine trop tôt."); // A priori, cette instruction n'est jamais exécutée.
	return (current.symbol() == s); 
    }

    public void eat(Sym s) throws ParserException {
	if (!check(s)) {
	    throw new ParserException("\nErreur de syntaxe : symbole attendu : "+s+", symbole trouvé : "+current+"."+lexer.getPos());
	}
	try {
	    current=lexer.yylex();
	}catch(IOException e){
	    System.out.println(e);
	    System.exit(0);
	}
	catch(LexerException e){
	    System.out.println(e);
	    System.exit(0);
	}
    }

    public String getString() {
	return current.toString();
    }

    public String getStringValue() throws ParserException {
	/* Donne la valeur d'un token possèdant un attribut String. */
	if (current instanceof MotToken) {
	    MotToken t = (MotToken) current;
	    return t.getValue();
	}
        if(current instanceof ColorToken) {
            ColorToken t = (ColorToken) current;
            return t.getValue();
        }
        if(current instanceof VarToken){
            VarToken t =(VarToken) current;
            return t.getValue();
        }
	throw new ParserException("Erreur : "+current+" n'est pas censé avoir de valeur.");// à priori, cette exception n'est jamais lancée.   
    }

    public String getPos(){return lexer.getPos();}
}
