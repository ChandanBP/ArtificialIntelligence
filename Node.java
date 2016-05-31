/*Author - Chandan Bychapura Parameswaraiah
 * cbp140230
 * This class includes Search Node implementation for the heuristic
 * involved in Greedy Best First Search */

package finalproject;

public class Node implements Comparable<Node>
{
	boolean hn;
	
	int constrSatisfied;
	
	int numInstances;
	
	int order;
	
	String query;
    
    String constraint;
	
    Node parent;
    
    public int getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public boolean isHn() {
		return hn;
	}

	public void setHn(boolean hn) {
		this.hn = hn;
	}

	public int getConstrSatisfied() {
		return constrSatisfied;
	}

	public void setConstrSatisfied(int constrSatisfied) {
		this.constrSatisfied = constrSatisfied;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.order-o.order;
	}
}
