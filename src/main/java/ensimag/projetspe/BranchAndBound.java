/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.ArrayList;
import static java.util.Collections.reverseOrder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author benjelloun
 */
public abstract class BranchAndBound extends Algorithm{

    public BranchAndBound(Matrix matrix) {
        super(matrix);
    }
   public abstract void createChildren(Node parent);
    public void sortLines(int iline, int jcolumn) {
        //expected complexity O(nlogn)
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = iline; i < matrix.getNbRows(); i++) {
            Line line = matrix.getRow(i);
            int index = matrix.getPermutRows().get(i);
            map.put(index, line.getNbWhitesSub(jcolumn));
        }
        Map<Integer, Integer> sorted = map
                .entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));
        ArrayList<Integer> finalset = new ArrayList<>();
        for (int i = 0; i < iline; i++) {
            finalset.add(matrix.getPermutRows().get(i));
        }
        for (int element : sorted.keySet()) {
            finalset.add(element);
        }
        matrix.setPermutRows(finalset);
    }
    
    public void reorganizeColumns() {
        //expected complexity O(nlogn)
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < matrix.getNbRows(); i++) {
            Line line = matrix.getColumn(i);
            int index = matrix.getPermutColumns().get(i);
            map.put(index, line.getNbWhitesTop());
        }
        Map<Integer, Integer> sorted = map
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));
        matrix.setPermutColumns(new ArrayList<>(sorted.keySet()));
    }

    public void lineKthMax(Node node, int K){
        //puts the Kth best one on top O((K+1)(n-K))(->heap for better complexity)
        int i = node.depth;
        int j = (node.parent.parent==null)? 0 
                : matrix.getNbRows() - node.parent.nbWhites;
        if (K==0){
            
        }else{
            matrix.setPermutRows(node.parent.backupSort);
            matrix.permutRows(i, i+K);
        }
        reorganizeColumns();
    }
    
}
