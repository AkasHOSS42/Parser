import java.util.HashMap;

public class AbbEnv extends HashMap<String, Suite_elts>{
    public void addC(String k, Suite_elts v) throws VarException{ //ajouter une variable
	if(containsKey(k))
	    throw new VarException("Erreur : une seule abréviation par identifiant. "+k+" est déjà initialisé.");
	put(k, v);
    }

    public Suite_elts getC(String k) throws Exception{
	if(!containsKey(k))
	    throw new Exception("Erreur : identifiant "+k+" non initialisé.");
	return get(k);
    }
}
