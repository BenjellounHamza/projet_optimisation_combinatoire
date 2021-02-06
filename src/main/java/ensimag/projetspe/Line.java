/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

import java.util.ArrayList;

/**
 *
 * @authors
 */
public class Line {

    private ArrayList<Boolean> elements;
    private int nbWhites;
    private int nbBlacks;

    public ArrayList<Boolean> getElements() {
        return elements;
    }

    public int getNbWhites() {
        return nbWhites;
    }

    public int getNbBlacks() {
        return nbBlacks;
    }
    public int getNbWhitesTop(){
        return getNbWhiteSpec();
    }

    public int getNbWhiteSpec() {
        //counts number of whites starting from the top
        // used for columns
        int compteur = 0;
        while (compteur < nbWhites) {
            if (this.elements.get(compteur)) {
                break;
            }
            compteur++;
        }
        return compteur;
    }

    public int getNbWhitesRight() {
        // counts number of whites starting from the right until a black is found
        int compteur = 0;
        for (int index = this.getElements().size() - 1; index >= 0; index--) {
            if (this.getElements().get(index)) {
                break;
            } else {
                compteur++;
            }
        }
        return compteur;
    }

    public int getNbWhitesSub(int index) {
        //counts number of whites in [index, line.size()]
        int compteur = 0;
        for (int i = index; i < elements.size(); i++) {
            if (!elements.get(i)) {
                compteur++;
            }
        }
        return compteur;
    }

    public Line(ArrayList<Boolean> elements) {
        this.elements = elements;
        nbWhites = 0;
        for (Boolean bool : elements) {
            if (!bool) {
                nbWhites++;
            }
        }
        nbBlacks = elements.size() - nbWhites;
    }

    public void printLine() {
        for (Boolean element : elements) {
            if (element) {
                System.out.print(1);
            } else {
                System.out.print(0);
            }
            System.out.print("  ");
        }
        System.out.println();
    }

    public void add(Boolean bool) {
        this.elements.add(bool);
        if (!bool) {
            nbWhites++;
        } else {
            nbBlacks++;
        }
    }

}
