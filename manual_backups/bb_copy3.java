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
public class BranchAndBound {

    private final Matrix matrix;

    public BranchAndBound(Matrix matrix) {
        this.matrix = matrix;
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
        matrix.addLog("sortLines " + iline + "," + jcolumn);
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

    public void lineKthMax(Node node, int K) {
        matrix.addLog("lineKthMax " + node.nbWhites + "," + node.depth + "," + K);
        //swaps the Kth max line with the first one (K=0 does nothing)
        int i = node.depth;
        int j;
        if (node.parent.parent == null) {
            j = 0;
        } else {
            j = matrix.getNbRows() - node.parent.nbWhites;
        }
        sortLines(i, j);
        matrix.permutRows(i, i + K);
        reorganizeColumns(i, j);
        //Copyrights to Omar Corp, estimated net value of 0.1 pesos
    }

    public void reorganizeColumns(){
        int currentj = matrix.getNbRows()-1;
        while (currentj>0){
            int jmax = -1; int nmax = -1;
            for (int k = currentj; k>0; k--){
                int tmp = matrix.getColumn(k).getNbWhitesTop();
                if (tmp>nmax){
                    nmax = tmp;
                    jmax = k;
                }
            }
            matrix.permutColumns(currentj, jmax);
            currentj--;
        }
    }
    
    public void reorganizeColumns(int iline, int jcolumn) {
        matrix.addLog("reorganize " + iline + "," + jcolumn);
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

    public void createChildren(Node parent) {
        //creates children of parent
        matrix.addLog("createChildren " + parent.nbWhites + "," + parent.depth);
        if (parent.nbWhites == 1 || parent.depth == matrix.getNbRows() - 1) {
            //élimination préliminaire
            parent.noChildren = true;
            return;
        }

        int currentj;
        if (parent.parent == null) {
            currentj = 0;
        } else {
            currentj = matrix.getNbRows() - parent.nbWhites;
        }

        sortLines(parent.depth + 1, currentj);
        reorganizeColumns(parent.depth + 1, currentj);
        int nbChildren = 0;
        int cpt;
        for (int i = parent.depth + 1; i < matrix.getNbRows(); i++) {
            if (parent.parent == null) {
                cpt = matrix.getRow(i)
                        .getNbWhitesSub(0);
            } else {
                cpt = matrix.getRow(i)
                        .getNbWhitesSub(currentj);
                if (cpt == parent.nbWhites) {
                    cpt--;
                }
            }
            if (cpt > 0) {
                //if (cpt + parent.depth > lowerBound) {
                new Node(parent, cpt);
                nbChildren++;
            }
        }
        parent.noChildren = (nbChildren == 0);

    }

    void GUBUpdate(Node racine) {
        int max = -1;
        Stack<Node> stack = new Stack<>();
        stack.push(racine);
        Node current;
        while (!stack.empty()) {
            current = stack.peek();
            stack.pop();
            for (Node child : current.getChildren()) {
                stack.push(child);
            }
            if (current.getChildren().isEmpty()) {//c'est une feuille
                int UB = (current.noChildren) ? current.depth + 1
                        : current.depth + current.nbWhites;
                if (UB > max) {
                    max = UB;
                }
            }
        }
        assert (max != -1);
        globalUpperBound = Math.min(max, globalUpperBound);
    }

    public void execute() {
        ArrayList<Integer> lastPermutRows = matrix.getPermutRows();
        ArrayList<Integer> lastPermutColumns = matrix.getPermutColumns();
        
        globalUpperBound = matrix.getNbRows();
        lowerBound = 0;
        Node racine = new Node(null);
        Stack<Node> stack = new Stack<>();
        createChildren(racine);
        for (int index = racine.getChildren().size() - 1; index >= 0; index--) {
            stack.push(racine.getChildren().get(index));
        }
        matrixLog();
        GUBUpdate(racine);

        Node current;
        int k;
        while (!stack.empty()) {
            current = stack.peek();
            stack.pop();
            if (current.nbWhites + current.depth >= lowerBound) {
                matrix.addLog("Node " + current.nbWhites + " , " + current.depth);

                k = current.parent.getChildren().indexOf(current);
                lineKthMax(current, k);
                createChildren(current);
                matrixLog();
                for (int index = current.getChildren().size() - 1; index >= 0; index--) {
                    stack.push(current.getChildren().get(index));
                }
                GUBUpdate(racine);
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
        matrix.setPermutRows(lastPermutRows);
        matrix.setPermutColumns(lastPermutColumns);
        if (lowerBound != globalUpperBound) {
            System.out.println("GUB" + globalUpperBound);
            System.out.println("LB" + lowerBound);
            System.out.println(matrix.getLog());
            racine.print();
            System.exit(1);
        }
    }

    public void matrixLog() {
        String s = "";
        int tmp;
        for (int i = 0; i < matrix.getNbRows(); i++) {
            for (int j = 0; j < matrix.getNbRows(); j++) {
                tmp = matrix.getElement(i, j) ? 1 : 0;
                s += tmp + " ";
            }
            s += "\n";
        }
        matrix.addLog(s);
    }
}
