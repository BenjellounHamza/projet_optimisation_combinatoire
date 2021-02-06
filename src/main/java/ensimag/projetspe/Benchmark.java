/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensimag.projetspe;

/**
 *
 * @authors 
 */
public class Benchmark {
    int sizes[] = {10, 20, 30, 40, 50, 60, 70, 100, 200, 300, 400, 500};
    double densities[] = {0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8};
    
    void generator(){
        for (int s = 0; s<sizes.length; s++){
            for (int d = 0; d<densities.length; d++){
                Matrix m = new Matrix(sizes[s], densities[d]); 
                m.printFile("./test/matrix" + "-" + sizes[s] + "-" + densities[d] );
            }
        }

    }
    
}
