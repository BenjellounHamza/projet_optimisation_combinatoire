package ensimag.projetspe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tester {

    private ArrayList<Algorithm> algoList(Matrix m) {
        ArrayList<Algorithm> algos = new ArrayList<>();
        //algos.add(new AlgoNaifNaif(m));
        //algos.add(new AlgoNaif(m));
        algos.add(new BranchAndBoundBS(m));
        algos.add(new BranchAndBoundDFS(m));
        return algos;
    }

    public void test() throws InterruptedException {
        File dir = new File("./test/benchmark");
        File[] directoryListing = dir.listFiles();
        try {
            PrintWriter writer = new PrintWriter("Log.csv", "UTF-8");
            String firstLine = "Size,Density,LowerBoundBS,LowerBoundDFS,UpperBoundDFS";
            //for (Algorithm algo : algoList(new Matrix(5, 0))) {//useless dummy matrix
            //    firstLine += "," + algo.toString();
            //}
            //firstLine += ",OPTIMAL?";
            writer.println(firstLine);
            String str;
            int k = 0;
            for (File child : directoryListing) {
                Matrix m = new Matrix(child.getAbsolutePath());
                int LB = 0;
                int GUB = m.getNbRows();
                Boolean optimal = false;
                ArrayList<Integer> sizes = new ArrayList<>();
                for (Algorithm algo : algoList(m)) {
                    optimal = algo.runTimeout(60000, 60);
                    LB = algo.lowerBound;
                    GUB = algo.globalUpperBound;
                    sizes.add(algo.lowerBound);
                }

                str = "";
                str = str + Integer.toString(m.getNbColumns())
                        + "," + Integer.toString((int) (100 * m.getDensity()));
                        
                for (int size : sizes) {
                    str += "," + Integer.toString(size);
                }
                str += "," + Integer.toString(GUB);
                //str += "," + (optimal == false);
                writer.print(str + "\n");
                System.out.println("done for matrix"
                        + Integer.toString(m.getNbColumns())
                        + "," + Integer.toString((int) (100 * m.getDensity())));

            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("ERROR");
            Logger.getLogger(Matrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
