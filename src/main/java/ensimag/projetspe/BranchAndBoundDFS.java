/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import java.util.ArrayList;
import static java.util.Collections.reverseOrder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import static java.util.Map.Entry.comparingByValue;
import java.util.Stack;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author omar
 */
public class BranchAndBoundDFS extends BranchAndBound {

    public BranchAndBoundDFS(Matrix matrix) {
        super(matrix);
    }
    
    public void createChildren(Node parent) {
        //creates children of parent
        if (parent.nbWhites == 1 || parent.depth == matrix.getNbRows() - 1) {
            //élimination préliminaire
            //UGLY CODE, NEEDS REFACTORING
            int oldUB = parent.UB;
            parent.setNoChildren(true);
            if (oldUB!=100000){
            leavesUB.remove(oldUB);leavesUB.add(parent.UB);}
            return;
        }
        int currentj = parent.parent==null? 0 : 
                matrix.getNbRows() - parent.nbWhites;

        sortLines(parent.depth + 1, currentj);
        parent.backupSort = new ArrayList<>(matrix.getPermutRows());
        int nbChildren = 0;
        for (int i = parent.depth + 1; i < matrix.getNbRows(); i++) {
            int cpt = parent.parent==null? matrix.getRow(i).getNbWhitesSub(0):
                    matrix.getRow(i).getNbWhitesSub(currentj);

            if(cpt == parent.nbWhites || cpt == parent.nbWhites - 1){
                    //remove all brothers and keep only this one.
                    parent.removechildren();
                    new Node(parent, cpt);
                    nbChildren = 1;
                    break;
            }
            if (cpt > 0) {
                new Node(parent, cpt);
                nbChildren++;
            }
        }
        //UGLY CODE, NEEDS REFACTORING
        int oldUB = parent.UB;
        parent.setNoChildren(nbChildren == 0);
        if (oldUB!=100000){
        leavesUB.remove(oldUB);leavesUB.add(parent.UB);}
    }

    void GUBUpdate(TreeMultiset<Integer> leavesUB){
        globalUpperBound = 
                Math.min(leavesUB.elementSet().last(), globalUpperBound);
    }

    protected Node root;
    protected LinkedHashSet<Node> leaves = new LinkedHashSet<>();
    protected TreeMultiset<Integer> leavesUB = TreeMultiset.create();

    @Override
    protected void execute() {
        root = new Node(null, this.matrix.getNbRows());
        Stack<Node> stack = new Stack<>();
        stack.push(root);
                
        Node current;
        while (!stack.empty()) {
            current = stack.peek();
            stack.pop();
            if (current.nbWhites + current.depth > lowerBound ) {
                if (current.parent==null){
                    //sortLines(0,0); reorganizeColumns();
                }else{
                    int k = current.parent.getChildren().indexOf(current);
                    lineKthMax(current, k);
                }
                createChildren(current);
                if (!current.getChildren().isEmpty()) {
                    leaves.remove(current);
                    leavesUB.remove(current.UB);
                }
                for (int i = current.getChildren().size() - 1; i >= 0; i--) {
                    stack.push(current.getChildren().get(i));
                    leaves.add(current.getChildren().get(i));
                    leavesUB.add(current.getChildren().get(i).UB);
                }
                GUBUpdate(leavesUB);
                if (matrix.maxTriangleSize() > lowerBound) {
                    lowerBound = matrix.maxTriangleSize();
                    lastPermutRows = matrix.getPermutRows();
                    lastPermutColumns = matrix.getPermutColumns();
                }
                if (lowerBound >= globalUpperBound) {
                    break;
                }
            }
        }
        if (lowerBound != globalUpperBound) {
            freakOut();
        }
    }

    void freakOut() {
        System.out.println("GUB" + globalUpperBound);
        System.out.println("LB" + lowerBound);
        matrix.initPermut();
        matrix.printMatrix();
        System.out.println(matrix.getLog());
        root.print();
        System.exit(1);
    }

    @Override
    public String toString() {
        return "BranchAndBoundDFS";
    }
}
