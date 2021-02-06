/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.reverseOrder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.Entry.comparingByValue;
import java.util.Stack;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author omar
 */
public class BranchAndBound{

    private final Matrix matrix;

    public BranchAndBound(Matrix matrix) {
        this.matrix=matrix;
    }

    private int lowerBound;
    private int globalUpperBound;

    public int getLowerBound() {
        return lowerBound;
    }

    public int getGlobalUpperBound() {
        return globalUpperBound;
    }

    public void sortLines(int iline, int jcolumn) {
        matrix.addLog("sortLines " + iline +"," +jcolumn);
        //sorts the lines in the submatix defined by iline and jcolumn
        //O(n-iline^2) (can be optimized)
        int currenti = iline;
        while (currenti < matrix.getNbRows()) {
            //search for max and swap with currenti
            int nbmax = -1;
            int imax = -1;
            for (int k = currenti; k < matrix.getNbRows(); k++) {
                if (matrix.getRow(k).getNbWhitesSub(jcolumn) > nbmax) {
                    nbmax = matrix.getRow(k).getNbWhitesSub(jcolumn);
                    imax = k;
                }
            }
            matrix.permutRows(currenti, imax);
            currenti++;
        }
    }

    public void lineKthMax(int iline, int jcolumn, int K) {
        matrix.addLog("lineKthMax " + iline + "," +jcolumn +"," +K);
        //on the submatrix defined by iline and jcolumn
        //WE SUPPOSE THE LINES ARE ALREADY SORTED (using sortLines)
        //swaps the Kth max line with the first one (K=0 does nothing)
        sortLines(iline, jcolumn+1);
        matrix.permutRows(iline, iline + K);
        reorganizeColumns(iline, jcolumn+1);
        //Copyrights to Omar Corp, estimated net value of 0.1 pesos
    }

    public void reorganizeColumns(int iline, int jcolumn) {
        matrix.addLog("reorganize " + iline +"," +jcolumn);
        //reorganizes columns such that we maximize the count
        //of whites in the submatrix defined by iline and jcolumn
        Line line = matrix.getRow(iline);
        int start = line.getElements().size() - 1;
        int end = jcolumn;
        int startSearch;
        int endSearch;
        while (start > end) {
            if (line.getElements().get(start)) {
                //swap with the first 0 beginning from the "end"
                startSearch = end;
                endSearch = start;
                while (line.getElements().get(startSearch)
                        && startSearch < endSearch) {
                    startSearch++;
                }
                if (startSearch < endSearch) {//we swap!
                    matrix.permutColumns(startSearch, endSearch);
                    assert (startSearch >= jcolumn);
                    assert (endSearch >= jcolumn);
                    line = matrix.getRow(iline);
                }
            }
            start--;
        }
    }

    public void createChildren(Node parent, int currentj) {
        matrix.addLog("createChildren " + parent.nbWhites + "," + parent.depth +"," +currentj);
        //creates children for a node
        sortLines(parent.depth + 1, currentj+1);
        reorganizeColumns(Math.min(parent.depth + 1, matrix.getNbRows()-1) , currentj+1);

        int nbChildren = 0;
        int cpt; Node tmp;
        for (int i = parent.depth+1; i < matrix.getNbRows(); i++) {
            cpt = matrix.getRow(i)
                    .getNbWhitesSub(currentj+1);
            if (cpt>0){
            //if (cpt + parent.depth+1 > lowerBound) {
                tmp = new Node(parent, cpt); nbChildren++;
            }
        }
        parent.noChildren = (nbChildren==0);
    }

    void GUBUpdate(Node racine){
        int max = -1;
        Stack<Node> stack = new Stack<>();
        stack.push(racine);
        Node current;
        while (!stack.empty()){
            current = stack.peek(); stack.pop();
            for (Node child : current.getChildren()){
                stack.push(child);
            }
            if (current.getChildren().isEmpty()){//c'est une feuille
                int UB = (current.noChildren)? current.depth+1 
                        : current.depth+current.nbWhites;
                if (UB>max){
                    max = UB;
                }
            }
        }
        assert(max!=-1);
        globalUpperBound = Math.min(max, globalUpperBound);
    }


    public void execute() {
        globalUpperBound = matrix.getNbRows();
        lowerBound = 0;
        Node racine = new Node(null);
        Stack<Node> stack = new Stack<>();
        createChildren(racine, -1);
        for (int index = racine.getChildren().size() - 1; index >= 0; index--) {
            stack.push(racine.getChildren().get(index));
        }
        GUBUpdate(racine);

        Node current;
        int currenti;
        int currentj;
        int oldj;
        int k;
        while (!stack.empty()) {
            current = stack.peek();
            stack.pop();
            currenti = current.depth;
            if (current.parent.parent==null){//first generation
                oldj = 0;
            } else {
                oldj = matrix.getNbRows() - current.parent.nbWhites;
            }
            k = current.parent.getChildren().indexOf(current);
            lineKthMax(currenti, oldj, k);
            currentj = matrix.getNbRows() - matrix.getRow(currenti)
                    .getNbWhitesSub(oldj);

            createChildren(current, currentj);
            
            String s=""; int tmp;
            for (int i = 0; i<matrix.getNbRows(); i++){
                for (int j = 0; j<matrix.getNbRows(); j++){
                    tmp = matrix.getElement(i, j)?1:0;
                    s+= tmp + " ";
                }
                s+="\n";
            }
            matrix.addLog(s);
            
            for (int index = current.getChildren().size() - 1; index >= 0; index--) {
                stack.push(current.getChildren().get(index));
            }
            GUBUpdate(racine);

            if (matrix.maxTriangleSize()>lowerBound){
                lowerBound = matrix.maxTriangleSize();
            }

            if (lowerBound==globalUpperBound){
                break;
            }
        }

        if (lowerBound != globalUpperBound){
            System.out.println("GUB" + globalUpperBound);
            System.out.println("LB" + lowerBound);
            matrix.printMatrix();
            System.out.println(matrix.getLog());
            System.exit(1);
        }
    }


}
