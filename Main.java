import java.io.*;

class Main {
    public static void main(String[] args) throws Exception {
	/* On attend deux arguments. Le premier est le fichier simpletext à analyser, le second le nom deu fichier html à créer. */
	if (args.length < 2) {
	    System.out.println("Argument attendu");
	    System.exit(0);
	}

	File input = new File(args[0]);
	Reader reader = new FileReader(input);
	Lexer lexer = new Lexer(reader);
	LookAhead1 look = new LookAhead1(lexer);

	Parser parser = new Parser(look);

	/* Pour écrire dans un fichier de nom args[1] */
        BufferedWriter out=new BufferedWriter(new FileWriter(new File(args[1])));

        try{
            Expression e=parser.nontermDoc();
            String contenu=e.eval(new ColorEnvironment(), new AbbEnv());
            out.write(contenu);
            out.flush();
            out.close();
        }
        catch(Exception e){
            System.out.println("Le fichier est incorrect.");
            System.out.println(e);
        }
    }
}
