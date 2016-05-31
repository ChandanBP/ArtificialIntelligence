/*Author - Chandan Bychapura Parameswaraiah
 * This class contains all the sparql queries which is used to 
 * retrieve facts from knowledge base*/

package finalproject;

public class Queries 
{
   //AC query
   public static final String AUTOMATIC_AC_QUERY = "Select ?cars ?ac WHERE{?cars car:hasAutomaticAC ?ac.}";
   
   public static final String MANUAL_AC_QUERY = "Select ?cars ?ac WHERE{?cars car:hasManualAC ?ac.}";
   
   // Body Style query
   public static final String COUPE_QUERY = "Select ?cars ?coupe WHERE{?cars car:hasCoupeBodyStyle ?coupe.}";
   
   public static final String SEDAN_QUERY = "Select ?cars ?sedan WHERE{?cars car:hasSedanBodyStyle ?sedan.}";
   
   public static final String SUV_QUERY = "Select ?cars ?suv WHERE{?cars car:hasSUVBodyStyle ?suv.}";
   
   // Color Query
   public static final String BRIGHT_COLOR_QUERY = "Select ?cars WHERE{?cars car:hasDarkColor.}";
   
   public static final String LIGHT_COLOR_QUERY = "Select ?cars WHERE{?cars car:hasLightColor.}";
   
	
   // Car Make Query	
   public static final String AUDI_INSTANCES_QUERY = "Select ?cars WHERE{?cars rdf:type car:Audi.}";
   
   public static final String BENZ_INSTANCES_QUERY = "Select ?cars WHERE{?cars rdf:type car:Benz.}";
   
   public static final String BMW_INSTANCES_QUERY = "Select ?cars WHERE{?cars rdf:type car:BMW.}";
   
   public static final String CHEVROLET_INSTANCES_QUERY = "Select ?cars WHERE{?cars rdf:type car:Chevrolet.}";
   
   public static final String FORD_INSTANCES_QUERY = "Select ?cars WHERE{?cars rdf:type car:Ford.}";
   
   public static final String HONDA_INSTANCES_QUERY = "Select ?cars WHERE{?cars rdf:type car:Honda.}";
   
   
   // Engine Queries
   public static final String PETROL_ENGINES_QUERY = "Select ?cars ?engine WHERE{?cars car:hasPetrolEngine ?engine.}";
   
   public static final String DIESEL_ENGINES_QUERY = "Select ?cars ?engine WHERE{?cars car:hasDieselEngine ?engine.}";
   
}
