/*Author - Chandan Bychapura Parameswaraiah
 * cbp140230
 * This class includes implementation of heuristic in A* algorithm 
 * for expansion and search to progress to reach the goal node.
 * The heuristic implemented in number of remaining unsatisfied constraints*/
package finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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

public class CarSelection 
{
   	int mileage;
   	
   	int price;
	
   	String ac;
   	
   	String bodystyle;
   	
   	String make;
   	
   	String color;
   	
   	String engine;
   	
   	String queryString;
   	
   	String namespace;
   	
   	Path input;
   	
   	OntModel ontmodel;
   	
   	Query query;
   	
   	QueryExecution qe;
   	
   	ResultSet resultSet;
   	
   	PriorityQueue<SearchNode> queue;
   	
   	boolean userinput[];
   	
   	boolean goal;
   	
   	int numConstraints;
   	
   	int satisfiedConstraints;
   	
   	List<SearchNode> childNodes;
   	
   	HashMap<String, Boolean>map;
   	
   	public CarSelection(){
   		
   		input = Paths.get("CarSelection.owl");
   		
   		// Initialize the ONTOLOGY model
   		ontmodel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
   		ontmodel.read(input.toUri().toString(), "RDF/XML");
   		
   		namespace = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
   					"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				    "PREFIX car: <http://www.semanticweb.org/chandan/ontologies/2014/10/untitled-ontology-9#>";
   		
   		queue = new PriorityQueue<SearchNode>();
   		userinput = new boolean[7];
   		numConstraints = 0;
   		satisfiedConstraints = 0;
   		childNodes = new ArrayList<SearchNode>();
   		mileage = price = 0;
   		goal = false;
   		
   		map = new HashMap<String, Boolean>();
   		map.put("ac", false);
   		map.put("bodystyle", false);
   		map.put("make", false);
   		map.put("color", false);
   		map.put("engine", false);
   	}
   	
