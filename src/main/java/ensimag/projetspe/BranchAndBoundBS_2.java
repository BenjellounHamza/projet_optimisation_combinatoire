/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*package ensimag.projetspe;

import com.google.common.collect.TreeMultiset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Stack;

/**
 *
 * @author omar_

public class BranchAndBoundBS extends BranchAndBound{
    
    int width; //beam width
    public BranchAndBoundBS(Matrix matrix, int width) {
        super(matrix);
        this.width = width;
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
        int mn = parent.depth+1==parent.levels.size()?
                parent.depth+1+width:
                parent.depth+1+width-parent.levels.get(parent.depth+1);
        mn = Math.min(mn, matrix.getNbRows());
        for (int i = parent.depth + 1; i < mn; i++) {
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
    @Override
    public void execute(){
        super.execute();
        this.setGlobalUpperBound(matrix.getNbRows());
    }
    
    @Override
    public String toString() {
        return "BranchAndBoundBS";
    }
    
}
*/