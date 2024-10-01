import java.util.HashMap;

public class ColorEnvironment extends HashMap<String, String>{
    public void addC(String k, String v) throws VarException{ // ajoute une couleur
	if(containsKey(k))
	    throw new VarException("Erreur : une seule couleur par identifiant. "+k+" est déjà initialisé.");
	put(k, v);
    }

    public String getC(String k) throws VarException{
	if(!containsKey(k))
	    throw new VarException("Erreur : identifiant "+k+" non initialisée.");
	return get(k);
    }
}
