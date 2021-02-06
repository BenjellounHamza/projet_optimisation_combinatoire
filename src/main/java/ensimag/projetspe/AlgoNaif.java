package ensimag.projetspe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.*;

public class AlgoNaif extends Algorithm {

    public AlgoNaif(Matrix matrix) {
        super(matrix);
    }

    public void sortLines() {
        //sorts ela hsab NbWhites
        HashMap<Integer, Integer> map = new HashMap<>();
        int compteur = 0;
        for (Line element : matrix.getLines()) {
            map.put(compteur, element.getNbWhites());
            compteur++;
        }
        Map<Integer, Integer> sorted = map
                .entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));
        matrix.setPermutRows(new ArrayList<>(sorted.keySet()));
    }

    public void sortColumns() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < matrix.getColumns().size(); i++) {
            map.put(i, matrix.getColumn(i).getNbWhiteSpec());
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

    @Override
    protected void execute() {
        this.sortLines();
        this.sortColumns();
        lowerBound = matrix.maxTriangleSize();
    }

    @Override
    public String toString() {
        return "AlgoNaif";
    }
}
