package ensimag.projetspe;

import java.util.ArrayList;
import java.util.Collections;

public class BranchAndBoundBS extends BranchAndBound{
        int D;
	ArrayList<Node> Nodes;
	public BranchAndBoundBS(Matrix matrix, int d) {
		super(matrix);
		Nodes = new ArrayList<>();
                D = d;
	}
        public BranchAndBoundBS(Matrix matrix) {
		super(matrix);
		Nodes = new ArrayList<>();
                D = matrix.getNbRows() > 50 ? 50: matrix.getNbRows();
	}

	
	@Override
        public void createChildren(Node parent) {
        if(!parent.getNoChildren()){
        parent.removechildren();
	reorganizematrix(parent);
        int currentj = parent.parent==null? 0 : matrix.getNbRows() - parent.nbWhites;
        Boolean nochildren = true; 
        for (int i = parent.depth + 1; i < matrix.getNbRows(); i++) {
            int cpt = parent.parent==null? matrix.getRow(i).getNbWhitesSub(0): matrix.getRow(i).getNbWhitesSub(currentj);
            if (cpt > 0) {
            	Node children = new Node(parent, cpt);
            	children.setPlace(matrix.getPermutRows().get(i));
                nochildren = false;
            }
        }
        parent.setNoChildren(nochildren);
        }
        else{
            	reorganizematrix(parent);
        }
	}

	private void reorganizematrix(Node node) {
		/*reorganize rows*/
		Node current = node;
		while(current.parent != null) {
			/* place current in current.depth*/
			matrix.permutRows(current.depth, matrix.getPermutRows().indexOf(current.place));
			current = current.parent;
		}
		/*reorganize columns*/
		this.reorganizeColumns();
	}

	public void execute() {
			Node root = new Node(null, this.matrix.getNbRows());
                        this.createChildren(root);
			ArrayList<Node> preNodes = root.getChildren(); 
			//Collections.sort(preNodes, (p1,p2)->{return p1.compareTo(p2);});
                        Collections.sort(preNodes, Collections.reverseOrder());
                        if(D < preNodes.size()){
                            preNodes = new ArrayList<>(preNodes.subList(0, D));
                        }
                        ArrayList<Node> newNodes = new ArrayList<>();
			for(int i = 0; i < matrix.getNbRows(); i++) {
			newNodes = new ArrayList<>();
			for(Node node :preNodes) {
                                this.createChildren(node);
				for(Node n:node.getChildren()) {
                                    newNodes.add(n);
				}
                                if(node.getNoChildren()){
                                    newNodes.add(node);
                                    node.nbWhites = matrix.maxTriangleSize();                                    
                                }
			}
                        Collections.sort(newNodes, Collections.reverseOrder());
                        if(D < newNodes.size()){
                            preNodes = new ArrayList<>(newNodes.subList(0, D));
                            Node first = preNodes.get(0);
                            this.reorganizematrix(first);
                            this.lowerBound = matrix.maxTriangleSize();
                        }
                        else if(!newNodes.isEmpty()){
                            preNodes = newNodes;
                        }
                        else{
                            break;
                        }
                    }
                        Node optinode = preNodes.get(0);
                        reorganizematrix(optinode);
                        this.lowerBound = matrix.maxTriangleSize();
                        //matrix.printMatrix();
        }
}
