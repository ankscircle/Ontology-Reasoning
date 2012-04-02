package soc.project3;
import com.hp.hpl.jena.rdf.model.Model;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
public class MyWordnetReasoner {
	String sensUrls;
	String simiUrls;
	String meroUrls;
	String antyUrls;
	
	public MyWordnetReasoner(String url1,String url2,String url3,String url4){
		this.sensUrls=url1;
		this.simiUrls=url2;
		this.meroUrls=url3;
		this.antyUrls=url4;
		
	}
	
	public static void checkWordUri(String word1,String word2)
	{
		String wd1Uri="hello",wd2Uri="hi";
		try
		{
		Model sch = FileManager.get().loadModel("file:p3.owl");
		Reasoner rea = ReasonerRegistry.getOWLReasoner();
		rea = rea.bindSchema(sch);
		Model model = ModelFactory.createDefaultModel();
     	Model data = model.read("http://courses.ncsu.edu/csc750/lec/001/hw/p3_resources/p3_senselabels.rdf");
		
		InfModel read = ModelFactory.createInfModel(rea,data);
		ResIterator iter =read.listSubjects();
		
	
		while (iter.hasNext( )) {
		RDFNode node = iter.next();
		if(node.isURIResource())
		{
		Resource res =read.getResource(node.toString());
		StmtIterator stmt =res.listProperties();
		res.getNameSpace();
		res.getURI();
		while(stmt.hasNext())
		{
			Statement stat = stmt.next();
			RDFNode node2 = stat.getObject();
			
			if(node2.toString().equals(word1+"@en-US"))
			{//System.out.println(" FOUND!!!"+node2.toString()+" Subject: "+ res2 +" Property: "+prop+" URI: "+node.toString());
			wd1Uri=node.toString();
			}
			if(node2.toString().equals(word2+"@en-US"))
			{//System.out.println(" FOUND!!!"+node2.toString()+"URI:"+node.toString());
			wd2Uri=node.toString();
			}
		}
		}
		}
		}catch(Exception e)
		{
			System.out.println(" " + e);
		}
			if(wd1Uri==wd2Uri)
			{
				System.out.println(""+word1+" SynonymOf "+word2);	
				return;
			}
			if(!wd1Uri.isEmpty())
			{
			if(checkAntonym(wd1Uri,wd2Uri))
			{
				System.out.println(""+word1+" AntonymOf "+word2);
				return;
			}
			}
			if(!wd1Uri.isEmpty())
			{
			if(checkMeronyms(wd1Uri,wd2Uri,word1,word2))
			{
				
				return;
			}	
			}
			
			if(!wd1Uri.isEmpty())
			{
			if(checkSimilarTo(wd1Uri,wd2Uri))
			{
				System.out.println(""+word1+" SimilarTo "+word2);
				return;
			}	
			}
			System.out.println("Unknown("+word1+","+word2+")");
			
			
		
	}
	public static Boolean checkAntonym(String Uri1,String Uri2)
	{
		Model model = ModelFactory.createDefaultModel();
     	Model data = model.read("http://courses.ncsu.edu/csc750/lec/001/hw/p3_resources/p3_antonyms.rdf");
     	Model sch = FileManager.get().loadModel("file:p3.owl");
		Reasoner rea = ReasonerRegistry.getOWLReasoner();
		rea = rea.bindSchema(sch);		
		InfModel read = ModelFactory.createInfModel(rea,data);
		Resource res = read.getResource(Uri1);
		StmtIterator stmt = res.listProperties();
		while(stmt.hasNext())
		{
			Statement s = stmt.next();
			Property prop=s.getPredicate();
			RDFNode rnode = s.getObject();
			if(prop.getLocalName().equals("AntonymOf"))
			{
				if(rnode.toString().equals(Uri2))
				return true;
			
		}
		}
		
		return false;
	}
	public static Boolean checkMeronyms(String Uri1,String Uri2,String word1,String word2)
	{
		Model model = ModelFactory.createDefaultModel();
     	Model data = model.read("http://courses.ncsu.edu/csc750/lec/001/hw/p3_resources/p3_meronyms.rdf");
     	Model sch = FileManager.get().loadModel("file:p3.owl");
		Reasoner rea = ReasonerRegistry.getOWLReasoner();
		rea = rea.bindSchema(sch);		
		InfModel read = ModelFactory.createInfModel(rea,data);
		Resource res = read.getResource(Uri1);
		StmtIterator stmt = res.listProperties();
		while(stmt.hasNext())
		{
			Statement s = stmt.next();
			Property prop=s.getPredicate();
			RDFNode rnode = s.getObject();
			if(prop.getLocalName().equals("partMeronymOf"))
			{
				
				if(rnode.toString().equals(Uri2))
				{
				System.out.println(""+word1+" MeronymOf "+word2);
				return true;
				}
			
		}
			if(prop.getLocalName().equals("partHolonymOf"))
			{
				
				if(rnode.toString().equals(Uri2))
				{
				System.out.println(""+word1+" HolonymOf "+word2);
				return true;
				}
		}
		}
		
		return false;
	}
	public static Boolean checkHolonyms(String Uri1,String Uri2)
	{
		Model model = ModelFactory.createDefaultModel();
     	Model data = model.read("http://courses.ncsu.edu/csc750/lec/001/hw/p3_resources/p3_meronyms.rdf");
     	Model sch = FileManager.get().loadModel("file:p3.owl");
		Reasoner rea = ReasonerRegistry.getOWLReasoner();
		rea = rea.bindSchema(sch);		
		InfModel read = ModelFactory.createInfModel(rea,data);
		
		Resource res = read.getResource(Uri1);
		StmtIterator stmt = res.listProperties();
		while(stmt.hasNext())
		{
			Statement s = stmt.next();
			Property prop=s.getPredicate();
			RDFNode rnode = s.getObject();
			if(prop.getLocalName().equals("partMeronymOf"))
			{
				if(rnode.toString().equals(Uri2))
				return true;
			
		}
		}
		
		return false;
	}
	public static Boolean checkSimilarTo(String Uri1,String Uri2)
	{
		Model model = ModelFactory.createDefaultModel();
     	Model data = model.read("http://courses.ncsu.edu/csc750/lec/001/hw/p3_resources/p3_similarity.rdf");
     	Model sch = FileManager.get().loadModel("file:p3.owl");
		Reasoner rea = ReasonerRegistry.getOWLReasoner();
		rea = rea.bindSchema(sch);		
		InfModel read = ModelFactory.createInfModel(rea,data);
		Resource res = read.getResource(Uri1);
		StmtIterator stmt = res.listProperties();
		while(stmt.hasNext())
		{
			Statement s = stmt.next();
			Property prop=s.getPredicate();
			RDFNode rnode = s.getObject();
			if(prop.getLocalName().equals("similarTo"))
			{
				if(rnode.toString().equals(Uri2))
				return true;
			
		}
		}
		
		return false;
	}
	
	public static void main(String[] args)
	{
		
			
		try{
			checkWordUri(args[0],args[1]);
		}catch(Exception e)
		{
			System.out.println(" " + e);
		}
		
	}

}