   	public void readUserInput(){
   		String s;
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
   	
   public SearchNode getACS(String AC,SearchNode parent){
	   
	   SearchNode node;
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
   	
   public SearchNode getBodyStyleCars(String BodyStyle,SearchNode parent){
	   
	   SearchNode node;
	   
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
  
   public SearchNode getMake(String Make,SearchNode parent){
	   
	   SearchNode node;
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
   
   public SearchNode getColorCars(String Color,SearchNode parent){
	   
	   SearchNode node=null;
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
	   }
	   return node;
   }
   
   public SearchNode getEngineType(String Engine,SearchNode parent){

	   SearchNode node;
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
   
   public SearchNode initQuery(){
	   
	   SearchNode node=null;
	   if(!ac.isEmpty()){
		   node = getACS(ac,null);
		   if(node!=null &&node.gn>=1){
			   satisfiedConstraints = 1;
			   node.hn = numConstraints-satisfiedConstraints;
			   node.constrSatisfied = satisfiedConstraints;
			   node.fn = node.gn+node.hn;
			   node.constraint = "ac";
			   node.parent = null;
			   queue.add(node);
		   }
	   }
	   
	   if(!bodystyle.isEmpty()){
		   node = getBodyStyleCars(bodystyle,null);
		   if(node!=null && node.gn>=1){
			   satisfiedConstraints = 1;
			   node.hn = numConstraints-satisfiedConstraints;
			   node.constrSatisfied = satisfiedConstraints;
			   node.fn = node.gn+node.hn;
			   node.constraint = "bodystyle";
			   node.parent = null;
			   queue.add(node);
		   }
	   }
	   
	   if(!make.isEmpty()){
		   node = getMake(make,null);
		   if(node!=null && node.gn>=1){
			   satisfiedConstraints = 1;
			   node.hn = numConstraints-satisfiedConstraints;
			   node.constrSatisfied = satisfiedConstraints;
			   node.fn = node.gn+node.hn;
			   node.constraint = "make";
			   node.parent = null;
			   queue.add(node);
		   }
	   }
	   
	   if(!color.isEmpty()){
		   node = getColorCars(color,null);
		   if(node!=null && node.gn>=1){
			   satisfiedConstraints = 1;
			   node.hn = numConstraints-satisfiedConstraints;
			   node.constrSatisfied = satisfiedConstraints;
			   node.fn = node.gn+node.hn;
			   node.constraint = "color";
			   node.parent = null;
			   queue.add(node);
		   }
	   }
	 
	   if(!engine.isEmpty()){
		   node = getEngineType(engine,null);
		   if(node!=null && node.gn>=1){
			   satisfiedConstraints = 1;
			   node.hn = numConstraints-satisfiedConstraints;
			   node.constrSatisfied = satisfiedConstraints;
			   node.fn = node.gn+node.hn;
			   node.constraint = "engine";
			   node.parent = null;
			   queue.add(node);
		   }
	   }
	   return node;
   }
   
   public SearchNode execQuery(SearchNode parent){
		
	   List<QuerySolution>list;
	   SearchNode node = null;
	   int gn;
	   
	   query = QueryFactory.create(queryString);
	   qe = QueryExecutionFactory.create(query, ontmodel);
	   resultSet = qe.execSelect();
	   
	   list =  ResultSetFormatter.toList(resultSet);
	   
	   if(list.size()!=0){
		   node = new SearchNode();
		   gn = list.size();
		   node.setGn(gn);
		   node.hn = Integer.MIN_VALUE;
		   node.setParent(parent);
		   node.setQuery(queryString);
	   }

	   return node;
   }
   
   public ArrayList<SearchNode> getChildNodes(SearchNode parent,int satConstraints){
	   
	   SearchNode acnode,bodyNode,makeNode,colorNode,engineNode;
	   acnode=bodyNode=makeNode=colorNode=engineNode=null;
	   boolean flag = false;
	   ArrayList<SearchNode>list = new ArrayList<SearchNode>();
	   
	   // check for AC
	   if(userinput[0] == true && !map.get("ac")){
		   acnode = getACS(ac,parent);
		   if(acnode!=null){
			   if(acnode.gn>=1){
				   flag = true;
				   acnode.hn = numConstraints-(satConstraints+1);
				   acnode.fn = acnode.gn+acnode.hn;
				   acnode.parent = parent;
				   acnode.constraint = "ac";
				   acnode.constrSatisfied = satConstraints+1;
				   queue.add(acnode);
				   list.add(acnode);
			   }
		   }
	   }
	   
	   // check for body style
	   if(userinput[1] == true && !map.get("bodystyle")){
		   bodyNode = getBodyStyleCars(bodystyle,parent);
		   if(bodyNode!=null){
			   if(bodyNode.gn>=1){
				   flag = true;
				   bodyNode.hn = numConstraints-(satConstraints+1);
				   bodyNode.fn = bodyNode.gn+bodyNode.hn;
				   bodyNode.parent = parent;
				   bodyNode.constraint = "bodystyle";
				   queue.add(bodyNode);
				   list.add(bodyNode);
				   bodyNode.constrSatisfied = satConstraints+1;
			   }
		   }
	   }
	 
	 //Check for Make
	   if(userinput[2] == true && !map.get("make")){
		   makeNode = getMake(make,parent);
		   if(makeNode!=null){
			   if(makeNode.gn>=1){
				   flag = true;
				   makeNode.hn = numConstraints-(satConstraints+1);
				   makeNode.fn = makeNode.gn+makeNode.hn;
				   makeNode.parent = parent;
				   makeNode.constrSatisfied = satConstraints+1;
				   makeNode.constraint = "make";
				   queue.add(makeNode);
				   list.add(makeNode);
			   }
		   }
	   }
	   
	   //Check for Color
	   if(userinput[3] == true && !map.get("color")){
	   colorNode = getColorCars(color,parent);
	   if(colorNode!=null){
	      if(colorNode.gn>=1){
				   flag = true;
				   colorNode.hn = numConstraints-(satConstraints+1);
				   colorNode.fn = colorNode.gn+colorNode.hn;
				   colorNode.parent = parent;
				   colorNode.constraint = "color";
				   queue.add(colorNode);
				   list.add(colorNode);
				   colorNode.constrSatisfied = satConstraints+1;
			   }
		   }
	   }
	   
	  //Check for Engine
	   if(userinput[4] == true && !map.get("engine")){
	   engineNode = getEngineType(engine,parent);
	   if(engineNode!=null){
	      if(engineNode.gn>=1){
				   flag = true;
				   engineNode.hn = numConstraints-(satConstraints+1);
				   engineNode.fn = engineNode.gn+engineNode.hn;
				   engineNode.parent = parent;
				   engineNode.constrSatisfied = satConstraints+1;
				   engineNode.constraint = "engine";
				   queue.add(engineNode);
				   list.add(engineNode);
			   }
		   }
	   }
	   if(flag){
		   satisfiedConstraints++;
	   }
	   return list;
   }
   
   public boolean isGoalNode(SearchNode node){
	   
	   boolean isGoal = false;
	   
	   if(node.hn==0 && node.constrSatisfied==numConstraints){
		   isGoal = true;
	   }
	   return isGoal;
   }
   
   public void printInstance(SearchNode goalNode){
	   query = QueryFactory.create(goalNode.query);
	   qe = QueryExecutionFactory.create(query, ontmodel);
	   resultSet = qe.execSelect();
	   
	   QuerySolution row = resultSet.nextSolution();
	   String cars = row.get("cars").toString();
	   int index1 = cars.indexOf("#", 0);
	   cars = cars.substring(index1+1, cars.length());
	   
	   System.out.println("Solution Found "+cars);
   }
   
   
   public void getSolution(){
	   
	   SearchNode leastFN = new SearchNode();
	   SearchNode child;
	   ArrayList<SearchNode>list;
	   
	   while(!queue.isEmpty()){
		   
		   leastFN = queue.poll();
		   if(leastFN.parent== null){
			 set();  
		   }
		   map.put(leastFN.constraint, true);
		   list = getChildNodes(leastFN,satisfiedConstraints);
		   
		   for (int i = 0; i < list.size(); i++) {
			   child = list.get(i);
			   if(isGoalNode(child)){
				   printInstance(child);
				   goal = true;
				   break;
			   }
		   }
		   if(goal){
			   break;
		   }
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
   
   public void checkGoal(SearchNode node){
	   if(numConstraints == 1){
		   if(node.hn==0 && node.constrSatisfied==numConstraints){
			   printInstance(node);
		   }
	   }
   }
   
   public static void main(String[] args) {
	    SearchNode node;
	    CarSelection obj = new CarSelection();
		obj.readUserInput();
		node = obj.initQuery();
		if(node!=null){
		   obj.checkGoal(node);
		}
		obj.getSolution();
   }
}
