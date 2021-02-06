/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author omar_
 */
public abstract class Algorithm extends Thread {

    protected final Matrix matrix;
    protected ArrayList<Integer> lastPermutRows;
    protected ArrayList<Integer> lastPermutColumns;

    protected int lowerBound;
    protected int globalUpperBound;

    public int getLowerBound() {
        return lowerBound;
    }

    public int getGlobalUpperBound() {
        return globalUpperBound;
    }
    
    public void setGlobalUpperBound(int gub) {
        this.globalUpperBound = gub;
    }

    public Algorithm(Matrix matrix) {
        this.matrix = matrix;
        matrix.initPermut();
        lastPermutRows = matrix.getPermutRows();
        lastPermutColumns = matrix.getPermutColumns();
        lowerBound = 0;
        globalUpperBound = matrix.getNbRows();
    }

    public Matrix getMatrix() {
        return matrix;
    }

    protected abstract void execute();

    @Override
    public void run() {
        matrix.initPermut();
        execute();
        matrix.setPermutRows(lastPermutRows);
        matrix.setPermutColumns(lastPermutColumns);
    }

    public boolean runTimeout(int timeout)
            throws InterruptedException {
        //1sec by defaut
        return runTimeout(timeout, timeout / 1000);
    }

    public boolean runTimeout(int timeout, int nbIterations)
            throws InterruptedException {
        //return true if the thread takes longer than timeout
        Thread timer = new Thread(new timer());
        timer.start();
        this.start();
        long startTime = System.currentTimeMillis();
        int k = 1;
        while (System.currentTimeMillis() - startTime < timeout
                && this.isAlive()) {
            timer.sleep(timeout / nbIterations);
            k++;
        }
        timer.stop();
        if (this.isAlive()) {
            this.stop();
            matrix.setPermutRows(lastPermutRows);
            matrix.setPermutColumns(lastPermutColumns);
            return true;
        }
        return false;
    }

    public boolean runTimeoutSlow(int timeout) {
        //takes 2 times more than execute()
        long startTime = System.currentTimeMillis();
        this.start();
        while (System.currentTimeMillis() - startTime <= timeout
                && this.isAlive()) {

        }
        if (this.isAlive()) {
            this.stop();
            matrix.setPermutRows(lastPermutRows);
            matrix.setPermutColumns(lastPermutColumns);
            return true;
        }
        return false;
    }

}

class timer implements Runnable {

    @Override
    public void run() {
        //while(true){
        // System.out.println("MyClass running");
        //}
    }

}
