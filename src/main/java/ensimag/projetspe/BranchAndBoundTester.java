/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author omar_
 */
public class BranchAndBoundTester {
    //tests integrity of the BranchAndBound class
    //compares the BnB result with that of AlgoNaifNaif
    //for small matrixes (where AlgoNaifNaif gives 
    // results in less than 2 sec)
    
    public BranchAndBoundTester(){
        
    }
    
    public Matrix generateMatrix(int x, int size){
        //generate matrix by transforming x to a binary sequences
        //and filling the matrix with the binary values
        ArrayList<Line> lines = new ArrayList<>();
        String binaryString = Integer.toBinaryString(x);
        char[] sequence = binaryString.toCharArray();
        for(int i=0; i<sequence.length/2; i++){
            char tmp = sequence[i];
            sequence[i] = sequence[sequence.length -i -1];
            sequence[sequence.length -i -1] = tmp;
        }
        for (int i  = 0; i<size; i++){
            ArrayList<Boolean> line = new ArrayList<>();
            for (int j = 0; j<size; j++){
                if (i*size+j<sequence.length){
                    line.add(sequence[i*size+j] == '1');
                }else{
                    line.add(false);
                }
            }
            lines.add(new Line(line));
        }
        return new Matrix(lines);
    }
    
    public void test() throws InterruptedException {
        int size = 5;
        double nbMatrixes = Math.pow(2, Math.pow(size, 2));
        
        ArrayList<Integer> shuffled = new ArrayList<>();
        for (int i =0; i<nbMatrixes; i++){
            shuffled.add(i);
        }
        Collections.shuffle(shuffled);
        
        for (int i : shuffled) {
            Matrix m = generateMatrix(i, size);
            //m.printMatrix();
            m.initPermut();
            AlgoNaifNaif a = new AlgoNaifNaif(m);
            a.runTimeoutSlow(200000);
            int t1 = m.maxTriangleSize();
            
            m.initPermut();
            BranchAndBoundDFS bnb = new BranchAndBoundDFS(m);
            bnb.runTimeoutSlow(2000000);
            int t2 = m.maxTriangleSize();
            
            if (t1==t2){
                System.out.println("done " +  
                        (shuffled.indexOf(i)/Math.pow(2, Math.pow(size, 2))));
            }
            else{
                System.out.println("FUCK: " + t1 + "," +t2);
                m.initPermut();
                m.printMatrix();
                System.exit(1);
            }
        }        
    }
    
    public void test2() throws InterruptedException{
        for (int i = 0; i<10000; i++){
            Matrix m = new Matrix(15,0.5);
            m.initPermut();
            BranchAndBoundDFS bnb = new BranchAndBoundDFS(m);
            bnb.runTimeoutSlow(100000);
            System.out.println("done" + (double)i/5000000);
            //m.printMatrix();
        }
    }
    
    public void test3() throws InterruptedException {
        int size = 6;
        for (int i = 0; i<5000000; i++){
            Matrix m = new Matrix(size,0.5);
            //m.printMatrix();
            m.initPermut();
            AlgoNaifNaif a = new AlgoNaifNaif(m);
            a.runTimeoutSlow(2000000);
            int t1 = m.maxTriangleSize();
            
            m.initPermut();
            BranchAndBoundBS bnb = new BranchAndBoundBS(m, 200000);
            bnb.execute();
            int t2 = m.maxTriangleSize();
            if (t1==t2){
                System.out.println("done " +  
                        (i/Math.pow(2, Math.pow(size, 2))));
            }
            else{
                System.out.println("FUCK: " + t1 + "," +t2);
                m.initPermut();
                m.printMatrix();
                System.exit(1);
            }
        }        
    }
    
}



