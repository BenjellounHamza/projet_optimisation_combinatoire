/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import org.apache.commons.collections4.iterators.PermutationIterator;

/**
 *
 * @author omar
 */
public class AlgoNaifNaif extends Algorithm {

    public AlgoNaifNaif(Matrix matrix) {
        super(matrix);
    }

    public void reorganizeColumns() {
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

    public void reorganizeColumnsSlow() {
        int tmp;
        int currentj = matrix.getNbRows() - 1;
        while (currentj > 0) {
            int jmax = -1;
            int nmax = -1;
            for (int k = currentj; k >= 0; k--) {
                tmp = matrix.getColumn(k).getNbWhitesTop();
                if (tmp > nmax) {
                    nmax = tmp;
                    jmax = k;
                }
            }
            matrix.permutColumns(currentj, jmax);
            currentj--;
        }
    }

    @Override
    protected void execute() {
        PermutationIterator<Integer> pr;
        pr = new PermutationIterator<>(matrix.getPermutRows());
        int tailleMax = -1;
        while (pr.hasNext()) {
            ArrayList<Integer> rtmp = (ArrayList<Integer>) pr.next();
            matrix.setPermutRows(rtmp);
            reorganizeColumns();
            if (matrix.maxTriangleSize() > tailleMax) {
                tailleMax = matrix.maxTriangleSize();
                lowerBound = matrix.maxTriangleSize();
                lastPermutRows = rtmp;
                lastPermutColumns = matrix.getPermutColumns();
            }
        }
    }

    public void executeSlow() {
        PermutationIterator<Integer> pr, pc;
        pr = new PermutationIterator<>(matrix.getPermutRows());
        ArrayList<Integer> rtmp;
        ArrayList<Integer> ctmp;
        int tailleMax = -1;
        while (pr.hasNext()) {
            rtmp = (ArrayList<Integer>) pr.next();
            pc = new PermutationIterator<>(matrix.getPermutColumns());
            while (pc.hasNext()) {
                ctmp = (ArrayList<Integer>) pc.next();
                matrix.setPermutRows(rtmp);
                matrix.setPermutColumns(ctmp);
                if (matrix.maxTriangleSize() > tailleMax) {
                    tailleMax = matrix.maxTriangleSize();
                    lastPermutRows = rtmp;
                    lastPermutColumns = ctmp;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "AlgoNaifNaif";
    }
}
