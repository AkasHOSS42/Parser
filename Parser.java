import java.util.*;

class Parser {
    protected LookAhead1 reader;

    public Parser(LookAhead1 r) {
	reader=r;
    }

    /* Quand on n'arrive pas à choisir quelle règle de production effectuer,
       cette méthode nous donne le string de l'exception à lancer. */
    private String getError(Sym[] t){
	String ans=t[0]+"";
	for(int i=1; i<t.length; i++)
	    ans+=", ou"+t[i];
	return "Erreur de syntaxe. Symbole attendu : "+ans+"; symbole trouvé : "+reader.getString()+". "+reader.getPos();
    }

    public Doc nontermDoc() throws ParserException{
        Suite_decls decls = nontermSuite_decls();
	Suite_elts elts = nontermCorps();
	reader.eat(Sym.EOF);
	return new Doc(elts,decls);
    }

    /* Remarque : on a ajouté un symbole non terminal intermédiaire pour les déclarations. */

    public Declaration nontermDeclaration() throws ParserException{
        if(reader.check(Sym.SET)){
            reader.eat(Sym.SET);
            reader.eat(Sym.LACC);
            String val = reader.getStringValue();
            reader.eat(Sym.MOT);
            reader.eat(Sym.RACC);
            reader.eat(Sym.LACC);
            String cst = reader.getStringValue();
            reader.eat(Sym.CONSTCOLOR);
            reader.eat(Sym.RACC);
            return new DeclCouleur(val, cst);
        }
        if(reader.check(Sym.ABB)){
            reader.eat(Sym.ABB);
            reader.eat(Sym.LACC);
            String var = reader.getStringValue();
            reader.eat(Sym.VARIABLE);
            reader.eat(Sym.RACC);
            reader.eat(Sym.LACC);
            Suite_elts el = nontermSuite_elts();
            reader.eat(Sym.RACC);
            return new DeclAbb(var, el);
        }
	throw new ParserException(getError(new Sym[]{Sym.SET, Sym.ABB}));
    }

    public Suite_decls nontermSuite_decls() throws ParserException{
        Suite_decls ans=new Suite_decls();
        if(reader.check(Sym.SET)||reader.check(Sym.ABB)){
            ans.add(nontermDeclaration());
            ans.addAll(nontermSuite_decls());
        }
        return ans;
    }

    public Suite_elts nontermCorps() throws ParserException{
	if(reader.check(Sym.BEGINDOC)){
	    reader.eat(Sym.BEGINDOC);
	    Suite_elts arg=nontermSuite_elts();
	    reader.eat(Sym.ENDDOC);
	    return arg;
	}
	throw new ParserException(getError(new Sym[]{Sym.BEGINDOC}));
    }

    public Suite_elts nontermSuite_elts() throws ParserException{
	Suite_elts ans=new Suite_elts();
	if(reader.check(Sym.MOT)||reader.check(Sym.NEWLINE)||
	   reader.check(Sym.BF)||reader.check(Sym.IT)||reader.check(Sym.BEGINENUM)||reader.check(Sym.COLOR)||reader.check(Sym.CONSTCOLOR)||reader.check(Sym.VARIABLE)){
	    ans.add(nontermElement());
	    ans.addAll(nontermSuite_elts());
        }
        return ans;
    }

    public Element nontermElement() throws ParserException{
	if(reader.check(Sym.MOT)){
	    String s=reader.getStringValue();
            reader.eat(Sym.MOT);
	    return new Mot(s);
	}
	if(reader.check(Sym.NEWLINE)){
	    reader.eat(Sym.NEWLINE);
	    return new NewLine();
	}
	if(reader.check(Sym.BF)){
	    reader.eat(Sym.BF);
	    reader.eat(Sym.LACC);
	    Suite_elts list=nontermSuite_elts();
	    reader.eat(Sym.RACC);
	    return new Gras(list);
	}
	if(reader.check(Sym.IT)){
	    reader.eat(Sym.IT);
	    reader.eat(Sym.LACC);
	    Suite_elts list=nontermSuite_elts();
	    reader.eat(Sym.RACC);
	    return new Italique(list);
	}
	/* Le langage décrit par CONSTCOLOR est inclus dans celui décrit par MOT. Dans une suite d'élements,
	   une CONSTCOLOR doit donc être interprétée comme un MOT. */
        if(reader.check(Sym.CONSTCOLOR)){
            String s=reader.getStringValue();
            reader.eat(Sym.CONSTCOLOR);
            return new Mot(s);
        }
        if(reader.check(Sym.COLOR)){
            reader.eat(Sym.COLOR);
            reader.eat(Sym.LACC);
            String val="";
            boolean isID=true;
            Suite_elts list=null;
            if(reader.check(Sym.CONSTCOLOR)){
                val = reader.getStringValue();
                isID = false;
                reader.eat(Sym.CONSTCOLOR);
            }else if(reader.check(Sym.MOT)){
                val = reader.getStringValue();
                reader.eat(Sym.MOT);
            }
            reader.eat(Sym.RACC);
            reader.eat(Sym.LACC);
            list=nontermSuite_elts();
            reader.eat(Sym.RACC);
            return new EnCouleur(val,isID,list);
        }
        if(reader.check(Sym.VARIABLE)){
            String var = reader.getStringValue();
            reader.eat(Sym.VARIABLE);
            return new Variable(var);
        }
	if(reader.check(Sym.BEGINENUM))
	    return nontermEnum();
	throw new ParserException(getError(new Sym[]{Sym.MOT, Sym.NEWLINE, Sym.BF, Sym.IT, Sym.CONSTCOLOR, Sym.COLOR, Sym.RACC, Sym.VARIABLE, Sym.BEGINENUM}));
    }

    public Enumeration nontermEnum()throws ParserException{
	if(reader.check(Sym.BEGINENUM)){
	    reader.eat(Sym.BEGINENUM);
	    Suite_item list=nontermSuite_item();
	    reader.eat(Sym.ENDENUM);
	    return new Enumeration(list);
	}
	throw new ParserException(getError(new Sym[]{Sym.BEGINENUM}));
    }

    public Suite_item nontermSuite_item() throws ParserException{
	Suite_item ans=new Suite_item();
	if(reader.check(Sym.ITEM)){
	    ans.add(nontermItem());
	    ans.addAll(nontermSuite_item());
	}
	return ans;
    }

    public Item nontermItem() throws ParserException{
	if(reader.check(Sym.ITEM)){
	    reader.eat(Sym.ITEM);
	    return new Item(nontermSuite_elts());
	}
	throw new ParserException(getError(new Sym[]{Sym.ITEM}));
    }
}
