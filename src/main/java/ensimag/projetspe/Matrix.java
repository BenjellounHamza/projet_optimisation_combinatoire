/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Spec
 */
public class Matrix {
    
    private String log; //logs any changes we apply to the matrix
    
    public String getLog(){
        return log;
    }
    
    public void addLog(String s){
        log += s + "\n";
    }
    
    private int nbRows;
    private int nbColumns;
    private double density;
       
    public int getNbRows() {
        return nbRows;
    }

    public int getNbColumns() {
        return nbColumns;
    }
    
    public ArrayList<Line> getLines() {
	return lines;
    }

    public ArrayList<Line> getColumns() {
        return columns;
    }

    public double getDensity() {
        return density;
    }
    
    public ArrayList<Line> lines;
    private ArrayList<Line> columns;
    
    public Matrix(int dimension){
        this(dimension, dimension, (new Random()).nextDouble());
    }
    
    public Matrix(int dimension, double density){
        this(dimension, dimension, density);
    }
    
    public Matrix(int nbRows, int nbColumns, double density){
        //constructor of a random matrix with the given density
        assert(density>=0 && density<=1);
        this.nbColumns = nbColumns; this.nbRows = nbRows; this.density = density;
        lines = new ArrayList<>();
        columns = new ArrayList<>();
        ArrayList<Boolean> line; 
        Boolean bool;
        Random rand = new Random();
        for (int j = 0; j<nbColumns; j++){
            columns.add(new Line(new ArrayList<>()));
        }
        for (int j = 0; j<nbColumns; j++){
            line = new ArrayList<>();
            for (int i = 0; i<nbRows; i++){
                 bool = rand.nextDouble()>density;
                 line.add(bool);
                 columns.get(i).add(bool);
             }
            lines.add(new Line(line));
        }
        this.initPermut();
    }
    public Matrix(ArrayList<Line> lines){
        //internal constructor
        this.modifMatrix(lines);
    }
    private void modifMatrix(ArrayList<Line> lines){
        this.lines = lines;
        this.columns = new ArrayList<>();
        this.nbRows = lines.size();
        this.nbColumns = lines.get(0).getElements().size();
        int nbWhites = 0;
        ArrayList<Boolean> line; 
        for (int j = 0; j<nbColumns; j++){
            columns.add(new Line(new ArrayList<>()));
        }
        for (int j = 0; j<nbRows; j++){
            line = lines.get(j).getElements();
            nbWhites += lines.get(j).getNbWhites();
            for (int i = 0; i<nbColumns; i++){
                 columns.get(i).add(line.get(i));
             }
        }
        this.density = (double) nbWhites / (nbRows * nbColumns);
        this.initPermut();
    }

    public Matrix(String fileName) throws FileNotFoundException {//reads file and constructs the matrix
        Scanner scanner = new Scanner(new File(fileName));
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Boolean> line;
        while (scanner.hasNextLine()) {
            String sline = scanner.nextLine();
            String[] elements = sline.split(" ");
            line = new ArrayList<>();
            for (String element : elements) {
                line.add(Integer.parseInt(element) == 1);
            }
            lines.add(new Line(line));
        }
        this.modifMatrix(lines);
    }

    public Matrix(String fileName, Boolean prof) throws FileNotFoundException {
        //parses the weird format of a matrix file given in test/prof/
        //ONLY works for square matrix
        ArrayList<Line> lines = new ArrayList<>();    
        Scanner scanner = new Scanner(new File(fileName));
        String sline = scanner.nextLine();
        while (scanner.hasNextLine()){
            sline+=scanner.nextLine();
        }
        String[] split = sline.split("#");
        
        int taille = split.length;
        for (int i = 0; i<split.length; i++) {
            String line = split[i];
            line = line.split(":")[1];//remove "i:"
            String[] elements = line.split(",");
            for (String element : elements){
                int index = Integer.parseInt(element)+1;
                if (index>taille){
                    taille=index;
                }
            }
        }
        for (int i = 0; i<split.length; i++) {
            String line = split[i];
            line = line.split(":")[1];//remove "i:"
            ArrayList<Boolean> bools = new ArrayList<>();
            String[] elements = line.split(",");
            for (int j = 0; j<taille; j++){
                bools.add(false);
            }
            for (String element : elements){
                int index = Integer.parseInt(element);
                bools.set(index,true);
            }
            lines.add(new Line(bools));
        }
        for (int i = split.length; i<taille; i++){
            ArrayList<Boolean> bools = new ArrayList<>();
            for (int j = 0; j<taille; j++){
                bools.add(false);
            }
            lines.add(new Line(bools));
        }
        this.modifMatrix(lines);
    }

    private ArrayList<Integer> permutRows;
    private ArrayList<Integer> permutColumns;

