/*Author - Chandan Bychapura Parameswaraiah
 * cbp140230
 * This class includes Search Node implementation for the heuristic
 * involved in A* Search*/

package finalproject;


public class SearchNode implements Comparable<SearchNode>
{
    int gn;
    
    int fn;
    
    int hn;
    
    SearchNode parent;
    
    String query;
    
    String constraint;
    
    int constrSatisfied;
    
    public int getHn() {
		return hn;
	}

	public void setHn(int hn) {
		this.hn = hn;
	}

	public int getGn() {
		return gn;
	}

	public void setGn(int gn) {
		this.gn = gn;
	}

	public int getFn() {
		return fn;
	}

	public void setFn(int fn) {
		this.fn = fn;
	}

	public SearchNode getParent() {
		return parent;
	}

	public void setParent(SearchNode parent) {
		this.parent = parent;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public int compareTo(SearchNode arg0) {
		return fn-arg0.fn;
	}
}
