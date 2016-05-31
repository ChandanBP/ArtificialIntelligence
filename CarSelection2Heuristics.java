/*Author - Chandan Bychapura Parameswaraiah
 * cbp140230
 * This class includes implementation of heuristic which involves
 * selecting the variable with least order or weight 
 * for expansion and search to progress to reach the goal node*/
package finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class CarSelection2Heuristics 
{
	boolean userinput[];
   	
   	int numConstraints;
   	
   	int satisfiedConstraints;
	
	String ac;
   	
   	String bodystyle;
   	
   	String make;
   	
   	String color;
   	
   	String engine;
   	
   	String queryString;
   	
   	String namespace;
   	
   	Query query;
   	
   	QueryExecution qe;
   	
   	ResultSet resultSet;
   	
   	OntModel ontmodel;
   	
   	Path input;
   	
   	ArrayList<Node> queue;
   	
   	HashMap<String, Boolean>map;
   	
   	boolean goal;
   	
   	ArrayList<PriorityQueue<Node>>test;
   	
   	PriorityQueue<Node>pt;
   	
   	public CarSelection2Heuristics(){
   	   userinput = new boolean[5];
   	   numConstraints = 0;
   	   satisfiedConstraints = 0;
   	   
		input = Paths.get("CarSelection.owl");
		
		// Initialize the ONTOLOGY model
		ontmodel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		ontmodel.read(input.toUri().toString(), "RDF/XML");
   	    namespace = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				   "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				   "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				   "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				   "PREFIX car: <http://www.semanticweb.org/chandan/ontologies/2014/10/untitled-ontology-9#>";
   	    
   	    queue = new ArrayList<Node>();
   	    
   	    map = new HashMap<String, Boolean>();
		map.put("ac", false);
		map.put("bodystyle", false);
		map.put("make", false);
		map.put("color", false);
		map.put("engine", false);
   	    goal = false;
   	    test = new ArrayList<PriorityQueue<Node>>();
   	    pt = new PriorityQueue<Node>();
   	}
	
	public void readUserInput(){
   		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
   		try{
   			
   			System.out.println("Enter AC Type");
   			ac = buffReader.readLine();
   			if(!ac.isEmpty()){
   				userinput[0] = true;
   				numConstraints++;
   			}
   			
   			System.out.println("Enter Body Style");
   			bodystyle = buffReader.readLine();
   			if(!bodystyle.isEmpty()){
   				userinput[1] = true;
   				numConstraints++;
   			}
   			
   			System.out.println("Enter make");
   			make = buffReader.readLine();
   			if(!make.isEmpty()){
   				userinput[2] = true;
   				numConstraints++;
   			}
   			
   			System.out.println("Enter color");
   			color = buffReader.readLine();
   			if(!color.isEmpty()){
   				userinput[3] = true;
   				numConstraints++;
   			}
   			
   			System.out.println("Enter Engine Type");
   			engine = buffReader.readLine();
   			if(!engine.isEmpty()){
   				userinput[4] = true;
   				numConstraints++;
   			}
   		}
   		catch(IOException ioException){
   			ioException.printStackTrace();
   		}
   	}	
	
	public Node execQuery(Node parent){
		
		   List<QuerySolution>list;
		   Node node = null;
		   int num;
		   
		   query = QueryFactory.create(queryString);
		   qe = QueryExecutionFactory.create(query, ontmodel);
		   resultSet = qe.execSelect();
		   
		   list =  ResultSetFormatter.toList(resultSet);
		   num = list.size();
		   if(num!=0){
			   node = new Node();
			   node.hn = false;
			   node.numInstances = num;
			   node.setParent(parent);
			   node.setQuery(queryString);
		   }

		   return node;
	   }
	
	public Node getACS(String AC,Node parent){
		   
		   Node node=null;
		   queryString = "";
		   
		   if(parent == null){
			   if(AC.equalsIgnoreCase("Auto")){
				   queryString = namespace+Queries.AUTOMATIC_AC_QUERY;
			   }
			   if(AC.equalsIgnoreCase("Manual")){
				   queryString = namespace+Queries.MANUAL_AC_QUERY;
			   }
		   }
		   else{
			   queryString = parent.query.substring(0, parent.query.length()-1);
			   
			   if(AC.equalsIgnoreCase("Auto")){
				   queryString = queryString+"?cars car:hasAutomaticAC ?ac.}";   
			   }
			   if(AC.equalsIgnoreCase("Manual")){
				   queryString = queryString+"?cars car:hasManualAC ?ac.}";
			   }
		   }
		   
		   node = execQuery(parent);
		   return node;
	   }
	
	public Node getBodyStyleCars(String BodyStyle,Node parent){
		   
		   Node node;
		   
		   queryString = "";
		  
	        if(parent==null){
		   
		       if(BodyStyle.equalsIgnoreCase("Coupe")){
				   queryString = namespace+Queries.COUPE_QUERY;
			   }
			   if(BodyStyle.equalsIgnoreCase("Sedan")){
				   queryString = namespace+Queries.SEDAN_QUERY;
			   }
			   if(BodyStyle.equalsIgnoreCase("Suv")){
				   queryString = namespace+Queries.SUV_QUERY;
			   }
	        }
	        else{
	        	
	           queryString = parent.query.substring(0, parent.query.length()-1);	
	           
	           if(BodyStyle.equalsIgnoreCase("Coupe")){
	        	   queryString = queryString+"?cars car:hasCoupeBodyStyle ?coupe.}";
	 		   }
	 		   if(BodyStyle.equalsIgnoreCase("Sedan")){
	 			  queryString = queryString+"?cars car:hasSedanBodyStyle ?sedan.}";
	 		   }
	 		   if(BodyStyle.equalsIgnoreCase("Suv")){
	 			  queryString = queryString+"?cars car:hasSUVBodyStyle ?suv.}";
	 		   }
	        }
				 
		   node = execQuery(parent);
		   return node;
	   }
	
	public Node initQuery(){
		   
		   Node node=null;
		   
		   if(!ac.isEmpty()){
			   node = getACS(ac,null);
			   if(node!=null &&node.numInstances>=1){
				   satisfiedConstraints = 1;
				   node.constrSatisfied = satisfiedConstraints;
				   node.hn = true;
				   node.constraint = "ac";
				   node.order = 3;
				   node.parent = null;
				   queue.add(node);
				   pt.add(node);
				   test.add(pt);
			   }
		   }
		   
		   if(!bodystyle.isEmpty()){
			   node = getBodyStyleCars(bodystyle,null);
			   if(node!=null && node.numInstances>=1){
				   satisfiedConstraints = 1;
				   node.hn = true;
				   node.constrSatisfied = satisfiedConstraints;
				   node.constraint = "bodystyle";
				   node.order = 5;
				   node.parent = null;
				   queue.add(node);
				   pt.add(node);
				   test.add(pt);
			   }
		   }

		   	if(!make.isEmpty()){
			   node = getMake(make,null);
			   if(node!=null && node.numInstances>=1){
				   satisfiedConstraints = 1;
				   node.hn = true;
				   node.constrSatisfied = satisfiedConstraints;
				   node.constraint = "make";
				   node.order = 1;
				   node.parent = null;
				   queue.add(node);
				   pt.add(node);
				   test.add(pt);
			   }
		   }
		   
		   if(!color.isEmpty()){
			   node = getColorCars(color,null);
			   if(node!=null && node.numInstances>=1){
				   satisfiedConstraints = 1;
				   node.hn = true;
				   node.constrSatisfied = satisfiedConstraints;
				   node.constraint = "color";
				   node.order = 4;
				   node.parent = null;
				   queue.add(node);
				   pt.add(node);
				   test.add(pt);
			   }
		   }
		   
		   if(!engine.isEmpty()){
			   node = getEngineType(engine,null);
			   if(node!=null && node.numInstances>=1){
				   satisfiedConstraints = 1;
				   node.hn = true;
				   node.order = 2;
				   node.constrSatisfied = satisfiedConstraints;
				   node.constraint = "engine";
				   node.parent = null;
				   queue.add(node);
				   pt.add(node);
				   test.add(pt);
			   }
		   }
		   return node;
	   }
	
	public Node getColorCars(String Color,Node parent){
		   
		   Node node=null;
		   queryString = "";
		   
		   if(parent == null){
			   
			   queryString = namespace+"Select ?cars "
					   + "WHERE{"
					   + "?cars car:hasColor ?colors."
					   + "?instance rdfs:subClassOf car:Color."
					   + "?colors rdf:type ?instance."
					   + "?colors car:color ?x.FILTER(?x='"+color+"').}";
		   
				  node= execQuery(parent);
		   }
		   else{
			   queryString = parent.query.substring(0, parent.query.length()-1);
			   queryString = queryString+"?cars car:hasColor ?colors."
					   + "?instance rdfs:subClassOf car:Color."
					   + "?colors rdf:type ?instance."
					   + "?colors car:color ?x.FILTER(?x='"+color+"').}";
			   node= execQuery(parent);
		   }
		   return node;
	   }
	
	public Node getEngineType(String Engine,Node parent){
		   
		   Node node;
		   queryString = "";
		   
		   
		   if(parent == null){
			   queryString = (engine.equalsIgnoreCase("Petrol"))? namespace+Queries.PETROL_ENGINES_QUERY:
	           										namespace+Queries.DIESEL_ENGINES_QUERY;
		   }
		   else{
			   queryString = parent.query.substring(0, parent.query.length()-1);
			   
			   if(engine.equalsIgnoreCase("Petrol")){
				   queryString = queryString+"?cars car:hasPetrolEngine ?engine.}";  
			   }
			   if(engine.equalsIgnoreCase("Diesel")){
				   queryString = queryString+"?cars car:hasDieselEngine ?engine.}";  
			   }
		   }
		   
		   node = execQuery(parent);
		   return node;
	   }
	
	public Node getMake(String Make,Node parent){
		   
		   Node node;
		   queryString = "";
		   
		   if(Make.equalsIgnoreCase("Audi")){
			   if(parent==null){
				   queryString =  namespace+Queries.AUDI_INSTANCES_QUERY;
			   }
			   else{
				   queryString = parent.query.substring(0, parent.query.length()-1);
				   queryString = queryString+"?cars rdf:type car:Audi.}";
			   }
		   }
		   if(Make.equalsIgnoreCase("Benz")){
			   if(parent ==null){
				   queryString =  namespace+Queries.BENZ_INSTANCES_QUERY;
			   }
			   else{
				   queryString = parent.query.substring(0, parent.query.length()-1);
				   queryString = queryString+"?cars rdf:type car:Benz.}";
			   }
		   }
		   if(Make.equalsIgnoreCase("BMW")){
			   if(parent == null){
			   queryString =  namespace+Queries.BMW_INSTANCES_QUERY;
			   }
			   else{
				   queryString = parent.query.substring(0, parent.query.length()-1);
				   queryString = queryString+"?cars rdf:type car:BMW.}";
			   }
		   }
		   if(Make.equalsIgnoreCase("Chevrolet")){
			   if(parent == null){
			   queryString =  namespace+Queries.CHEVROLET_INSTANCES_QUERY;
			   }
			   else{
				   queryString = parent.query.substring(0, parent.query.length()-1);
				   queryString = queryString+"?cars rdf:type car:Chevrolet.}";
			   }
		   }
		   if(Make.equalsIgnoreCase("Ford")){
			   if(parent == null){
			   queryString =  namespace+Queries.FORD_INSTANCES_QUERY;
			   }
			   else{
				   queryString = parent.query.substring(0, parent.query.length()-1);
				   queryString = queryString+"?cars rdf:type car:Ford.}";
			   }
		   }
		   if(Make.equalsIgnoreCase("Honda")){
			   if(parent == null){
			   queryString =  namespace+Queries.HONDA_INSTANCES_QUERY;
			   }
			   else{
				   queryString = parent.query.substring(0, parent.query.length()-1);
				   queryString = queryString+"?cars rdf:type car:Honda.}";
			   }
		   }
		   node =  execQuery(parent);
		   return node;
	   }
	
	public void deQueue(){
		if(!queue.isEmpty())
		{
			queue.remove(0);
		}
	}
	
	public Node getMinNode(){
		
		Node min,node;
		Iterator ite;
		
		min = new Node();
		min.order = Integer.MAX_VALUE;
		
		ite = queue.iterator();
		while(ite.hasNext()){
			node = (Node)ite.next();
			if(node.order<min.order){
				min = node;
			}
		}
		
		return min;
	}
	
	public ArrayList<Node> getChildNodes(Node parent,int satConstraints){
		   
		   Node acnode,bodyNode,makeNode,colorNode,engineNode;
		   acnode=bodyNode=makeNode=colorNode=engineNode=null;
		   boolean flag = false;
		   ArrayList<Node>list = new ArrayList<Node>();
		   
		   // check for AC
		   if(userinput[0] == true && !map.get("ac")){
			   acnode = getACS(ac,parent);
			   if(acnode!=null){
				   if(acnode.numInstances>=1){
					   flag = true;
					   acnode.parent = parent;
					   acnode.constraint = "ac";
					   acnode.constrSatisfied = satConstraints+1;
					   acnode.order = 3;
					   list.add(acnode);
				   }
			   }
		   }
		   
		   // check for body style
		   if(userinput[1] == true && !map.get("bodystyle")){
			   bodyNode = getBodyStyleCars(bodystyle,parent);
			   if(bodyNode!=null){
				   if(bodyNode.numInstances>=1){
					   flag = true;
					   bodyNode.parent = parent;
					   bodyNode.constraint = "bodystyle";
					   list.add(bodyNode);
					   bodyNode.order = 5;
					   bodyNode.constrSatisfied = satConstraints+1;
				   }
			   }
		   }
		 
		 //Check for Make
		   if(userinput[2] == true && !map.get("make")){
			   makeNode = getMake(make,parent);
			   if(makeNode!=null){
				   if(makeNode.numInstances>=1){
					   flag = true;
					   makeNode.parent = parent;
					   makeNode.constrSatisfied = satConstraints+1;
					   makeNode.constraint = "make";
					   list.add(makeNode);
					   makeNode.order = 1;
				   }
			   }
		   }
		   
		   //Check for Color
		   if(userinput[3] == true && !map.get("color")){
		   colorNode = getColorCars(color,parent);
		   if(colorNode!=null){
		      if(colorNode.numInstances>=1){
					   flag = true;
					   colorNode.parent = parent;
					   colorNode.constraint = "color";
					   list.add(colorNode);
					   colorNode.order = 4;
					   colorNode.constrSatisfied = satConstraints+1;
				   }
			   }
		   }
		   
		  //Check for Engine
		   if(userinput[4] == true && !map.get("engine")){
		   engineNode = getEngineType(engine,parent);
		   if(engineNode!=null){
		      if(engineNode.numInstances>=1){
					   flag = true;
					   engineNode.parent = parent;
					   engineNode.constrSatisfied = satConstraints+1;
					   engineNode.constraint = "engine";
					   list.add(engineNode);
					   engineNode.order = 2;
				   }
			   }
		   }
		   
		   if(flag){
			   satisfiedConstraints++;
		   }
		   return list;
	   }
	
	public void getSolution(){
		
		Node minchild,min;
		
		ArrayList<Node>list,childList;
		
		while(!queue.isEmpty()){
			min = getMinNode();
			if(min.parent== null){
				 set();  
			}
			map.put(min.constraint, true);
			
			// Get child nodes for the above obtained node.
			list = getChildNodes(min, satisfiedConstraints);
			if(checkGoal(list)){
				break;
			}
			while(!list.isEmpty()){
				minchild = getMinChild(list);
				map.put(minchild.constraint, true);
				childList = getChildNodes(minchild, satisfiedConstraints);
				if(childList.size()==0){
					
				}
				if(checkGoal(childList)){
					break;
				}
				list.remove(minchild);
			}
			if(goal){
				break;
			}
			queue.remove(min);
		}
		
		if(!goal && queue.isEmpty() && satisfiedConstraints!=numConstraints){
			   System.out.println("No Solution or No Car Matching User Choices");
		   }
	}
	
	public void set(){
	    satisfiedConstraints = 1;
	    map.put("ac", false);
  		map.put("bodystyle", false);
  		map.put("make", false);
  		map.put("color", false);
  		map.put("engine", false);
   }
	
	public boolean checkGoal(ArrayList<Node> list){
		Node child;
		boolean isGoal = false;
		for (int i = 0; i < list.size(); i++) {
			   child = list.get(i);
			   if(child.constrSatisfied==numConstraints){
				   printInstance(child);
				   isGoal = true;
				   goal = true;
				   break;
			   }
		   }
		return isGoal;
	}
	
	
	public Node getMinChild(ArrayList<Node> list){
		Node min,node;
		Iterator ite;
		
		min = new Node();
		min.order = Integer.MAX_VALUE;
		
		ite = list.iterator();
		while(ite.hasNext()){
			node = (Node)ite.next();
			if(node.order<min.order){
				min = node;
			}
		}
		
		return min;
	}
	
	public static void main(String[] args) {
	    Node node;
	    CarSelection2Heuristics obj = new CarSelection2Heuristics();
		obj.readUserInput();
		node = obj.initQuery();
		if(node!=null){
		   obj.checkGoal(node);
		}
		obj.getSolution();
   }
	
	public void checkGoal(Node node){
		   if(numConstraints == 1){
			   if(node.constrSatisfied==numConstraints){
				   printInstance(node);
			   }
		   }
	}	
	
	public void printInstance(Node goalNode){
		   query = QueryFactory.create(goalNode.query);
		   qe = QueryExecutionFactory.create(query, ontmodel);
		   resultSet = qe.execSelect();
		   
		   
		   QuerySolution row = resultSet.nextSolution();
		   String cars = row.get("cars").toString();
		   int index1 = cars.indexOf("#", 0);
		   cars = cars.substring(index1+1, cars.length());
		   
		   System.out.println("Solution Found "+cars);
		   
	}	
}
