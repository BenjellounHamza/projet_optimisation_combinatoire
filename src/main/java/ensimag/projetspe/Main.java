/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import ensimag.projetspe.Matrix;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors
 */
public class Main {

    public static void main(String[] args) 
            throws FileNotFoundException, InterruptedException {
            /*BranchAndBoundTester t = new BranchAndBoundTester();
            t.test3();*/
            Matrix m = new Matrix("./test/benchmark/matrix-70-0.5");
            //Matrix m = new Matrix(6,0.5);
            //Algorithm a = new BranchAndBoundBS(m);
            BranchAndBoundDFS a = new BranchAndBoundDFS(m);
            //System.out.println("matrice initiale");
            //m.printMatrix();
            //ArrayList<Integer> l = new ArrayList<>(Arrays.asList(35, 48, 62, 64, 39, 11, 57, 29, 44, 69, 63, 17, 16, 41, 20, 36, 55, 18, 40, 4, 14, 7, 30, 46, 25, 65, 68, 66, 32, 34, 45, 58, 19, 23, 6, 9, 22, 50, 31, 13, 10, 47, 3, 26, 59, 5, 33, 37, 1, 21, 49, 51, 12, 28, 56, 27, 53, 54, 24, 0, 42, 60, 61, 2, 15, 52, 38, 67, 8, 43));
            //ArrayList<Integer> c = new ArrayList<>(Arrays.asList(5, 7, 8, 9, 15, 23, 25, 29, 33, 34, 35, 38, 40, 41, 42, 43, 45, 46, 47, 48, 53, 60, 63, 64, 65, 1, 3, 4, 10, 11, 16, 18, 19, 28, 31, 39, 44, 49, 51, 56, 58, 66, 2, 17, 20, 21, 24, 30, 50, 55, 59, 27, 36, 52, 54, 62, 13, 32, 14, 37, 6, 68, 22, 26, 67, 12, 69, 61, 57, 0));
            //m.setPermutRows(l);
            //m.setPermutColumns(c);
            /*a.execute();
            System.out.println(a.lowerBound);
            System.out.println(a.globalUpperBound);
            BranchAndBoundBS b = new BranchAndBoundBS(m);
            b.execute();
            System.out.println(b.lowerBound);*/
            //m.printMatrix();
            //System.out.println("stat = "+ a.compteur);
            /*Tester tester = new Tester();
            tester.test();*/
            //BranchAndBoundTester tester = new BranchAndBoundTester();
            //tester.test3();
            Tester test = new Tester();
            test.test();
    }
}