    public ArrayList<Integer> getPermutRows() {
        ArrayList<Integer> copy = new ArrayList<>();
        for (Integer element : permutRows){
            copy.add(element);
        }
        return copy;
    }

    public void setPermutRows(ArrayList<Integer> permutRows) {
        assert(permutRows.size()==this.getNbRows());
        /*log += "setPermutRows() called : \n before : \n" 
                + this.getPermutRows().toString() + "\n";*/
        this.permutRows = permutRows;
        /*log += "after : \n" 
                + this.getPermutRows().toString() + "\n";*/
    }

    public ArrayList<Integer> getPermutColumns() {
        return (ArrayList<Integer>)permutColumns.clone();
    }

    public void setPermutColumns(ArrayList<Integer> permutColumns) {
        /*log += "setPermutColumns() called : \n before : \n" 
                + this.getPermutColumns().toString() + "\n";*/
        this.permutColumns = permutColumns;
        /*log += "after : \n" 
                + this.getPermutColumns().toString() + "\n";*/
    }
    public void initPermut(){
        //log += "initPermut() called : \n";
        this.permutRows = new ArrayList<>();
        for(int i = 0; i < nbRows; i++){
            this.permutRows.add(i);
        }
        this.permutColumns = new ArrayList<>();
        for(int i = 0; i < nbColumns; i++){
            this.permutColumns.add(i);
        }
        /*log += "\n initPermut() called : \n permutRows : \n" +
                permutRows.toString() + "\n permutColumns : \n" +
                permutColumns.toString() + "\n";*/
    }
    public void printMatrix(){
        for (int i = 0; i < nbRows; i++){
            this.getRow(i).printLine();
        }
        System.out.println();
    }
    public Line getRow(int i){
        Line L = this.lines.get(this.permutRows.get(i));
        ArrayList<Boolean> newL = new ArrayList<>();
        for(int j = 0; j < L.getElements().size(); j++){
            newL.add(L.getElements().get(permutColumns.get(j)));
        }
        return new Line(newL);
    }
    public Line getColumn(int i){
        Line C = this.columns.get(this.permutColumns.get(i));
        ArrayList<Boolean> newC = new ArrayList<>();
        for(int j = 0; j < C.getElements().size(); j++){
            newC.add(C.getElements().get(this.permutRows.get(j)));
        }
        return new Line(newC);
    }
    public boolean getElement(int i, int j){
        Line L = this.lines.get(this.permutRows.get(i));
        return L.getElements().get(this.permutColumns.get(j));
    }
 
    public int maxTriangleSize() {
        int max = -1;
        Boolean bool = true;
        int currentI = 0;
        int currentJ = nbColumns - 1;
        while (bool) {
            max += 1;
            if (max>nbColumns-1){//ca suffit mon petit
                break;
            }
            while (currentJ < nbColumns) {
                if (currentJ==-1){
                    printMatrix();
                    System.exit(0);
                }
                if (this.getElement(currentI, currentJ)) {
                    bool = false;
                    break;
                }
                currentJ += 1;
                currentI += 1;
            }
            currentJ = nbColumns - (max + 1 + 1);
            currentI = 0;
        }
        return max;
    }
    public void permutRows(int i, int j) {
        /*if (i != j) {
            log += "permutRows(" + Integer.toString(i) + "," + Integer.toString(j)
                    + ") called : \n before : \n"
                    + this.getPermutRows().toString() + "\n";*/
            int tmp = permutRows.get(i);
            this.permutRows.set(i, permutRows.get(j));
            this.permutRows.set(j, tmp);
        /*    log += "after : \n"
                    + this.getPermutRows().toString() + "\n";
        }*/
    }
    public void permutColumns(int i, int j) {
        /*if (i != j) {
            log += "permutColumns(" + Integer.toString(i) + Integer.toString(j)
                    + ") called : \n before : \n"
                    + this.getPermutColumns().toString() + "\n";*/
            int tmp = permutColumns.get(i);
            this.permutColumns.set(i, permutColumns.get(j));
            this.permutColumns.set(j, tmp);
        /*    log += "after : \n"
                    + this.getPermutColumns().toString() + "\n";
        }else{
            log += "permutColumns(" + Integer.toString(i) + "," 
                    + Integer.toString(i) + ") : doing nothing \n";
        }*/
    }
    public void printFile(String fileName){
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for (Line line : lines){
                for (Boolean element : line.getElements()){
                    if (element){
                        writer.print(1);
                    }
                    else{
                        writer.print(0);
                    }
                    writer.print(" ");
                }
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("ERROR");
            Logger.getLogger(Matrix.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    @Override
    public String toString() {
        String s = "";
        int tmp;
        for (int i = 0; i < getNbRows(); i++) {
            for (int j = 0; j < getNbRows(); j++) {
                tmp = getElement(i, j) ? 1 : 0;
                s += tmp + " ";
            }
            s += "\n";
        }
        return s;
    }
}
