class Token {
    protected Sym symbol;

    public Token(Sym s) {
	symbol=s;
    }

    public Sym symbol() {
	return symbol;
    }

    public String toString(){
	return "Symbol : "+this.symbol;
    }    
}

class MotToken extends Token {
    private String value;

    public MotToken(String value){
        super(Sym.MOT);
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}

class ColorToken extends Token { // correspond à un code hexadécimal d'une couleur
    private String value;

    public ColorToken(String value){
        super(Sym.CONSTCOLOR);
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}

class VarToken extends Token{ // une variable commence par un backslash en simpletext
    private String value;

    public VarToken(String value){
        super(Sym.VARIABLE);
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
