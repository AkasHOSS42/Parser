public class LexerException extends Exception {
	public LexerException(int line, int column) {
		super("Erreur : caractère non attendu, ligne " + line + ", colonne " + column + ".");
	}
}
