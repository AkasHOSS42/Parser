import java.util.*;

/* Des interfaces me semblent plus adaptées. */
interface Expression{
    String eval(ColorEnvironment c, AbbEnv a) throws Exception;
}

interface Instruction{
    void exec(ColorEnvironment c, AbbEnv a) throws Exception;
}

class Doc implements Expression{
    private Suite_elts val;
    private Suite_decls d;

    public Doc(Suite_elts list, Suite_decls d){
	val=list;
	this.d=d;
    }

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	d.exec(c, a);
	return "<!DOCTYPE html>\n<html>\n<head>\n<title>Lecture en cours.</title>\n</head>\n<body>\n"
	    +val.eval(c, a)+"\n</body>\n</html>";
    }
}

interface Declaration extends Instruction{
    void exec(ColorEnvironment c, AbbEnv a) throws Exception;
}

class DeclCouleur implements Declaration{
    private String ID, CST;
    public DeclCouleur(String id, String cst){
	ID=id;
	CST=cst;
    }

    public void exec(ColorEnvironment c, AbbEnv a) throws Exception{
	c.addC(ID, CST);
    }
}

class DeclAbb implements Declaration{
    private String ID;
    private Suite_elts contenu;

    public DeclAbb(String id, Suite_elts list){
        ID=id;
        contenu=list;
    }

    public void exec(ColorEnvironment c, AbbEnv a) throws Exception{
	a.addC(ID, contenu);
    }
}

class Variable extends Element{
    private String key;

    public Variable(String k){
        key=k;
    }

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
        return a.getC(key).eval(c, a);
    }
}

/* Suite de déclarations. */
class Suite_decls extends LinkedList<Declaration> implements Instruction{
    public void exec(ColorEnvironment c, AbbEnv a) throws Exception{
	for(Instruction i : this)
	    i.exec(c, a);
    }
}

/* Suite d'éléments. */ 
class Suite_elts extends LinkedList<Element> implements Expression{
    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	String ans="";
	for(Element e : this)
	    ans=ans+e.eval(c, a);
	return ans+"\n";
    }
}

abstract class Element implements Expression{
    public abstract String eval(ColorEnvironment c, AbbEnv a) throws Exception;
}

class Mot extends Element{
    private String val;

    public Mot(String s){
	val=s;
    }

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{return val+" ";}
}

class NewLine extends Element{
    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{return "<br>\n";}
}

class Gras extends Element{
    private Suite_elts value;

    public Gras(Suite_elts list){
	value=list;
    }

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	return "<b>"+value.eval(c, a)+"</b>\n";
    }
}

class Italique extends Element{
    private Suite_elts value;

    public Italique(Suite_elts list){
	value=list;
    }

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	return "<i>"+value.eval(c, a)+"</i>\n";
    }
}

class Item implements Expression{
    private Suite_elts value;

    public Item(Suite_elts list){value=list;}

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	return "<li>"+value.eval(c, a)+"</li>\n";
    }
}

class Suite_item extends LinkedList<Item> implements Expression{
    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	String ans="";
	for(Item i : this)
	    ans+=i.eval(c, a);
	return ans;
    }
}

class Enumeration extends Element{
    private Suite_item value;

    public Enumeration(Suite_item list){value=list;}

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	return "<ol>"+value.eval(c, a)+"</ol>\n";
    }
}

/* correspond à \\color */
class EnCouleur extends Element{
    private Suite_elts contenu;
    private String couleur;
    private boolean isID;

    public EnCouleur(String val, boolean isID, Suite_elts list){
	contenu=list;
	couleur=val;
	this.isID=isID;
    }

    public String eval(ColorEnvironment c, AbbEnv a) throws Exception{
	if(isID)
	    couleur=c.getC(couleur);
	return "<font color=\""+couleur+"\">"+contenu.eval(c, a)+"</font>";
    }
}
