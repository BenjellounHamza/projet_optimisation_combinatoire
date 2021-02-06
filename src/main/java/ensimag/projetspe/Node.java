/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author omar
 */
public class Node implements Comparable{
    Node parent;
    private ArrayList<Node> children;
    int depth;
    int nbWhites;
    private Boolean noChildren;
    int UB;
    public ArrayList<Integer> levels;//nb of nodes in each level
    public ArrayList<Integer> backupSort=null;//permutRows when sortLines() saved here
    public int place;
    
    public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public ArrayList<Node> getChildren(){
        return children;
    }
    
    public Node(Node parent, int nbWhites){
        this.parent = parent;
        this.children = new ArrayList<>();
        this.noChildren = false; //You can't do optimization without being optimistic..
        this.nbWhites = nbWhites;
        if (parent == null){
            this.depth = -1;
            this.levels = new ArrayList<>();
        }else{
            this.depth = parent.depth +1;
            parent.children.add(this);
            this.levels = parent.levels;
            if (depth<=levels.size()-1){
                levels.set(depth, levels.get(depth)+1);
            }else{
                levels.add(1);
            }
        }
        UBUpdate();
    }
    
    public void UBUpdate(){
        if (parent==null){
            this.UB = 100000;
        }else if (parent.parent==null){
            this.UB = this.nbWhites;
            if (this.noChildren) {
                this.UB = Math.min(this.UB, 1);
            }
        }else{
            this.UB = Math.min(parent.UB, this.nbWhites+this.depth);
            if (this.noChildren) {
                this.UB = Math.min(this.UB, this.depth + 1);
            }
        }
    }
  
    public Node nextChild() {
        if (parent != null) {
            int k = parent.getChildren().indexOf(this);
            if (k + 1 == parent.getChildren().size()) {
                return parent.nextChild();
            } else {
                return parent.getChildren().get(k + 1);
            }
        }
        return null;
    }

    public void setNoChildren(Boolean noChildren) {
        this.noChildren = noChildren;
        UBUpdate();
    }
    
    public Boolean getNoChildren(){
        return noChildren;
    }
    
    public void print(){
        print(this, 0);
    }
    
    private void print(Node self, int indent){
        //prints node and all children of the node
        String s = "";
        for (int i = 0; i<indent; i++){
            s+=' ';
        }
        System.out.println(s+self.nbWhites + ";" + self.depth + "," +self.UB);       
        for (Node c : self.children){
            c.print(c, indent+1);  
        }
    }

    void removechildren() {
        this.children = new ArrayList<>();
    }
 
    @Override 
    public String toString(){
        return "Node : depth" + depth + " UB" + UB;
    }

	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nbWhites;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if ((this.getNoChildren()? this.nbWhites:(this.nbWhites + this.depth)) != (((Node)other).getNoChildren()? ((Node)other).nbWhites:(((Node)other).nbWhites + ((Node)other).depth)))
			return false;
		return true;
	}
        */
        @Override
	public int compareTo(Object other) {
		int nbrwhiteother = (((Node)other).getNoChildren()? ((Node)other).nbWhites:(((Node)other).nbWhites + ((Node)other).depth));
                int nbrwhite = (this.getNoChildren()? this.nbWhites:(this.nbWhites + this.depth));
		if(nbrwhite > nbrwhiteother) {
			return 1;
		}
		else if(nbrwhite == nbrwhiteother) {
			return 0;
		}
		else {
			return -1;
		}
	}
    
}
